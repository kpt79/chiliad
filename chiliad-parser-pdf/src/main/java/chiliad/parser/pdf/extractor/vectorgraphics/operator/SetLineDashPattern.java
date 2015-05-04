/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Above you can read the original licence.
 * The original class from pdfbox project was repackage and modified.
 *
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
package chiliad.parser.pdf.extractor.vectorgraphics.operator;

import chiliad.parser.pdf.extractor.vectorgraphics.VectorGraphicsExtractor;
import java.awt.BasicStroke;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.PDLineDashPattern;
import org.apache.pdfbox.util.PDFOperator;

/**
 * Implementation of content stream operator for page extractor.
 */
public class SetLineDashPattern extends org.apache.pdfbox.util.operator.SetLineDashPattern {

    /**
     * Set the line dash pattern.
     *
     * @param operator The operator that is being executed.
     * @param arguments List
     *
     * @throws IOException If an error occurs while processing the font.
     */
    @Override
    public void process(PDFOperator operator, List<COSBase> arguments) throws IOException {
        super.process(operator, arguments);
        PDLineDashPattern lineDashPattern = context.getGraphicsState().getLineDashPattern();
        VectorGraphicsExtractor extractor = (VectorGraphicsExtractor) context;
        BasicStroke stroke = (BasicStroke) extractor.getStroke();
        if (stroke == null) {
            if (lineDashPattern.isDashPatternEmpty()) {
                extractor.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f));
            } else {
                extractor.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f,
                        lineDashPattern.getCOSDashPattern().toFloatArray(), lineDashPattern.getPhaseStart()));
            }
        } else {
            if (lineDashPattern.isDashPatternEmpty()) {
                extractor.setStroke(new BasicStroke(stroke.getLineWidth(), stroke.getEndCap(),
                        stroke.getLineJoin(), stroke.getMiterLimit()));
            } else {
                extractor.setStroke(new BasicStroke(stroke.getLineWidth(), stroke.getEndCap(), stroke.getLineJoin(),
                        stroke.getMiterLimit(), lineDashPattern.getCOSDashPattern().toFloatArray(),
                        lineDashPattern.getPhaseStart()));
            }
        }
    }

}
