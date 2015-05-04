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

public class ClasspathSource implements PDFSource {

    private final String path;

    public ClasspathSource(String path) {
        this.path = path;
    }

    @Override
    public PDDocument loadDocument() throws IOException {
        return PDDocument.load(this.getClass().getClassLoader().getResourceAsStream(path));
    }

    @Override
    public String getId() {
        return new File(path).getName();
    }

}
