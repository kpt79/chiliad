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
package chiliad.parser.pdf.input;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;

public class FileSource implements PDFSource {

    private final File pdfFile;

    public FileSource(File pdfFile) {
        this.pdfFile = pdfFile;
    }

    @Override
    public PDDocument loadDocument() throws IOException {
        return PDDocument.load(pdfFile);
    }

    @Override
    public String getId() {
        return pdfFile.getName();
    }
}
