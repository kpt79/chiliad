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
import java.awt.Color;
import java.io.Serializable;

public class MToken implements Boundable, Serializable {

    private static final long serialVersionUID = 2665396432824852438L;

    private String text;
    private Color strokingColor;
    private Color nonStrokingColor;
    private Double fontSizeInPt;
    private String fontFamily;
    private String fontName;
    private Double fontWeight;
    /**
     * Start position X on page (page unit) - bottom left
     */
    private Double x;
    /**
     * Start position y on page (page unit) - bottom left
     */
    private Double y;
    /**
     * Width (page unit)
     */
    private Double width;
    /**
     * Height (page unit)
     */
    private Double height;

    public static MToken from(Double x, Double y, Double width, Double height, String text) {
        MToken t = new MToken();
        t.setX(x);
        t.setY(y);
        t.setWidth(width);
        t.setHeight(height);
        t.setText(text);
        return t;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getStrokingColor() {
        return strokingColor;
    }

    public void setStrokingColor(Color strokingColor) {
        this.strokingColor = strokingColor;
    }

    public Color getNonStrokingColor() {
        return nonStrokingColor;
    }

    public void setNonStrokingColor(Color nonStrokingColor) {
        this.nonStrokingColor = nonStrokingColor;
    }

    public Double getFontSizeInPt() {
        return fontSizeInPt;
    }

    public void setFontSizeInPt(Double fontSizeInPt) {
        this.fontSizeInPt = fontSizeInPt;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public Double getFontWeight() {
        return fontWeight;
    }

    public void setFontWeight(Double fontWeight) {
        this.fontWeight = fontWeight;
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

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
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
        return MoreObjects.toStringHelper(this)
                .add("text", text).add("x", x).add("y", y).add("width", width).add("height", height)
                .add("fontName", fontName)
                .add("fontSizeInPt", fontSizeInPt)
                .add("strokingColor", strokingColor)
                .add("nonStrokingColor", nonStrokingColor)
                .toString();
    }

}
