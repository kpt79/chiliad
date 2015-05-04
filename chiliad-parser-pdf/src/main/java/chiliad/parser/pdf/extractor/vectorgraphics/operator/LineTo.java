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
import java.awt.geom.Point2D;
import java.util.List;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.util.PDFOperator;
import org.apache.pdfbox.util.operator.OperatorProcessor;

/**
 * Implementation of content stream operator for page extractor.
 */
public class LineTo extends OperatorProcessor {

    /**
     * process : l : Append straight line segment to path.
     *
     * @param operator The operator that is being executed.
     * @param arguments List
     */
    @Override
    public void process(PDFOperator operator, List<COSBase> arguments) {
        VectorGraphicsExtractor extractor = (VectorGraphicsExtractor) context;

        //append straight line segment from the current point to the point.
        COSNumber x = (COSNumber) arguments.get(0);
        COSNumber y = (COSNumber) arguments.get(1);

        Point2D pos = extractor.transformedPoint(x.doubleValue(), y.doubleValue());
        extractor.getLinePath().lineTo((float) pos.getX(), (float) pos.getY());
    }
}
