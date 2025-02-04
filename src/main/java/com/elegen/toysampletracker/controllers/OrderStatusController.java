package com.elegen.toysampletracker.controllers;

import com.elegen.toysampletracker.models.dtos.OrderStatusRequest;
import com.elegen.toysampletracker.models.dtos.SampleStatusResponse;
import com.elegen.toysampletracker.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * Controller for handling order status-related operations.
 */
@RestController
@RequestMapping("/api/order-status")
public class OrderStatusController {
    private static final Logger logger = LoggerFactory.getLogger(OrderStatusController.class);

    @Autowired
    OrderService orderService;

    /**
     * Retrieves the status of samples within an order.
     *
     * @param request contains the order UUID to fetch sample statuses
     * @return ResponseEntity containing a list of sample status responses
     */
    @PostMapping("/get-status")
    public ResponseEntity<List<SampleStatusResponse>> getSampleStatuses(@RequestBody OrderStatusRequest request) {
        logger.info("Received request to fetch sample statuses for order UUID: {}", request.getOrderUuid());
        List<SampleStatusResponse> statuses = orderService.getSampleStatuses(request.getOrderUuid());
        if (statuses.isEmpty()) {
            logger.warn("No sample statuses found for order UUID: {}", request.getOrderUuid());
        } else {
            logger.info("Returning {} sample statuses for order UUID: {}", statuses.size(), request.getOrderUuid());
        }

        return ResponseEntity.ok(statuses);
    }
}
