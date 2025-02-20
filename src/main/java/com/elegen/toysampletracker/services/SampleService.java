package com.elegen.toysampletracker.services;

import com.elegen.toysampletracker.commons.DuplicateEntryException;
import com.elegen.toysampletracker.models.Sample;
import com.elegen.toysampletracker.models.SampleStatus;
import com.elegen.toysampletracker.models.dtos.SampleResponse;
import com.elegen.toysampletracker.repositories.SampleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Service class for handling sample-related operations.
 */
@Service
public class SampleService {

    private static final Logger logger = LoggerFactory.getLogger(SampleService.class);

    @Autowired
    private SampleRepository sampleRepository;

    @Value("${feature-flags.enable-sample-approval:false}")
    private boolean enableSampleApproval;

    /**
     * Retrieves the list of samples that are still in the "ORDERED" status (unprocessed).
     *
     * @return List of SampleResponse objects containing unprocessed samples
     */
    public List<SampleResponse> getUnprocessedSamples() {
        logger.info("Fetching unprocessed samples with status: ORDERED");
        try {
            List<Sample> samples = sampleRepository.findByStatus(SampleStatus.ORDERED);

            if (samples.isEmpty()) {
                logger.warn("No unprocessed samples found.");
            } else {
                logger.info("Retrieved {} unprocessed samples.", samples.size());
            }

            return samples.stream()
                    .map(sample -> new SampleResponse(sample.getSampleUuid(), sample.getSequence()))
                    .collect(Collectors.toList());
        } catch (DataIntegrityViolationException e) {
            logger.warn("Duplicate sample UUID detected. Order creation failed.");
            throw new DuplicateEntryException("A sample with the same UUID already exists.");
        }
    }

}
