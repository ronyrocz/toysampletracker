package com.elegen.toysampletracker.models.dtos;

import com.elegen.toysampletracker.models.SampleStatus;

import java.util.UUID;

public class SampleStatusResponse {

    private UUID sampleUuid;
    private SampleStatus status;

    public SampleStatusResponse(UUID sampleUuid, SampleStatus status) {
        this.sampleUuid = sampleUuid;
        this.status = status;
    }

    public UUID getSampleUuid() {
        return sampleUuid;
    }

    public SampleStatus getStatus() {
        return status;
    }
}
