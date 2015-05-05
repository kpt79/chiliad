package chiliad.parser.pdf.cli;

import chiliad.parser.pdf.output.JSONOutput;
import java.io.File;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.junit.Test;

public class ParserCliTest {

    @Test
    public void testHasOptionHelp() {
        ParserCli parser = new ParserCli();
        parser.parse(new String[]{"-h", "-pdfFile", "value"});
        assertThat("Help option is present.", parser.hasOptionHelp(), is(true));

        parser = new ParserCli();
        parser.parse(new String[]{"-pdfFile", "value"});
        assertThat("Help option is not present.", parser.hasOptionHelp(), is(false));
    }

    @Test
    public void testGetPDFFile() {
        ParserCli parser = new ParserCli();
        String fooFile = "foo.pdf";
        parser.parse(new String[]{"-pdfFile", fooFile});
        File pdfFile = parser.getPDFFile();
        assertThat("PDF file path", pdfFile.getName(), is(fooFile));
    }

    @Test
    public void testGetOutputDir() {
        ParserCli parser = new ParserCli();
        String outputDirName = "fooOut";
        parser.parse(new String[]{"-pdfFile", "foo.pdf", "-outputDir", outputDirName});
        File outputDir = parser.getOutputDir();
        assertThat("Output directory name", outputDir.getName(), is(outputDirName));
    }

    @Test
    public void testGetStartPage() {
        ParserCli parser = new ParserCli();
        Integer startPage = 3;
        parser.parse(new String[]{"-pdfFile", "foo.pdf", "-startPage", startPage.toString()});
        assertThat("Get start page", parser.getStartPage(), is(startPage));
    }

    @Test
    public void testGetStartPageDefaultValue() {
        ParserCli parser = new ParserCli();
        parser.parse(new String[]{"-pdfFile", "foo.pdf"});
        assertThat("Get start page", parser.getStartPage(), is(1));
    }

    @Test
    public void testGetEndPage() {
        ParserCli parser = new ParserCli();
        Integer endPage = 3;
        parser.parse(new String[]{"-h", "-pdfFile", "foo.pdf", "-endPage", endPage.toString()});
        assertThat("Get end page", parser.getEndPage(), is(endPage));
    }

    @Test
    public void testGetOutputDefaultValue() throws Exception {
        ParserCli parser = new ParserCli();
        parser.parse(new String[]{"-pdfFile", "foo.pdf"});
        assertThat("Output format", parser.getOutputFormat(), instanceOf(JSONOutput.class));
    }

}
