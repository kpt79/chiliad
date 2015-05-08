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
package chiliad.parser.pdf.output;

import chiliad.parser.pdf.model.MPage;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.io.Writer;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.slf4j.LoggerFactory;

public class JSONOutput implements ParserOutputWriter {

    final static org.slf4j.Logger LOG = LoggerFactory.getLogger(JSONOutput.class);

    private JsonGenerator gen;

    public JSONOutput(Writer writer) {
        JsonFactory jfactory = new JsonFactory();
        try {
            gen = jfactory.createGenerator(writer);
        } catch (IOException ex) {
            throw new ParserOutputException("Failed to writer JSON output.", ex);
        }
    }

    @Override
    public void startDocument(PDDocument document) {
        try {
            //gen.writeStartObject();
            gen.writeStartArray();
        } catch (IOException ex) {
            throw new ParserOutputException("Failed to writer JSON output.", ex);
        }
    }

    @Override
    public void startPage(PDPage page) {
//        try {
//            gen.writeStartObject();//page
//        } catch (IOException ex) {
//            throw new ParserOutputException("Failed to writer JSON output.", ex);
//        }

    }

    @Override
    public void processPageContent(MPage page) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
			mapper.writeValue(gen, page);
		} catch (IOException e) {
			throw new ParserOutputException("Failed to writer JSON output.", e);
		}
//        try {
//            gen.writeNumberField("pageNumber", page.getPageNumber());
//
//            gen.writeArrayFieldStart("tokens");
//            page.getTokens().forEach(t -> {
//                try {
//                    mapper.writeValue(gen, t);
//                } catch (IOException ex) {
//                    throw new ParserOutputException("Failed to write JSON output.", ex);
//                }
//            });
//            gen.writeEndArray();
//
//            gen.writeArrayFieldStart("images");
//            page.getImages().forEach(img -> {
//                try {
//                    mapper.writeValue(gen, img);
//                } catch (IOException ex) {
//                    throw new ParserOutputException("Failed to write JSON output.", ex);
//                }
//            });
//            gen.writeEndArray();
//        } catch (IOException ex) {
//            throw new ParserOutputException("Failed to writer JSON output.", ex);
//        }
    }

    @Override
    public void endPage(PDPage page) {
//        try {
//            gen.writeEndObject();//page
//        } catch (IOException ex) {
//            throw new ParserOutputException("Failed to writer JSON output.", ex);
//        }
    }

    @Override
    public void endDocument(PDDocument document) {
        try {
            gen.writeEndArray();//pages
            //gen.writeEndObject();
            gen.close();
        } catch (IOException ex) {
            throw new ParserOutputException("Failed to writer JSON output.", ex);
        } finally {
            IOUtils.closeQuietly(gen);
        }
    }

}
