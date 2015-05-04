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
package chiliad.parser.pdf.output;

import chiliad.parser.pdf.model.MImage;
import chiliad.parser.pdf.model.MPage;
import chiliad.parser.pdf.model.MToken;
import java.io.IOException;
import java.io.Writer;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.slf4j.LoggerFactory;

public class PlainStringOutput implements ParserOutputWriter {

    final static org.slf4j.Logger LOG = LoggerFactory.getLogger(PlainStringOutput.class);

    private final Writer writer;

    public PlainStringOutput(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void startDocument(PDDocument document) {
        try {
            writer.append("StartDocument\n");
        } catch (IOException ex) {
            throw new ParserOutputException("Failed to write startDocument.", ex);
        }
    }

    @Override
    public void startPage(PDPage page) {
        try {
            writer.append("StartPage\n");
        } catch (IOException ex) {
            throw new ParserOutputException("Failed to write startPage.", ex);
        }
    }

    @Override
    public void processPageContent(MPage pageContent) {
        try {
            writer.append("PageNumber: ").append(pageContent.getPageNumber().toString()).append("\n");
            for (MToken t : pageContent.getTokens()) {
                writer.append(t.toString()).append("\n");
            }
            for (MImage img : pageContent.getImages()) {
                writer.append(img.toString()).append("\n");
            }
            writer.append(pageContent.getVectorGraphics().getSvgContent()).append("\n");
        } catch (IOException ex) {
            throw new ParserOutputException("Failed to write page content.", ex);
        }
    }

    @Override
    public void endPage(PDPage page) {
        try {
            writer.append("EndPage");
        } catch (IOException ex) {
            throw new ParserOutputException("Failed to write endPage.", ex);
        }
    }

    @Override
    public void endDocument(PDDocument document) {
        try {
            writer.append("EndDocument");
            writer.close();
        } catch (IOException ex) {
            throw new ParserOutputException("Failed to write endDocument.", ex);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

}
