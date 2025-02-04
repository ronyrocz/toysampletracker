package com.elegen.toysampletracker.controllers;

import com.elegen.toysampletracker.models.dtos.SampleResponse;
import com.elegen.toysampletracker.services.SampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Controller for handling sample-related operations.
 */
@RestController
@RequestMapping("/api/samples")
public class SampleController {
    private static final Logger logger = LoggerFactory.getLogger(SampleController.class);

    @Autowired
    private SampleService sampleService;

    /**
     * Retrieves the list of unprocessed samples.
     *
     * @return ResponseEntity containing a map with the list of samples to process
     */
    @GetMapping("/to-process")
    public ResponseEntity<Map<String, List<SampleResponse>>> getSamplesToProcess() {
        logger.info("Fetching unprocessed samples...");
        List<SampleResponse> samples = sampleService.getUnprocessedSamples();

        if (samples.isEmpty()) {
            logger.warn("No unprocessed samples found.");
        } else {
            logger.info("Returning {} unprocessed samples.", samples.size());
        }

        Map<String, List<SampleResponse>> response = new HashMap<>();
        response.put("samples_to_make", samples);

        return ResponseEntity.ok(response);
    }
}
