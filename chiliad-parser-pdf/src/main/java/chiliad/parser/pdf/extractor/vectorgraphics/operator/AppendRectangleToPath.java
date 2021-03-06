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
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.List;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.util.PDFOperator;
import org.apache.pdfbox.util.operator.OperatorProcessor;

/**
 * Implementation of content stream operator for page extractor.
 *
 *
 */
public class AppendRectangleToPath extends OperatorProcessor {

    /**
     * process : re : append rectangle to path.
     *
     * @param operator The operator that is being executed.
     * @param arguments List
     */
    @Override
    public void process(PDFOperator operator, List<COSBase> arguments) {
        VectorGraphicsExtractor extractor = (VectorGraphicsExtractor) context;

        COSNumber x = (COSNumber) arguments.get(0);
        COSNumber y = (COSNumber) arguments.get(1);
        COSNumber w = (COSNumber) arguments.get(2);
        COSNumber h = (COSNumber) arguments.get(3);

        double x1 = x.doubleValue();
        double y1 = y.doubleValue();

        // create a pair of coordinates for the transformation
        double x2 = w.doubleValue() + x1;
        double y2 = h.doubleValue() + y1;

        Point2D p0 = extractor.transformedPoint(x1, y1);
        Point2D p1 = extractor.transformedPoint(x2, y1);
        Point2D p2 = extractor.transformedPoint(x2, y2);
        Point2D p3 = extractor.transformedPoint(x1, y2);

        // to ensure that the path is created in the right direction, we have to create
        // it by combining single lines instead of creating a simple rectangle
        GeneralPath path = extractor.getLinePath();
        path.moveTo((float) p0.getX(), (float) p0.getY());
        path.lineTo((float) p1.getX(), (float) p1.getY());
        path.lineTo((float) p2.getX(), (float) p2.getY());
        path.lineTo((float) p3.getX(), (float) p3.getY());

        // close the subpath instead of adding the last line so that a possible set line
        // cap style isn't taken into account at the "beginning" of the rectangle
        path.closePath();
    }
}
