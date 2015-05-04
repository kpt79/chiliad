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
package chiliad.parser.pdf;

import chiliad.parser.pdf.cli.ParserCli;
import chiliad.parser.pdf.cli.ParserCliException;
import chiliad.parser.pdf.input.FileSource;
import chiliad.parser.pdf.input.PDFSource;
import chiliad.parser.pdf.output.ParserOutputWriter;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    final static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ParserCli commandLineParser = new ParserCli();
        try {
            commandLineParser.parse(args);
            if (commandLineParser.hasOptionHelp()) {
                commandLineParser.showHelpMessage();
            } else {
                ChiliadPDFParser giskard = newInstance(commandLineParser);
                giskard.parse();
                giskard.shutDown();
            }
        } catch (ParserCliException ex) {
            commandLineParser.showHelpMessage();
            LOG.error("Faild at start.", ex);
        }
    }

    private static ChiliadPDFParser newInstance(ParserCli parserCli) {
        File pdfFile = parserCli.getPDFFile();
        Integer startPage = parserCli.getStartPage();
        Integer endPage = parserCli.getEndPage();
        ParserOutputWriter outputWriter = parserCli.getOutputFormat();
        PDFSource source = new FileSource(pdfFile);
        ChiliadPDFParser giskard = new ChiliadPDFParser(source, outputWriter, parserCli.getExtractors());
        giskard.setStartPage(startPage);
        giskard.setEndPage(endPage);
        return giskard;
    }
}
