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
package chiliad.parser.pdf.cli;

import chiliad.parser.pdf.extractor.PageExtractor;
import chiliad.parser.pdf.extractor.image.ImageExtractor;
import chiliad.parser.pdf.extractor.text.TextExtractor;
import chiliad.parser.pdf.extractor.vectorgraphics.SVGBasedExtractor;
import chiliad.parser.pdf.output.HTMLOutput;
import chiliad.parser.pdf.output.JSONOutput;
import chiliad.parser.pdf.output.ParserOutputException;
import chiliad.parser.pdf.output.ParserOutputWriter;
import chiliad.parser.pdf.output.PlainStringOutput;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.collections4.comparators.FixedOrderComparator;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

public class ParserCli {

    final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ParserCli.class);

    private static final String DEFAULT_OUTUP_DIR = System.getProperty("user.dir");

    private final String optionPrefix = "-";

    private final String optionValueSeparator = ",";

    private final String JSON_OUTPUT = "json";

    private final String HTML_OUTPUT = "html";

    private final String PLAIN_OUTPUT = "plain";

    private final String TEXT_EXTRACTOR = "text";

    private final String IMAGE_EXTRACTOR = "image";

    private final String VECTOR_GRAPHICS_EXTRACTOR = "vectorgraphics";

    private final CommandLineParser parser;

    private final GiskardOptions options;

    private CommandLine commandLine;

    protected String helpMessage = "Example usage: extract hello.pdf from page 3 to 5 to a tmp directory in JSON format:\n"
            + " -pdfFile hello.pdf -startPage 3 -endPage 5 -outputDir tmp -outputFormat JSON ";

    private final Option help = new Option("h", false, "Print this help message.");
    private final Option pdfFile = new Option("pdfFile", true, "PDF file to parse.");
    private final Option outputDir = new Option("outputDir", true, "Output directory.");
    private final Option startPage = new Option("startPage", true, "First page for parsing.");
    private final Option endPage = new Option("endPage", true, "Last page for parsing.");
    private final Option outputFormat = new Option("outputFormat", true, "Ouput format of the parsed content (plain, html, json).");
    private final Option extractors = new Option("extractors", true, "Extracted information (" + TEXT_EXTRACTOR + ", " + IMAGE_EXTRACTOR + ", " + VECTOR_GRAPHICS_EXTRACTOR + ").");

    public ParserCli() {
        parser = new PosixParser();
        options = createOptions();
    }

    public void parse(String[] args) {
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException ex) {
            throw new ParserCliException(ex.getMessage(), ex);
        }
    }

    public boolean hasOptionHelp() {
        return commandLine.hasOption(help.getOpt());
    }

    public void showHelpMessage() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setOptionComparator(new FixedOrderComparator(options.getOptions().toArray()));
        helpFormatter.setOptPrefix(optionPrefix);
        helpFormatter.printHelp(helpMessage, options);
    }

    public File getPDFFile() {
        return new File(commandLine.getOptionValue(pdfFile.getOpt(), JSON_OUTPUT));
    }

    public File getOutputDir() {
        return new File(commandLine.getOptionValue(outputDir.getOpt(), DEFAULT_OUTUP_DIR));
    }

    public Integer getStartPage() {
        return Integer.parseInt(commandLine.getOptionValue(startPage.getOpt(), "1"));
    }

    public Integer getEndPage() {
        return Integer.parseInt(commandLine.getOptionValue(endPage.getOpt(), getStartPage().toString()));
    }

    public ParserOutputWriter getOutputFormat() {
        try {
            String format = StringUtils.lowerCase(commandLine.getOptionValue(outputFormat.getOpt(), JSON_OUTPUT));
            switch (format) {
                case JSON_OUTPUT:
                    return new JSONOutput(new FileWriter(outputFile(format)));
                case HTML_OUTPUT:
                    return new HTMLOutput(new FileWriter(outputFile(format)));
                default:
                    return new PlainStringOutput(new FileWriter(outputFile(format)));
            }
        } catch (IOException ex) {
            throw new ParserOutputException("Failed to create parser output.", ex);
        }
    }

    public PageExtractor[] getExtractors() {
        String exts = StringUtils.lowerCase(commandLine.getOptionValue(extractors.getOpt(), TEXT_EXTRACTOR));
        if (exts.contains(optionValueSeparator)) {
            String[] extNames = StringUtils.split(exts, optionValueSeparator);
            PageExtractor[] pageExtractors = new PageExtractor[extNames.length];
            for (int i = 0; i < extNames.length; i++) {
                pageExtractors[i] = createExtractor(extNames[i]);
            }
            return pageExtractors;
        }
        return new PageExtractor[]{createExtractor(exts)};
    }

    private PageExtractor createExtractor(String extName) {
        try {
            switch (extName) {
                case TEXT_EXTRACTOR:
                    return new TextExtractor();
                case IMAGE_EXTRACTOR:
                    return new ImageExtractor();
                case VECTOR_GRAPHICS_EXTRACTOR:
                    return new SVGBasedExtractor();
                default:
                    return new TextExtractor();
            }
        } catch (IOException ex) {
            throw new ParserOutputException("Failed to create extractor with name '" + extName + "'.", ex);
        }
    }

    private GiskardOptions createOptions() {
        GiskardOptions opts = new GiskardOptions();
        opts.addOption(help);
        opts.addOption(pdfFile);
        opts.addOption(startPage);
        opts.addOption(endPage);
        opts.addOption(outputDir);
        opts.addOption(outputFormat);
        opts.addOption(extractors);
        return opts;
    }

    /**
     *
     * @param ext
     * @return File with name pattern like filename_pages_startpage_endpage.ext
     */
    protected File outputFile(String ext) {
        return new File(
                DEFAULT_OUTUP_DIR
                + File.separator
                + FilenameUtils.getBaseName(getPDFFile().getName())
                + "_pages_"
                + getStartPage() + "_" + getEndPage()
                + "." + ext);
    }

    /**
     * Options class is extended for custom ordered help message.
     */
    private class GiskardOptions extends Options {

        private static final long serialVersionUID = 185332252329507094L;

        List<Option> optionList = new LinkedList<>();

        @Override
        public Options addOption(Option opt) {
            optionList.add(opt);
            return super.addOption(opt);
        }

        @Override
        public Collection getOptions() {
            return Collections.unmodifiableCollection(optionList);
        }
    }

}
