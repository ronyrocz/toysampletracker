package com.elegen.toysampletracker.services;

import com.elegen.toysampletracker.commons.ResourceNotFoundException;
import com.elegen.toysampletracker.models.Sample;
import com.elegen.toysampletracker.models.SampleQC;
import com.elegen.toysampletracker.models.SampleStatus;
import com.elegen.toysampletracker.models.dtos.SampleQCRequest;
import com.elegen.toysampletracker.repositories.SampleQCRepository;
import com.elegen.toysampletracker.repositories.SampleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SampleQCServiceTest {

    @Mock
    private SampleRepository sampleRepository;

    @Mock
    private SampleQCRepository sampleQCRepository;

    @InjectMocks
    private SampleQCService sampleQCService;

    @Test
    void testLogQCResults_Success() {
        // Mock sample data
        UUID sampleUuid1 = UUID.randomUUID();
        UUID sampleUuid2 = UUID.randomUUID();

        Sample sample1 = new Sample();
        sample1.setSampleUuid(sampleUuid1);
        sample1.setStatus(SampleStatus.ORDERED);

        Sample sample2 = new Sample();
        sample2.setSampleUuid(sampleUuid2);
        sample2.setStatus(SampleStatus.ORDERED);

        // Mock QC request data
        SampleQCRequest.SampleQCEntry entry1 = new SampleQCRequest.SampleQCEntry();
        entry1.setSampleUuid(sampleUuid1);
        entry1.setPlateId(101);
        entry1.setWell("A01");
        entry1.setQc1(17.5);
        entry1.setQc2(2.3);
        entry1.setQc3(SampleStatus.PASSED);

        SampleQCRequest.SampleQCEntry entry2 = new SampleQCRequest.SampleQCEntry();
        entry2.setSampleUuid(sampleUuid2);
        entry2.setPlateId(102);
        entry2.setWell("B02");
        entry2.setQc1(12.4);
        entry2.setQc2(5.6);
        entry2.setQc3(SampleStatus.FAILED);

        SampleQCRequest qcRequest = new SampleQCRequest();
        qcRequest.setSamplesMade(Arrays.asList(entry1, entry2));

        // Mock repository behavior
        when(sampleRepository.findBySampleUuid(sampleUuid1)).thenReturn(Optional.of(sample1));
        when(sampleRepository.findBySampleUuid(sampleUuid2)).thenReturn(Optional.of(sample2));

        // Execute the service method
        sampleQCService.logQCResults(qcRequest);

        // Verify QC data is saved
        verify(sampleQCRepository, times(2)).save(any(SampleQC.class));

        // Verify sample status is updated
        assertEquals(SampleStatus.PASSED, sample1.getStatus());
        assertEquals(SampleStatus.FAILED, sample2.getStatus());

        // Verify sampleRepository.save() is called for status update
        verify(sampleRepository, times(2)).save(any(Sample.class));
    }

    @Test
    void testLogQCResults_ThrowsExceptionWhenEmptyRequest() {
        SampleQCRequest qcRequest = new SampleQCRequest();
        qcRequest.setSamplesMade(null); // No samples

        // Verify IllegalArgumentException is thrown
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            sampleQCService.logQCResults(qcRequest);
        });

        assertEquals("samples_made list cannot be null or empty", exception.getMessage());
    }

    @Test
    void testLogQCResults_ThrowsExceptionForMissingSamples() {
        // Mock request with a non-existent sample
        UUID sampleUuid1 = UUID.randomUUID();

        SampleQCRequest.SampleQCEntry entry = new SampleQCRequest.SampleQCEntry();
        entry.setSampleUuid(sampleUuid1);
        entry.setPlateId(101);
        entry.setWell("A01");
        entry.setQc1(17.5);
        entry.setQc2(2.3);
        entry.setQc3(SampleStatus.PASSED);

        SampleQCRequest qcRequest = new SampleQCRequest();
        qcRequest.setSamplesMade(Arrays.asList(entry));

        // Simulate missing sample in DB
        when(sampleRepository.findBySampleUuid(sampleUuid1)).thenReturn(Optional.empty());

        // Expect the method to throw ResourceNotFoundException
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            sampleQCService.logQCResults(qcRequest);
        });

        // Verify exception message
        assertTrue(exception.getMessage().contains(sampleUuid1.toString()));

        // Ensure that QC data was **never saved**
        verify(sampleQCRepository, never()).save(any(SampleQC.class));
        verify(sampleRepository, never()).save(any(Sample.class));
    }

}
