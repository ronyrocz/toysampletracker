package com.elegen.toysampletracker.repositories;

import com.elegen.toysampletracker.models.SampleQC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SampleQCRepository extends JpaRepository<SampleQC, Long> {

    Optional<SampleQC> findBySample_SampleUuid(UUID sampleUuid);
}
