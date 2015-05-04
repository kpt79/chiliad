/*
 * Copyright 2015 Kovacs Peter Tibor
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package chiliad.parser.pdf;

import chiliad.parser.pdf.extractor.PageExtractor;
import chiliad.parser.pdf.input.PDFSource;
import chiliad.parser.pdf.model.MPage;
import chiliad.parser.pdf.output.ParserOutputWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChiliadPDFParser implements PDFParser {

    final static Logger LOG = LoggerFactory.getLogger(ChiliadPDFParser.class);

    private final PDFSource source;
    private final ParserOutputWriter output;
    private final PageExtractor[] extractors;

    private int startPage = 1;
    private int endPage = 1;
    private PDDocument doc;

    public ChiliadPDFParser(PDFSource source, ParserOutputWriter output, PageExtractor... extractors) {
        this.source = source;
        this.extractors = extractors;
        this.output = output;
    }

    protected PDDocument loadDocument() {
        try {
            this.doc = source.loadDocument();
            return doc;
        } catch (IOException ex) {
            LOG.error("Exception while loading document {}.", ex, source);
            throw new RuntimeException(ex);
        }
    }

    protected PDPage getPDPage(int pageNumber) {
        if (doc == null) {
            loadDocument();
        }
        List<COSObjectable> pages = doc.getDocumentCatalog().getAllPages();
        Iterator<COSObjectable> pageIter = pages.iterator();
        int currentPageNumber = 0;
        while (pageIter.hasNext()) {
            currentPageNumber++;
            PDPage currentPage = (PDPage) pageIter.next();
            if (currentPageNumber == pageNumber) {
                return currentPage;
            }
        }
        throw new IllegalStateException("Page " + currentPageNumber + " does not exists!");
    }

    @Override
    public void parse() {
        if (doc == null) {
            loadDocument();
        }
        startDocument(doc);

        if (doc.isEncrypted()) {
            try {
                // We are expecting non-encrypted documents here, but it is common
                // for users to pass in a document that is encrypted with an empty
                // password (such a document appears to not be encrypted by
                // someone viewing the document, thus the confusion).  We will
                // attempt to decrypt with the empty password to handle this case.
                doc.decrypt("");
            } catch (CryptographyException ex) {
                java.util.logging.Logger.getLogger(ChiliadPDFParser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ChiliadPDFParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        processPages(doc);
        endDocument(doc);
    }

    /**
     * The page numbering starts by 1.
     *
     * @param startPage
     */
    @Override
    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    @Override
    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public void setOnePage(int aPageNumber) {
        this.startPage = aPageNumber;
        this.endPage = aPageNumber;
    }

    @Override
    public void shutDown() {
        if (doc != null) {
            try {
                doc.close();
            } catch (IOException ex) {
                LOG.error("Exception during parser shut down.", ex);
            }
        }
    }

    private void processPages(PDDocument doc) {
        List<COSObjectable> pages = doc.getDocumentCatalog().getAllPages();
        Iterator<COSObjectable> pageIter = pages.iterator();
        int currentPageNumber = 1;
        while (pageIter.hasNext()) {
            PDPage currentPage = (PDPage) pageIter.next();
            if (isCurrentPageBetweenStartAndEnd(currentPageNumber)) {
                processPage(currentPageNumber, currentPage);
            }
            currentPageNumber++;
        }
    }

    private boolean isCurrentPageBetweenStartAndEnd(int currentPageNumber) {
        return currentPageNumber >= startPage && currentPageNumber <= endPage;
    }

    private void processPage(int currentPageNumber, PDPage page) {
        startPage(page);
        PDRectangle mediaBox = page.findMediaBox();
        MPage pageContent = MPage.newInstance(source.getId(), currentPageNumber, (double) mediaBox.getWidth(), (double) mediaBox.getHeight());

        for (PageExtractor extractor : extractors) {
            pageContent = extractor.extract(page, pageContent);
        }
        writePageContent(pageContent);
        for (PageExtractor extractor : extractors) {
            extractor.reset();
        }
        endPage(page);

    }

    private void startDocument(PDDocument document) {
        output.startDocument(document);
    }

    private void startPage(PDPage page) {
        output.startPage(page);
    }

    private void writePageContent(MPage pageContent) {
        output.processPageContent(pageContent);
    }

    private void endPage(PDPage page) {
        output.endPage(page);
    }

    private void endDocument(PDDocument document) {
        output.endDocument(document);
    }
}
