package com.elegen.toysampletracker.repositories;

import com.elegen.toysampletracker.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderUuid(UUID orderUuid);
}
