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
import java.util.LinkedList;
import java.util.stream.Collectors;
import org.apache.pdfbox.util.TextPosition;

class Token {

    private final double gapTolerance = 1.4;

    private final LinkedList<TextPositionWrapper> textPositionWrappers = new LinkedList<>();
    String text = "";

    Token(TextPositionWrapper wrapper) {
        textPositionWrappers.add(wrapper);
        text = wrapper.textPosition.getCharacter();
    }

    private void add(TextPositionWrapper tpw) {
        textPositionWrappers.add(tpw);
        text = buildText();
    }

    String buildText() {
        return textPositionWrappers.stream().map(tpw -> tpw.textPosition.getCharacter()).collect(Collectors.joining());
    }

    int numberOfCharacters() {
        return textPositionWrappers.stream().collect(Collectors.summingInt(tpw -> tpw.textPosition.getIndividualWidths().length));
    }

    double averageTextPositionWidth() {
        return textPositionWrappers.stream().mapToDouble(tpw -> tpw.textPosition.getWidth())
                .average()
                .getAsDouble();
    }

    double maxTextPositionWidth() {
        return textPositionWrappers.stream().mapToDouble(tpw -> tpw.textPosition.getWidth())
                .max()
                .getAsDouble();
    }

    double distanceFromTokenLastElement(TextPositionWrapper tpw) {
        TextPositionWrapper last = textPositionWrappers.getLast();
        return Math.abs(last.getTextPositionX() + last.textPosition.getWidth() - tpw.getTextPositionX());
    }

    boolean accept(TextPositionWrapper tpw) {
        TextPositionWrapper last = textPositionWrappers.getLast();
        if (distanceFromTokenLastElement(tpw) < maxTextPositionWidth() * gapTolerance
                && last.hasTheSameFontType(tpw) && last.hasTheSameFontSize(tpw)) {
            add(tpw);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Token{" + text + '}';
    }

    public String getFontFamily() {
        if (textPositionWrappers.getFirst().textPosition.getFont().getFontDescriptor() == null) {
            return "";
        }
        return textPositionWrappers.getFirst().textPosition.getFont().getFontDescriptor().getFontFamily();
    }

    public String getFontName() {
        if (textPositionWrappers.getFirst().textPosition.getFont().getFontDescriptor() == null) {
            return "";
        }
        return textPositionWrappers.getFirst().textPosition.getFont().getFontDescriptor().getFontName();
    }

    public Double getFontSizeInPt() {
        return (double) textPositionWrappers.getFirst().textPosition.getFontSizeInPt();
    }

    public Double getFontWeight() {
        if (textPositionWrappers.getFirst().textPosition.getFont().getFontDescriptor() == null) {
            return 300D;
        }
        return (double) textPositionWrappers.getFirst().textPosition.getFont().getFontDescriptor().getFontWeight();
    }

    //TODO kpt might be nicer distinguish for every char
    public Color getStrokingColor() {
        return textPositionWrappers.getFirst().getStrokingColor();
    }

    public Color getNonStrokingColor() {
        return textPositionWrappers.getFirst().getNonStrokingColor();
    }

    public Double getPositionStartX() {
        return (double) textPositionWrappers.getFirst().getTextPositionX();
    }

    public Double getPositionStartY() {
        return (double) textPositionWrappers.getFirst().getTextPositionY();
    }

    public Double getPositionEndX() {
        TextPosition tp = textPositionWrappers.getFirst().textPosition;
        return (double) (tp.getX() + tp.getWidth());
    }

    public Double getPositionEndY() {
        TextPosition tp = textPositionWrappers.getFirst().textPosition;
        return (double) (tp.getY() + tp.getHeight());
    }

    public Double getWidth() {
        return textPositionWrappers.stream().mapToDouble(tpw -> tpw.textPosition.getWidth()).sum();
    }

    public Double getHeight() {
        return textPositionWrappers.stream().mapToDouble(tpw -> tpw.textPosition.getHeight()).max().getAsDouble();
    }

    public String getText() {
        return text;
    }
}
