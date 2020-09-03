package io.noobymatze.ocr.boundary;

import io.noobymatze.ocr.control.AnalyzeService;
import io.noobymatze.ocr.entity.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/ocr")
public class OcrController {

    private static final Logger LOGGER = Logger.getLogger(OcrController.class.getName());

    private final AnalyzeService analyzeService;

    public OcrController(AnalyzeService analyzeService) {
        this.analyzeService = analyzeService;
    }

    @Operation(
        summary = "Analyze the given file contents and retrieve them as a string."
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String analyzeFile(@RequestBody MultipartFile multipartFile) throws IOException, TesseractException {
        LOGGER.log(Level.INFO, "Analyzing {0}", multipartFile.getOriginalFilename());
        return analyzeService.analyze(multipartFile.getOriginalFilename(), multipartFile.getInputStream());
    }

    @ExceptionHandler({IOException.class})
    public ResponseEntity<ApiError> handleIOException(IOException ex) {
        LOGGER.log(Level.SEVERE, null, ex);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiError("File could not be processed, please try again later."));
    }

    @ExceptionHandler({TesseractException.class})
    public ResponseEntity<ApiError> handleTesseractException(TesseractException ex) {
        LOGGER.log(Level.SEVERE, null, ex);
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ApiError(ex.getMessage()));
    }

}
