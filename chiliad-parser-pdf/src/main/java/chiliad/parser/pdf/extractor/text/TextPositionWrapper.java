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

import java.awt.Color;
import org.apache.pdfbox.util.TextPosition;

final class TextPositionWrapper {

    TextPosition textPosition;
    Color strokingColor;
    Color nonStrokingColor;

    public TextPosition getTextPosition() {
        return textPosition;
    }

    public void setTextPosition(TextPosition textPosition) {
        this.textPosition = textPosition;
    }

    public Float getTextPositionY() {
        return textPosition.getY();
    }

    public Float getTextPositionX() {
        return textPosition.getX();
    }

    public float getWidthOfSpaceBetweenTextPosition(TextPositionWrapper other) {
        return Math.abs(this.textPosition.getX() + this.textPosition.getWidth() - other.getTextPositionX());
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

    public boolean hasTheSameFontType(TextPositionWrapper other) {
        return this.textPosition.getFont().equals(other.textPosition.getFont());
    }

    public boolean hasTheSameFontSize(TextPositionWrapper other) {
        return this.textPosition.getFontSize() == other.textPosition.getFontSize();
    }
}
