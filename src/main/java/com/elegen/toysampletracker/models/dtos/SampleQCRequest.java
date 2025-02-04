package com.elegen.toysampletracker.models.dtos;

import com.elegen.toysampletracker.models.SampleStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SampleQCRequest {
    @JsonProperty("samples_made")
    private List<SampleQCEntry> samplesMade;

    public List<SampleQCEntry> getSamplesMade() {
        return samplesMade;
    }

    public void setSamplesMade(List<SampleQCEntry> samplesMade) {
        this.samplesMade = (samplesMade != null) ? samplesMade : new ArrayList<>();
    }

    public static class SampleQCEntry {
        @JsonProperty("sample_uuid")
        private UUID sampleUuid;

        @JsonProperty("plate_id")
        private int plateId;

        @JsonProperty("well")
        private String well;

        @JsonProperty("qc_1")
        private double qc1;

        @JsonProperty("qc_2")
        private double qc2;

        @JsonProperty("qc_3")
        private SampleStatus qc3;

        public SampleQCEntry() {
        }

        public UUID getSampleUuid() {
            return sampleUuid;
        }

        public void setSampleUuid(UUID sampleUuid) {
            this.sampleUuid = sampleUuid;
        }

        public int getPlateId() {
            return plateId;
        }

        public void setPlateId(int plateId) {
            this.plateId = plateId;
        }

        public String getWell() {
            return well;
        }

        public void setWell(String well) {
            this.well = well;
        }

        public double getQc1() {
            return qc1;
        }

        public void setQc1(double qc1) {
            this.qc1 = qc1;
        }

        public double getQc2() {
            return qc2;
        }

        public void setQc2(double qc2) {
            this.qc2 = qc2;
        }

        public SampleStatus getQc3() {
            return qc3;
        }

        public void setQc3(SampleStatus qc3) {
            this.qc3 = qc3;
        }
    }
}
