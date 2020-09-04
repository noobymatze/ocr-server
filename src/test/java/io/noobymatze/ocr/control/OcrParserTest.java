package io.noobymatze.ocr.control;

import org.apache.tika.metadata.Metadata;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
public class OcrParserTest {

    @Autowired
    private OcrParser parser;

    @Test
    void testOcrWorks() throws OcrParser.OcrException {
        final var toBeParsed = OcrParserTest.class
            .getClassLoader()
            .getResourceAsStream("receipt.jpg");

        final var result = parser.parse(toBeParsed, new Metadata());
        assertThat(result, CoreMatchers.containsString("KNOBLAUCH"));
    }

}
