package com.elegen.toysampletracker.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SampleStatus {
    ORDERED, FAILED, PASSED;

    @JsonCreator
    public static SampleStatus fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("QC Status cannot be null");
        }
        switch (value.toUpperCase()) {
            case "PASS":
                return PASSED;
            case "FAIL":
                return FAILED;
            default:
                throw new IllegalArgumentException("Invalid QC Status: " + value);
        }
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
