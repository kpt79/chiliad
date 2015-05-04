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
package chiliad.parser.pdf.extractor.image.operator;

import chiliad.parser.pdf.extractor.image.ImageExtractor;
import chiliad.parser.pdf.extractor.image.PDFImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDGraphicsState;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectForm;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.PDFOperator;
import org.apache.pdfbox.util.operator.OperatorProcessor;

/**
 * Processing operator: 'Do'
 */
public class ShowImage extends OperatorProcessor {

    @Override
    public void process(PDFOperator operator, List<COSBase> arguments) throws IOException {
        final ImageExtractor extractor = (ImageExtractor) context;
        final COSName objectName = (COSName) arguments.get(0);
        final Map<String, PDXObject> xobjects = context.getResources().getXObjects();
        final PDXObject xobject = (PDXObject) xobjects.get(objectName.getName());
        if (xobject instanceof PDXObjectImage) {
            System.out.println("ImageName=" + ((COSName) arguments.get(0)).getName());
            PDXObjectImage image = (PDXObjectImage) xobject;
            Matrix ctmNew = extractor.getGraphicsState().getCurrentTransformationMatrix();
            PDPage page = extractor.getCurrentPage();
            double pageHeight = page.getMediaBox().getHeight();
            extractor.addPDFImage(new PDFImage(((COSName) arguments.get(0)).getName(), image, ctmNew, pageHeight));
        } else if (xobject instanceof PDXObjectForm) {
            // save the graphics state
            extractor.getGraphicsStack().push((PDGraphicsState) extractor.getGraphicsState().clone());
            PDPage page = extractor.getCurrentPage();

            PDXObjectForm form = (PDXObjectForm) xobject;
            COSStream invoke = (COSStream) form.getCOSObject();
            PDResources pdResources = form.getResources();
            if (pdResources == null) {
                pdResources = page.findResources();
            }
            // if there is an optional form matrix, we have to
            // map the form space to the user space
            Matrix matrix = form.getMatrix();
            if (matrix != null) {
                Matrix xobjectCTM = matrix.multiply(extractor.getGraphicsState().getCurrentTransformationMatrix());
                extractor.getGraphicsState().setCurrentTransformationMatrix(xobjectCTM);
            }
            extractor.processSubStream(page, pdResources, invoke);

            // restore the graphics state
            extractor.setGraphicsState((PDGraphicsState) extractor.getGraphicsStack().pop());
        }
    }

}
