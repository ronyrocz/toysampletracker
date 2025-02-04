package com.elegen.toysampletracker.controllers;

import com.elegen.toysampletracker.models.dtos.SampleQCRequest;
import com.elegen.toysampletracker.services.SampleQCService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
/**
 * Controller for handling Quality Control (QC) logging for samples.
 */
@RestController
@RequestMapping("/api/qc")
public class SampleQCController {

    private static final Logger logger = LoggerFactory.getLogger(SampleQCController.class);


    @Autowired
    private SampleQCService sampleQCService;

    /**
     * Logs Quality Control (QC) results for samples.
     *
     * @param qcRequest the request containing QC results for samples
     * @return ResponseEntity containing a success message
     */
    @PostMapping("/log")
    public ResponseEntity<Map<String, String>> logQCResults(@RequestBody SampleQCRequest qcRequest) {
        logger.info("Received request to log QC results for {} samples", qcRequest.getSamplesMade().size());

        sampleQCService.logQCResults(qcRequest);

        logger.info("QC results logged successfully for {} samples", qcRequest.getSamplesMade().size());
        return ResponseEntity.ok(Collections.singletonMap("message", "QC results logged successfully"));
    }
}
