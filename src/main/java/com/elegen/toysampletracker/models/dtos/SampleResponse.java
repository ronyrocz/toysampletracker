package com.elegen.toysampletracker.models.dtos;

import com.elegen.toysampletracker.models.SampleStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class SampleResponse {

    private UUID sampleUuid;
    private String sequence;
    private LocalDateTime processedAt;
    private String approvalStatus;

    public SampleResponse(UUID sampleUuid, String sequence, LocalDateTime processedAt, String approvalStatus) {
        this.sampleUuid = sampleUuid;
        this.sequence = sequence;
        this.processedAt = processedAt;
        this.approvalStatus = approvalStatus;
    }

    public UUID getSampleUuid() {
        return sampleUuid;
    }

    public String getSequence() {
        return sequence;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }
}
