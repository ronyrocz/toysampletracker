package com.elegen.toysampletracker.models.dtos;

import java.util.UUID;

public class OrderStatusRequest {

    private UUID orderUuidToGetSampleStatusesFor;

    public UUID getOrderUuid() {
        return orderUuidToGetSampleStatusesFor;
    }

    public void setOrderUuid(UUID orderUuidToGetSampleStatusesFor) {
        this.orderUuidToGetSampleStatusesFor = orderUuidToGetSampleStatusesFor;
    }
}
