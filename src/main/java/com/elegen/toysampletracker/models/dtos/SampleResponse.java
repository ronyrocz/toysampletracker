package com.elegen.toysampletracker.models.dtos;

import com.elegen.toysampletracker.models.SampleStatus;

import java.util.UUID;

public class SampleResponse {

    private UUID sampleUuid;
    private String sequence;

    public SampleResponse(UUID sampleUuid, String sequence) {
        this.sampleUuid = sampleUuid;
        this.sequence = sequence;
    }

    public UUID getSampleUuid() {
        return sampleUuid;
    }

    public String getSequence() {
        return sequence;
    }
}
