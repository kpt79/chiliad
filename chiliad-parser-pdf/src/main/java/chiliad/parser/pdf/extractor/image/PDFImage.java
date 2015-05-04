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
package chiliad.parser.pdf.extractor.image;

import java.awt.geom.AffineTransform;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.Matrix;

public class PDFImage {

    private final String imageName;
    private final PDXObjectImage image;
    private final Matrix ctmNew;
    private final double pageHeight;
    //
    private final Double x;
    private final Double y;
    private final Double width;
    private final Double height;

    public PDFImage(String imageName, PDXObjectImage image, Matrix ctmNew, double pageHeight) {
        this.imageName = imageName;
        this.image = image;
        this.ctmNew = (Matrix) ctmNew.clone();
        this.pageHeight = pageHeight;

        Matrix m = doCalc();
        this.x = (double) m.getXPosition();
        this.y = (double) m.getYPosition();
        float imageXScale = m.getXScale();
        float imageYScale = m.getYScale();
        this.width = (double) imageXScale;
        this.height = (double) imageYScale;
    }

    private Matrix doCalc() {
        int imageWidth = getImage().getWidth();
        int imageHeight = getImage().getHeight();
        float yScaling = ctmNew.getYScale();
        float angle = (float) Math.acos(ctmNew.getValue(0, 0) / ctmNew.getXScale());
        if (ctmNew.getValue(0, 1) < 0 && ctmNew.getValue(1, 0) > 0) {
            angle = (-1) * angle;
        }
        ctmNew.setValue(2, 1, (float) (pageHeight - ctmNew.getYPosition() - Math.cos(angle) * yScaling));
        ctmNew.setValue(2, 0, (float) (ctmNew.getXPosition() - Math.sin(angle) * yScaling));
        // because of the moved 0,0-reference, we have to shear in the opposite direction
        ctmNew.setValue(0, 1, (-1) * ctmNew.getValue(0, 1));
        ctmNew.setValue(1, 0, (-1) * ctmNew.getValue(1, 0));
        AffineTransform ctmAT = ctmNew.createAffineTransform();
        ctmAT.scale(1f / imageWidth, 1f / imageHeight);
        return ctmNew;
    }

    @Override
    public String toString() {
        return "PDFImage{" + "imageName=" + getImageName() + ", positionX=" + getX() + ", positionY=" + getY() + ", imageWithInPx=" + getWidth() + ", imageHeightInPx=" + getHeight() + '}';
    }

    /**
     * @return the imageName
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * @return the image
     */
    public PDXObjectImage getImage() {
        return image;
    }

    /**
     * @return Position X in page unit.
     */
    public Double getX() {
        return x;
    }

    /**
     * @return Position Y in page unit.
     */
    public Double getY() {
        return y;
    }

    /**
     * @return the width
     */
    public Double getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public Double getHeight() {
        return height;
    }

}
