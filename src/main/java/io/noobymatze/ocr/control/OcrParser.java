package io.noobymatze.ocr.control;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface OcrParser {

    String parse(InputStream input, Metadata meta) throws IOException, TikaException, SAXException;

}
