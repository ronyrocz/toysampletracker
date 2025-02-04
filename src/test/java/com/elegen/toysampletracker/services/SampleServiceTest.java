package com.elegen.toysampletracker.services;

import com.elegen.toysampletracker.models.Sample;
import com.elegen.toysampletracker.models.SampleStatus;
import com.elegen.toysampletracker.models.dtos.SampleResponse;
import com.elegen.toysampletracker.repositories.SampleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SampleServiceTest {

    @Mock
    private SampleRepository sampleRepository;

    @InjectMocks
    private SampleService sampleService;

    @Test
    void testGetUnprocessedSamples() {
        // Mock sample data
        UUID sampleUuid1 = UUID.randomUUID();
        UUID sampleUuid2 = UUID.randomUUID();

        Sample sample1 = new Sample();
        sample1.setSampleUuid(sampleUuid1);
        sample1.setSequence("ATGC");
        sample1.setStatus(SampleStatus.ORDERED);

        Sample sample2 = new Sample();
        sample2.setSampleUuid(sampleUuid2);
        sample2.setSequence("CGTA");
        sample2.setStatus(SampleStatus.ORDERED);

        // Mock repository behavior
        when(sampleRepository.findByStatus(SampleStatus.ORDERED)).thenReturn(Arrays.asList(sample1, sample2));

        // Execute the service method
        List<SampleResponse> response = sampleService.getUnprocessedSamples();

        // Verify expected output
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(sampleUuid1, response.get(0).getSampleUuid());
        assertEquals("ATGC", response.get(0).getSequence());
        assertEquals(sampleUuid2, response.get(1).getSampleUuid());
        assertEquals("CGTA", response.get(1).getSequence());

        // Verify repository interaction
        verify(sampleRepository, times(1)).findByStatus(SampleStatus.ORDERED);
    }
}
