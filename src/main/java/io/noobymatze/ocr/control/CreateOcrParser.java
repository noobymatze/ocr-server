package io.noobymatze.ocr.control;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

@Component
public class CreateOcrParser {

    @Bean
    public OcrParser createInstance() {
        final var pdfConfig = new PDFParserConfig();
        pdfConfig.setOcrDPI(100);
        pdfConfig.setDetectAngles(true);
        pdfConfig.setOcrStrategy(PDFParserConfig.OCR_STRATEGY.OCR_ONLY);

        final var tesseractConfig = new TesseractOCRConfig();
        tesseractConfig.setLanguage("deu");
        tesseractConfig.setEnableImageProcessing(1);

        final var parser = new AutoDetectParser(TikaConfig.getDefaultConfig());

        final var parseContext = new ParseContext();
        parseContext.set(Parser.class, parser);
        parseContext.set(PDFParserConfig.class, pdfConfig);
        parseContext.set(TesseractOCRConfig.class, tesseractConfig);

        return (input, meta) -> {
            final var baos = new ByteArrayOutputStream();
            parser.parse(input, new BodyContentHandler(baos), meta, parseContext);
            return baos.toString(StandardCharsets.UTF_8);
        };
    }

}
