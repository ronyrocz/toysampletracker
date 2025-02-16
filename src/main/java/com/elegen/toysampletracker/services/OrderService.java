package com.elegen.toysampletracker.services;

import com.elegen.toysampletracker.models.Order;
import com.elegen.toysampletracker.models.Sample;
import com.elegen.toysampletracker.models.SampleStatus;
import com.elegen.toysampletracker.models.dtos.OrderRequest;
import com.elegen.toysampletracker.models.dtos.SampleResponse;
import com.elegen.toysampletracker.models.dtos.SampleStatusResponse;
import com.elegen.toysampletracker.repositories.OrderRepository;
import com.elegen.toysampletracker.repositories.SampleRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
/**
 * Service class for handling order-related operations.
 */
@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SampleRepository sampleRepository;


    /**
     * Creates a new order with the given samples.
     *
     * @param orderRequest the request containing the list of samples to be ordered
     * @return UUID of the created order
     */
    @Transactional
    public UUID createOrder(OrderRequest orderRequest) {
        logger.info("Creating a new order with {} samples", orderRequest.getOrder().size());

        Order order = new Order();
        List<Sample> samples = orderRequest.getOrder().stream().map(sampleDto -> {
            Sample sample = new Sample();
            sample.setSampleUuid(sampleDto.getSampleUuidAsUUID());
            sample.setStatus(SampleStatus.ORDERED);
            sample.setSequence(sampleDto.getSequence());
            sample.setOrder(order);

            return sample;
        }).collect(Collectors.toList());

        order.setSamples(samples);
        orderRepository.save(order);
        logger.info("Order created successfully with UUID: {}", order.getOrderUuid());
        return order.getOrderUuid();
    }

    /**
     * Retrieves all samples associated with a given order.
     *
     * @param orderUuid the unique identifier of the order
     * @return List of SampleResponse objects
     */
    public List<SampleResponse> getSamplesByOrder(UUID orderUuid) {
        logger.info("Fetching samples for order UUID: {}", orderUuid);

        List<Sample> samples = sampleRepository.findByOrder_OrderUuid(orderUuid);

        if (samples.isEmpty()) {
            logger.warn("No samples found for order UUID: {}", orderUuid);
        } else {
            logger.info("Retrieved {} samples for order UUID: {}", samples.size(), orderUuid);
        }

        return samples.stream()
                .map(sample -> new SampleResponse(sample.getSampleUuid(), sample.getSequence(), sample.getCreatedAt()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the status of samples within a given order.
     *
     * @param orderUuid the unique identifier of the order
     * @return List of SampleStatusResponse objects
     */
    public List<SampleStatusResponse> getSampleStatuses(UUID orderUuid) {
        logger.info("Fetching sample statuses for order UUID: {}", orderUuid);
        List<Sample> samples = sampleRepository.findByOrder_OrderUuid(orderUuid);
        if (samples.isEmpty()) {
            logger.warn("No sample statuses found for order UUID: {}", orderUuid);
        } else {
            logger.info("Retrieved statuses for {} samples in order UUID: {}", samples.size(), orderUuid);
        }
        return samples.stream()
                .map(sample -> new SampleStatusResponse(sample.getSampleUuid(), sample.getStatus()))
                .collect(Collectors.toList());
    }

}