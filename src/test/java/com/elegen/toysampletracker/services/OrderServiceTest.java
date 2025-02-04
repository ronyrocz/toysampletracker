package com.elegen.toysampletracker.services;

import com.elegen.toysampletracker.models.Order;
import com.elegen.toysampletracker.models.Sample;
import com.elegen.toysampletracker.models.SampleStatus;
import com.elegen.toysampletracker.models.dtos.OrderRequest;
import com.elegen.toysampletracker.models.dtos.SampleResponse;
import com.elegen.toysampletracker.models.dtos.SampleStatusResponse;
import com.elegen.toysampletracker.repositories.OrderRepository;
import com.elegen.toysampletracker.repositories.SampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private SampleRepository sampleRepository;

    @InjectMocks
    private OrderService orderService;

    private UUID orderUuid;
    private String sampleUuid1;
    private String sampleUuid2;

    @BeforeEach
    void setUp() {
        orderUuid = UUID.randomUUID();
        sampleUuid1 = UUID.randomUUID().toString();
        sampleUuid2 = UUID.randomUUID().toString();
    }

    //Ensure createOrder() correctly saves an order
    @Test
    void testCreateOrder() {
        // Create test data
        OrderRequest orderRequest = new OrderRequest();
        OrderRequest.SampleDTO sampleDTO1 = new OrderRequest.SampleDTO();
        sampleDTO1.setSampleUuid(sampleUuid1);
        sampleDTO1.setSequence("ATGC");

        OrderRequest.SampleDTO sampleDTO2 = new OrderRequest.SampleDTO();
        sampleDTO2.setSampleUuid(sampleUuid2);
        sampleDTO2.setSequence("CGTA");

        orderRequest.setOrder(Arrays.asList(sampleDTO1, sampleDTO2));

        // Mock repository behavior (Order UUID is generated)
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.setOrderUuid(UUID.randomUUID()); // Simulate DB-generated UUID
            return savedOrder;
        });

        // Execute the service method
        UUID resultUuid = orderService.createOrder(orderRequest);

        // Validate that an order UUID is returned
        assertNotNull(resultUuid);

        // Verify that the repository save method was called exactly once
        verify(orderRepository, times(1)).save(any(Order.class));
    }


    // Ensure getSamplesByOrder() returns correct sample data
    @Test
    void testGetSamplesByOrder() {
        // Mock data
        Sample sample1 = new Sample();
        sample1.setSampleUuid(UUID.fromString(sampleUuid1));
        sample1.setSequence("ATGC");

        Sample sample2 = new Sample();
        sample2.setSampleUuid(UUID.fromString(sampleUuid2));
        sample2.setSequence("CGTA");

        List<Sample> sampleList = Arrays.asList(sample1, sample2);

        when(sampleRepository.findByOrder_OrderUuid(orderUuid)).thenReturn(sampleList);

        // Execute
        List<SampleResponse> response = orderService.getSamplesByOrder(orderUuid);

        // Verify
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(UUID.fromString(sampleUuid1), response.get(0).getSampleUuid());
        assertEquals("ATGC", response.get(0).getSequence());
        verify(sampleRepository, times(1)).findByOrder_OrderUuid(orderUuid);
    }

    // Ensure getSampleStatuses() returns correct statuses
    @Test
    void testGetSampleStatuses() {
        // Mock data
        Sample sample1 = new Sample();
        sample1.setSampleUuid(UUID.fromString(sampleUuid1));
        sample1.setStatus(SampleStatus.PASSED);

        Sample sample2 = new Sample();
        sample2.setSampleUuid(UUID.fromString(sampleUuid2));
        sample2.setStatus(SampleStatus.FAILED);

        List<Sample> sampleList = Arrays.asList(sample1, sample2);

        when(sampleRepository.findByOrder_OrderUuid(orderUuid)).thenReturn(sampleList);

        // Execute
        List<SampleStatusResponse> response = orderService.getSampleStatuses(orderUuid);

        // Verify
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(UUID.fromString(sampleUuid1), response.get(0).getSampleUuid());
        assertEquals(SampleStatus.PASSED, response.get(0).getStatus());
        assertEquals(UUID.fromString(sampleUuid2), response.get(1).getSampleUuid());
        assertEquals(SampleStatus.FAILED, response.get(1).getStatus());
        verify(sampleRepository, times(1)).findByOrder_OrderUuid(orderUuid);
    }
}
