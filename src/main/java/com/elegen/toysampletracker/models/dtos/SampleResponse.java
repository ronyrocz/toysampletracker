package com.elegen.toysampletracker.models.dtos;

import com.elegen.toysampletracker.models.SampleStatus;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.UUID;

public class SampleResponse {

    private UUID sampleUuid;
    private String sequence;
    private LocalDateTime createdAt;

    public SampleResponse(UUID sampleUuid, String sequence, LocalDateTime createdAt) {
        this.sampleUuid = sampleUuid;
        this.sequence = sequence;
        this.createdAt = createdAt;
    }

    public UUID getSampleUuid() {
        return sampleUuid;
    }

    public String getSequence() {
        return sequence;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
