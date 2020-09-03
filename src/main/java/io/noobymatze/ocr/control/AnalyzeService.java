package io.noobymatze.ocr.control;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AnalyzeService {

    private static final Logger LOGGER = Logger.getLogger(AnalyzeService.class.getName());

    private final Tesseract tesseract;

    public AnalyzeService(Tesseract tesseract) {
        this.tesseract = tesseract;
    }

    public String analyze(String fileName, InputStream is) throws IOException, TesseractException {
        final var tempFile = File.createTempFile("tmp", fileName);
        LOGGER.log(Level.INFO, "{0}", tempFile.getAbsoluteFile());

        try (var fs = new FileOutputStream(tempFile)) {
            is.transferTo(fs);
        }

        // https://github.com/nguyenq/tess4j/issues/106
        CLibrary.INSTANCE.setlocale(CLibrary.LC_ALL, "C");
        CLibrary.INSTANCE.setlocale(CLibrary.LC_NUMERIC, "C");
        CLibrary.INSTANCE.setlocale(CLibrary.LC_CTYPE, "C");
        return tesseract.doOCR(tempFile);
    }

}
