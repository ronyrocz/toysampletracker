package com.elegen.toysampletracker.controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.elegen.toysampletracker.models.dtos.OrderRequest;
import com.elegen.toysampletracker.models.dtos.SampleResponse;
import com.elegen.toysampletracker.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
/**
 * Controller for handling order-related operations.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    /**
     * Places a new order.
     *
     * @param orderRequest the request containing sample details
     * @return ResponseEntity containing the generated order UUID
     */
    @PostMapping("/place")
    public ResponseEntity<Map<String, UUID>> placeOrder(@RequestBody OrderRequest orderRequest) {
        logger.info("Received request to place an order with {} samples", orderRequest.getOrder().size());
        UUID orderUuid = orderService.createOrder(orderRequest);
        logger.info("Successfully created order with UUID: {}", orderUuid);
        return ResponseEntity.ok(Collections.singletonMap("order_uuid", orderUuid));
    }


    //TODO Report Sample Statuses in Order (Stretch Goal) Currently happy path implemented with GET
    /**
     * Retrieves all samples associated with a given order.
     *
     * @param orderUuid the unique identifier of the order
     * @return ResponseEntity containing a list of sample responses
     */
    @GetMapping("/{orderUuid}/samples")
    public ResponseEntity<List<SampleResponse>> getSamplesByOrder(@PathVariable UUID orderUuid) {
        logger.info("Fetching samples for order UUID: {}", orderUuid);
        List<SampleResponse> samples = orderService.getSamplesByOrder(orderUuid);

        if (samples.isEmpty()) {
            logger.warn("No samples found for order UUID: {}", orderUuid);
        } else {
            logger.info("Found {} samples for order UUID: {}", samples.size(), orderUuid);
        }

        return ResponseEntity.ok(samples);
    }
}
