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
import java.io.IOException;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.util.PDFOperator;
import org.apache.pdfbox.util.operator.OperatorProcessor;

/**
 * Implementation of content stream operator for page drawer.
 *
 * @version $Revision: 1.3 $
 */
public class ClosePath extends OperatorProcessor {

    /**
     * Log instance.
     */
    private static final Log log = LogFactory.getLog(ClosePath.class);

    /**
     * process : h : Close path.
     *
     * @param operator The operator that is being executed.
     * @param arguments List
     *
     * @throws IOException if something went wrong during logging
     */
    @Override
    public void process(PDFOperator operator, List<COSBase> arguments) throws IOException {
        VectorGraphicsExtractor extractor = (VectorGraphicsExtractor) context;
        try {
            extractor.getLinePath().closePath();
        } catch (Throwable t) {
            log.warn(t, t);
        }
    }
}
