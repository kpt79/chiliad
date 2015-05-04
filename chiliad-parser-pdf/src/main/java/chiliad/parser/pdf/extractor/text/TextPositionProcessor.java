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

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.pdfbox.util.TextPosition;

public class TextPositionProcessor {

    private boolean printStat = false;

    private LinkedList<TextPositionWrapper> textPositionWrappers = new LinkedList<>();

    void add(TextPositionWrapper tpw) {
        textPositionWrappers.add(tpw);
    }

    Map<Float, List<TextPositionWrapper>> groupByPositionY() {
        return new TreeMap<>(textPositionWrappers.stream().sorted(textPositionComparator).collect(Collectors.groupingBy(TextPositionWrapper::getTextPositionY)));
    }

    LinkedList<Token> buildTokens() {
        LinkedList<Token> tokens = new LinkedList<>();

        groupByPositionY().values().stream().map((wrappers) -> {
            Token t = new Token(wrappers.get(0));
            for (int i = 1; i < wrappers.size(); i++) {
                final TextPositionWrapper currentPosition = wrappers.get(i);
                if (!t.accept(currentPosition)) {
                    tokens.add(t);
                    t = new Token(currentPosition);
                }
            }
            return t;
        }).forEach(t -> tokens.add(t));
        return tokens;
    }

    private String test(List<TextPositionWrapper> tpws) {
        return tpws.stream().sequential().map(tpw -> tpw.textPosition.getCharacter()).collect(Collectors.joining());
    }

    private final Comparator<TextPositionWrapper> textPositionComparator = (TextPositionWrapper left, TextPositionWrapper right) -> {
        return ComparisonChain.start().compare(left.textPosition.getY(), right.textPosition.getY()).compare(left.textPosition.getX(), right.textPosition.getX()).result();
    };

    public List<Token> process() {
        List<Token> tokens = buildTokens();

        if (printStat) {
            groupByPositionY().values().stream().sequential()
                    .peek(e -> System.out.println(e.size() + " : " + test(e))).count();

            for (List<TextPositionWrapper> wrappers : groupByPositionY().values()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i <= wrappers.size() - 1; i++) {
                    int j = i - 1;
                    Float dist = wrappers.get(i).getWidthOfSpaceBetweenTextPosition(wrappers.get(j));
                    float widthOfSpace = wrappers.get(i).textPosition.getWidthOfSpace();
                    sb.append(wrappers.get(j).textPosition.getCharacter()).append("(" + widthOfSpace + ")").append(" [").append(dist).append("] ");
                }
                sb.append(wrappers.get(wrappers.size() - 1).textPosition.getCharacter());
            }
        }
        return tokens;
    }

    private void print(Map<Float, List<TextPositionWrapper>> group) {
        for (Map.Entry<Float, List<TextPositionWrapper>> entry : group.entrySet()) {
            System.out.println("Y=" + entry.getKey());
            for (TextPositionWrapper tpw : entry.getValue()) {
                System.out.println(textPositionAsString(tpw.textPosition));
            }
        }
    }

    private String textPositionAsString(TextPosition tp) {
        return MoreObjects.toStringHelper(tp).add("char", tp.getCharacter()).add("x", tp.getX()).add("y", tp.getY()).add("widthOfSpace", tp.getWidthOfSpace()).toString();
    }
}
