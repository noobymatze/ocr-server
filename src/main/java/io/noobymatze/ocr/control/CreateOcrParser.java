package io.noobymatze.ocr.control;

import io.noobymatze.ocr.entity.ApiError;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class CreateOcrParser {

    @Bean
    public OcrParser createInstance() {
        final var pdfConfig = new PDFParserConfig();
        pdfConfig.setOcrDPI(300);
        pdfConfig.setDetectAngles(true);
        pdfConfig.setOcrStrategy(PDFParserConfig.OCR_STRATEGY.OCR_AND_TEXT_EXTRACTION);

        final var tesseractConfig = new TesseractOCRConfig();
        tesseractConfig.setLanguage("deu");
        tesseractConfig.setEnableImageProcessing(1);

        final var parser = new AutoDetectParser(TikaConfig.getDefaultConfig());

        final var parseContext = new ParseContext();
        parseContext.set(Parser.class, parser);
        parseContext.set(PDFParserConfig.class, pdfConfig);
        parseContext.set(TesseractOCRConfig.class, tesseractConfig);

        return (input, meta) -> {
            try {
                final var baos = new ByteArrayOutputStream();
                parser.parse(input, new BodyContentHandler(baos), meta, parseContext);
                return baos.toString(StandardCharsets.UTF_8);
            } catch (SAXException | TikaException | IOException e) {
                throw new OcrParser.OcrException(e, new ApiError(e.getMessage()));
            }
        };
    }

}
