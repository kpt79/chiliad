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
package chiliad.parser.pdf.extractor.vectorgraphics;

import chiliad.parser.pdf.model.MPage;
import chiliad.parser.pdf.model.MVectorGraphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.svg.SVGDocument;

public final class SVGBasedExtractor extends VectorGraphicsExtractor {

    final static Logger LOG = LoggerFactory.getLogger(SVGBasedExtractor.class);

    private final boolean useCSS = true; // we want to use CSS style attributes
    private final String qualifiedName = "svg";

    public SVGBasedExtractor() throws IOException {
        DOMImplementation domImpl
                = SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        SVGDocument doc = (SVGDocument) domImpl.createDocument(svgNS, qualifiedName, null);
        SVGGeneratorContext ctx
                = SVGGeneratorContext.createDefault(doc);
        setGraphics(new SVGGraphics2D(ctx, useCSS));
    }

    @Override
    public MPage handleResult(Graphics2D g, MPage pageContent) throws SVGGraphics2DIOException {
        SVGGraphics2D graphics = (SVGGraphics2D) g;
        StringWriter writer = new StringWriter();
        graphics.stream(writer);
        String svg = writer.getBuffer().toString();
        MVectorGraphics vectorGraphics = new MVectorGraphics();
        vectorGraphics.setSvgContent(svg);
        pageContent.setVectorGraphics(vectorGraphics);
        return pageContent;
    }

    @Override
    public void reset() {
        super.reset();
    }

//            PageContent.VectorGraphics vg = new PageContent.VectorGraphics();
//            vg.setSvg(svg);
//            SVGDocument svgDoc = buildDocFromSVGString(svg);
//            List<Rectangle> aois = findAOIs(svgDoc);
//            IntStream.range(0, aois.size()).mapToObj(i -> {
//                Rectangle aoi = aois.get(i);
//                return buildRasterizedGraphics(i, aoi, rasterizeAOI(svgDoc, aoi));
//            }).forEach(rg -> vg.addRasterizedGraphics(rg));
//            pageContent.setVectorGraphics(vg);
//    private SVGDocument buildDocFromSVGString(String svg) throws IOException {
//        StringReader reader = new StringReader(svg);
//        String parser = XMLResourceDescriptor.getXMLParserClassName();
//        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
//        // do not forget the URI, even it is fake eg: c://svg/sample.svg
//        // this is specially important if your doc reference other doc 
//        // in relative URI
//        SVGDocument svgDoc = f.createSVGDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, reader);
//        UserAgent userAgent = new UserAgentAdapter();
//        DocumentLoader loader = new DocumentLoader(userAgent);
//        BridgeContext ctx = new BridgeContext(userAgent, loader);
//        ctx.setDynamicState(BridgeContext.DYNAMIC);
//        GVTBuilder builder = new GVTBuilder();
//
//        builder.build(ctx, svgDoc);
//        return svgDoc;
//    }
//
//    protected List<Rectangle> findAOIs(SVGDocument svgDoc) {
//        List<Rectangle> rects = new ArrayList<>();
//        NodeList groups = svgDoc.getElementsByTagName("g");
//        for (int i = 0; i < groups.getLength(); i++) {
//            SVGRect rect = ((SVGLocatable) groups.item(i)).getBBox();
//            if (rect != null) {
//                rects.addTokens(new Rectangle((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight()));
//            }
//        }
//        return rects;
//    }
//
//    private byte[] rasterizeAOI(SVGDocument doc, Rectangle aoi) {
//        LOG.debug("AOI " + ToString.byReflexion(aoi));
//        if (onlyDimension) {
//            return new byte[]{};
//        } else {
//            try {
//                PNGTranscoder trans = new PNGTranscoder();
//                // Set hints to indicate the dimensions of the output image
//                // and the input area of interest.
//                trans.addTranscodingHint(PNGTranscoder.KEY_WIDTH,
//                        new Float(aoi.width));
//                trans.addTranscodingHint(PNGTranscoder.KEY_HEIGHT,
//                        new Float(aoi.height));
//                trans.addTranscodingHint(PNGTranscoder.KEY_AOI, aoi);
//                TranscoderInput input = new TranscoderInput(doc);
//                ByteArrayOutputStream ostream = new ByteArrayOutputStream();
//                TranscoderOutput output = new TranscoderOutput(ostream);
//                trans.transcode(input, output);
//                ostream.flush();
//                return ostream.toByteArray();
//            } catch (TranscoderException | IOException ex) {
//                LOG.error("Exception during transcoding.", ex);
//            }
//        }
//        return new byte[]{};
//    }
//
//    private MRasterizedGraphics buildRasterizedGraphics(int i, Rectangle aoi, byte[] bytes) {
//        MRasterizedGraphics rasterized = new MRasterizedGraphics();
//        MImage image = new MImage();
//        image.setImageName("rasterizedGraphics_" + i);
//        image.setImageBytes(bytes);
//        image.setHeight((double) aoi.height);
//        image.setWidth((double) aoi.width);
//        image.setX((double) aoi.x);
//        image.setY((double) aoi.y);
//        rasterized.setImage(image);
//        return rasterized;
//    }
}
