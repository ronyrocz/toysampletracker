package com.elegen.toysampletracker.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class OrderRequest {

    private List<SampleDTO> order;

    public List<SampleDTO> getOrder() {
        return order;
    }

    public void setOrder(List<SampleDTO> order) {
        this.order = order;
    }

    public static class SampleDTO {
        @JsonProperty("sample_uuid")
        private String sampleUuid;
        private String sequence;

        public String getSampleUuid() {
            return sampleUuid;
        }

        public void setSampleUuid(String sampleUuid) {
            this.sampleUuid = sampleUuid;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }

        public UUID getSampleUuidAsUUID() {

            return UUID.fromString(this.sampleUuid);
        }
    }
}
