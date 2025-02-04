package com.elegen.toysampletracker.services;

import com.elegen.toysampletracker.models.Sample;
import com.elegen.toysampletracker.models.SampleQC;
import com.elegen.toysampletracker.models.dtos.SampleQCRequest;
import com.elegen.toysampletracker.repositories.SampleQCRepository;
import com.elegen.toysampletracker.repositories.SampleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
/**
 * Service class for handling Quality Control (QC) logging for samples.
 */
@Service
public class SampleQCService {

    private static final Logger logger = LoggerFactory.getLogger(SampleQCService.class);

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private SampleQCRepository sampleQCRepository;

    /**
     * Logs Quality Control (QC) results for the given samples.
     *
     * @param qcRequest the request containing QC results for multiple samples
     */
    public void logQCResults(SampleQCRequest qcRequest) {
        if (qcRequest.getSamplesMade() == null || qcRequest.getSamplesMade().isEmpty()) {
            throw new IllegalArgumentException("samples_made list cannot be null or empty");
        }
        logger.info("Processing QC results for {} samples.", qcRequest.getSamplesMade().size());

        for (SampleQCRequest.SampleQCEntry entry : qcRequest.getSamplesMade()) {
            Optional<Sample> sampleOpt = sampleRepository.findBySampleUuid(entry.getSampleUuid());

            if (sampleOpt.isPresent()) {
                Sample sample = sampleOpt.get();
                SampleQC sampleQC = new SampleQC();

                sampleQC.setSample(sample);
                sampleQC.setPlateId(entry.getPlateId());
                sampleQC.setWell(entry.getWell());
                sampleQC.setQc1(entry.getQc1());
                sampleQC.setQc2(entry.getQc2());
                sampleQC.setQc3(entry.getQc3());

                sampleQCRepository.save(sampleQC);

                sample.setStatus(entry.getQc3());
                sampleRepository.save(sample);
                logger.info("QC results logged for sample UUID: {}", entry.getSampleUuid());
            } else {
                logger.warn("Sample with UUID {} not found. QC results were not logged.", entry.getSampleUuid());
            }
        }
    }
}