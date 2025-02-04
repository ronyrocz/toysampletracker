package com.elegen.toysampletracker.models;
import jakarta.persistence.*;


@Entity
@Table(name = "sample_qc")
public class SampleQC {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "sample_id", nullable = false, unique = true)
    private Sample sample;

    @Column(nullable = false)
    private Integer plateId;

    @Column(nullable = false)
    private String well;

    @Column(name = "qc_1", nullable = false)
    private double qc1;

    @Column(name = "qc_2", nullable = false)
    private double qc2;

    @Enumerated(EnumType.STRING)
    @Column(name = "qc_3", nullable = false)
    private SampleStatus qc3;

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    public Integer getPlateId() {
        return plateId;
    }

    public void setPlateId(Integer plateId) {
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