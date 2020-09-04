package io.noobymatze.ocr.boundary;

import io.noobymatze.ocr.control.OcrParser;
import io.noobymatze.ocr.entity.ApiError;
import io.noobymatze.ocr.entity.OcrResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/ocr")
public class OcrController {

    private static final Logger LOGGER = Logger.getLogger(OcrController.class.getName());

    private final OcrParser parser;

    public OcrController(OcrParser parser) {
        this.parser = parser;
    }

    @Operation(
        summary = "Analyze the given file contents and retrieve them as an OcrResult."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = OcrResult.class))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public OcrResult analyzeFile(@RequestBody MultipartFile multipartFile) throws OcrParser.OcrException, IOException {
        LOGGER.log(Level.INFO, "Analyzing {0}", multipartFile.getOriginalFilename());
        final var result = parser.parse(multipartFile.getInputStream(), new Metadata());
        return new OcrResult(result);
    }

    @ExceptionHandler({IOException.class})
    public ResponseEntity<ApiError> handleIOException(IOException ex) {
        LOGGER.log(Level.SEVERE, null, ex);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiError("File could not be processed, please try again later."));
    }

    @ExceptionHandler({OcrParser.OcrException.class})
    public ResponseEntity<ApiError> handleOcrException(OcrParser.OcrException ex) {
        LOGGER.log(Level.SEVERE, null, ex);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ex.getApiError());
    }

}
