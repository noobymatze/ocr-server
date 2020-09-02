package io.noobymatze.ocr.control;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CreateTesseractBean {

    @Bean
    public Tesseract createInstance() {
        return new Tesseract();
    }

}
