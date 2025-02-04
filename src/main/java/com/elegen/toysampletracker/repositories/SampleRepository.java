package com.elegen.toysampletracker.repositories;

import com.elegen.toysampletracker.models.Sample;
import com.elegen.toysampletracker.models.SampleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {

    Optional<Sample> findBySampleUuid(UUID sampleUuid);

    List<Sample> findByOrder_OrderUuid(UUID orderUuid);

    List<Sample> findByStatus(SampleStatus status);
}
