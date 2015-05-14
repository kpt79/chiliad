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
import chiliad.parser.pdf.model.MVectorGraphics;
import java.awt.Color;
import java.io.IOException;
import java.io.Writer;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.slf4j.LoggerFactory;

//TODO generate html by stax
public class HTMLOutput implements ParserOutputWriter {

    final static org.slf4j.Logger LOG = LoggerFactory.getLogger(HTMLOutput.class);

    private final Writer writer;

    /**
     * Ratio between screen unit and PDF unit
     */
    private final double ratioBetweenScreenAndPDFResolution = 96d / 72d;

    public HTMLOutput(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void startDocument(PDDocument document) {
        try {
            writer.append("<!DOCTYPE html>").append("\n");
            writer.append("<html lang='en' xmlns='http://www.w3.org/1999/xhtml' xmlns:p='http://gikard.org/parser/'>");
            writer.append("<head>");
            writer.append("<meta charset='utf-8'/>");
            writer.append("<style>\n");

            //minimal reset
            writer.append("html, body, body div, span {\n");
            writer.append("margin: 0;\n");
            writer.append("padding: 0;\n");
            writer.append("border: 0;\n");
            writer.append("font-size: 100%;\n");
            writer.append("font-weight: normal;\n");
            writer.append("vertical-align: baseline;\n");
            writer.append("background: transparent;\n");
            writer.append("}\n");

            writer.append("span.token {");
            writer.append("position: absolute;");
            //writer.append("border: 1px solid grey;");
            writer.append("white-space: nowrap;");
            writer.append("font-family: Helvetica;");
            writer.append("}\n");
            writer.append("div.image {");
            writer.append("position: absolute;");
            writer.append("border: 1px solid lightgreen;");
            writer.append("}\n");
            writer.append("div.rasterizedGraphics {");
            writer.append("position: absolute;");
            writer.append("border: 1px solid blue;");
            writer.append("}\n");
            writer.append("</style>");
            writer.append("</head>");
        } catch (IOException ex) {
            throw new ParserOutputException("Failed to write startDocument.", ex);
        }
    }

    @Override
    public void startPage(PDPage page) {
        try {
            writer.append("<body>");
        } catch (IOException ex) {
            throw new ParserOutputException("Failed to write startPage.", ex);
        }
    }

    @Override
    public void processPageContent(MPage p) {
        try {
            writer.append("<p:pageContent pageNumber='").append(p.getPageNumber().toString()).append("' width='").append(p.getWidth().toString()).append("' height='").append(p.getHeight().toString()).append("'>").append("\n");

            writer.append("<p:images>").append("\n");
            p.getImages().forEach(i -> {
                try {
                    writer.append("\t").append(imageToHTML(i)).append("\n");
                } catch (IOException ex) {
                    throw new ParserOutputException("Failed to write pageContent.", ex);
                }
            });
            writer.append("</p:images>").append("\n");

            writer.append("<p:vectorGraphics>").append("\n");
            MVectorGraphics vg = p.getVectorGraphics();
            if (vg != null) {
                writer.append("\t").append(vg.getSvgContent()).append("\n");
                writer.append("</p:vectorGraphics>").append("\n");
            }
            writer.append("<p:tokens>").append("\n");
            p.getTokens().forEach(t -> {
                try {
                    writer.append("\t").append(tokenToHTML(t)).append("\n");
                } catch (IOException ex) {
                    throw new ParserOutputException("Failed to write pageContent.", ex);
                }
            });
            writer.append("</p:tokens>").append("\n");

            writer.append("</p:pageContent>").append("\n");
        } catch (IOException ex) {
            throw new ParserOutputException("Failed to write pageContent.", ex);
        }
    }

    @Override
    public void endPage(PDPage page) {
        try {
            writer.append("</body>");
        } catch (IOException ex) {
            throw new ParserOutputException("Failed to write endPage.", ex);
        }
    }

    @Override
    public void endDocument(PDDocument document) {
        try {
            writer.append("</html>");
            writer.flush();
        } catch (IOException ex) {
            throw new ParserOutputException("Failed to write endDocument.", ex);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    private String tokenToHTML(MToken t) {
        StringBuilder fragment = new StringBuilder();
        fragment.append("<p:token x='$x' y='$y' width='$w' height='$h'>");
        fragment.append("<span class='token' title='${title}' style='top: $toppx; left: $leftpx;  font-weight: $font-weight; font-size: $font-sizept; color: $color;' >");
        //width: $widthpx; height: $heightpx;
        fragment.append(t.getText());
        fragment.append("</span>");
        fragment.append("</p:token>");
        String s = fragment.toString();
        s = s.replace("${title}", t.toString());
        //replace span
        s = s.replace("$top", scale(t.getY() - t.getHeight()));
        s = s.replace("$left", scale(t.getX()));
        //s = s.replace("$width", ratioBetweenScreenAndPDFResolution(t.getWidth()));
        //s = s.replace("$height", ratioBetweenScreenAndPDFResolution(t.getHeight()));
        s = s.replace("$font-weight", String.valueOf(t.getFontWeight().intValue()));
        s = s.replace("$font-size", String.valueOf(t.getFontSizeInPt()));
        //replace token
        s = s.replace("$x", String.valueOf(t.getX()));
        s = s.replace("$y", String.valueOf(t.getY()));
        s = s.replace("$w", String.valueOf(t.getWidth()));
        s = s.replace("$h", String.valueOf(t.getHeight()));
        s = s.replace("$color", t.getNonStrokingColor());
        return s;
    }

    private String imageToHTML(MImage i) {
        StringBuilder fragment = new StringBuilder();
        fragment.append("<p:image x='$x' y='$y' width='$w' height='$h' imageName='$imageName'>");
        fragment.append("<div class='image' style='top: $toppx; left: $leftpx; width: $widthpx; height: $heightpx'> ");
        fragment.append(i.getImageName());
        fragment.append("</div>");
        fragment.append("</p:image>");
        String s = fragment.toString();
        //replace div image
        s = s.replace("$top", scale(i.getY()));
        s = s.replace("$left", scale(i.getX()));
        s = s.replace("$width", scale((double) i.getWidth()));
        s = s.replace("$height", scale((double) i.getHeight()));
        //replace token
        s = s.replace("$x", String.valueOf(i.getX()));
        s = s.replace("$y", String.valueOf(i.getY()));
        s = s.replace("$w", String.valueOf(i.getWidth()));
        s = s.replace("$h", String.valueOf(i.getHeight()));
        s = s.replace("$imageName", i.getImageName());
        return s;
    }

    private String scale(Double d) {
        return String.valueOf((int) (d * ratioBetweenScreenAndPDFResolution));
    }

}
