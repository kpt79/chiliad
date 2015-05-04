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
package chiliad.parser.pdf.extractor.text;

import chiliad.parser.pdf.extractor.ExtractorException;
import chiliad.parser.pdf.extractor.PageExtractor;
import chiliad.parser.pdf.model.MPage;
import chiliad.parser.pdf.model.MToken;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFStreamEngine;
import org.apache.pdfbox.util.ResourceLoader;
import org.apache.pdfbox.util.TextPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextExtractor extends PDFStreamEngine implements PageExtractor {

    final static Logger LOG = LoggerFactory.getLogger(TextExtractor.class);

    private TextPositionProcessor textPositionProcessor;
    private List<Token> tokens;

    public TextExtractor() throws IOException {
        super(ResourceLoader.loadProperties(
                "TextExtractor.properties", true));
    }

    @Override
    public MPage extract(PDPage pageToExtract, MPage pageContent) {
        try {
            if (pageToExtract.getContents() == null) {
                throw new IllegalStateException("Empty page content.");
            }
            textPositionProcessor = new TextPositionProcessor();
            processStream(pageToExtract, pageToExtract.findResources(), pageToExtract.getContents().getStream());
            tokens = textPositionProcessor.process();
            pageContent.addTokens(getMTokens());
            return pageContent;
        } catch (IOException ex) {
            throw new ExtractorException("Failed to extract the tokens.", ex);
        }

    }

    @Override
    protected void processTextPosition(TextPosition tp) {
        try {
            if (tp.getWidth() == 0) {
                //TODO KPT - handle rotated text
                //Probably rotated text, do nothing right now.
                LOG.info("Probably rotated text (width is zero) {}", tp);
                return;
            }
            TextPositionWrapper tpw = new TextPositionWrapper();
            tpw.setTextPosition(tp);
            tpw.setStrokingColor(getGraphicsState().getStrokingColor().getJavaColor());
            tpw.setNonStrokingColor(getGraphicsState().getNonStrokingColor().getJavaColor());
            textPositionProcessor.add(tpw);
        } catch (IOException ex) {
            throw new ExtractorException("Failed while processing text position.", ex);
        }

    }

    @Override
    public void reset() {
        tokens.clear();
        super.resetEngine();
    }

    private List<MToken> getMTokens() {
        return ImmutableList.copyOf(tokens.stream().map(t -> toMToken(t)).collect(Collectors.toList()));
    }

    private MToken toMToken(Token token) {
        MToken mToken = new MToken();
        mToken.setText(StringUtils.normalizeSpace(StringUtils.trimToEmpty(token.getText())));
        mToken.setFontFamily(token.getFontFamily());
        mToken.setFontName(token.getFontName());
        mToken.setFontSizeInPt(token.getFontSizeInPt());
        mToken.setFontWeight(token.getFontWeight());
        mToken.setNonStrokingColor(token.getNonStrokingColor());
        mToken.setStrokingColor(token.getStrokingColor());
        mToken.setX(token.getPositionStartX());
        mToken.setY(token.getPositionStartY());
        mToken.setWidth(token.getWidth());
        mToken.setHeight(token.getHeight());
        return mToken;
    }
}
