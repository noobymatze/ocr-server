package io.noobymatze.ocr.control;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class AnalyzeService {

    private final Tesseract tesseract;

    public AnalyzeService(Tesseract tesseract) {
        this.tesseract = tesseract;
    }

    public String analyze(String fileName, InputStream is) throws IOException, TesseractException {
        final var tempFile = File.createTempFile(fileName, "");
        try (var fs = new FileOutputStream(tempFile)) {
            is.transferTo(fs);
        }

        return tesseract.doOCR(tempFile);
    }

}
