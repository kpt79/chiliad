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
package chiliad.parser.pdf.model;

import com.google.common.base.MoreObjects;
import java.io.Serializable;

public class MImage implements Boundable, Serializable {

    private static final long serialVersionUID = -933916200914979916L;

    private String imageName;
    /**
     * Position X on page (page unit).
     */
    private Double x;
    /**
     * Position Y on page (page unit).
     */
    private Double y;
    /**
     * Width (page unit).
     */
    private Double width;
    /**
     * Height (page unit).
     */
    private Double height;
    /**
     * Image bytes.
     */
    byte[] imageBytes;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    /**
     *
     * @return Width of image (page unit).
     */
    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    /**
     *
     * @return Height of image (page unit).
     */
    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    @Override
    public Double getMinX() {
        return getX();
    }

    @Override
    public Double getMaxX() {
        return getX() + getWidth();
    }

    @Override
    public Double getMinY() {
        return getY();
    }

    @Override
    public Double getMaxY() {
        return getY() + getHeight();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("imageName", imageName).add("x", x).add("y", y).add("width", width).add("height", height).toString();
    }

}
