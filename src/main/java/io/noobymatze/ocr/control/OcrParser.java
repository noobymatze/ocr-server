package io.noobymatze.ocr.control;

import io.noobymatze.ocr.entity.ApiError;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface OcrParser {

    final class OcrException extends Exception {

        private ApiError apiError;

        public OcrException(Throwable cause, ApiError apiError) {
            super(cause);
            this.apiError = apiError;
        }

        public ApiError getApiError() {
            return apiError;
        }

    }

    String parse(InputStream input, Metadata meta) throws OcrException;

}
