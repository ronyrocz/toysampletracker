package com.elegen.toysampletracker.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "samples")
public class Sample {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "sample_uuid", unique = true, nullable = false, updatable = false)
    private UUID sampleUuid;

    @Column(nullable = false)
    private String sequence;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SampleStatus status;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @OneToOne(mappedBy = "sample", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SampleQC sampleQC;

    public Sample() {
    }

    public Sample(UUID sampleUuid, String sequence, SampleStatus status, Order order) {
        this.sampleUuid = sampleUuid;
        this.sequence = sequence;
        this.status = status;
        this.order = order;
    }

    public void setSampleUuid(UUID sampleUuid) {
        this.sampleUuid = sampleUuid;
    }

    public UUID getSampleUuid() {
        return sampleUuid;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public SampleStatus getStatus() {
        return status;
    }

    public void setStatus(SampleStatus status) {
        this.status = status;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
