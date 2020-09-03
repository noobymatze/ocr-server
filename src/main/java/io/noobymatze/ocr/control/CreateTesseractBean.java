package io.noobymatze.ocr.control;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CreateTesseractBean {

    @Bean
    public Tesseract createInstance() {
        Tesseract instance = new Tesseract();
        instance.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata");
        instance.setLanguage("deu");
        return instance;
    }

}
