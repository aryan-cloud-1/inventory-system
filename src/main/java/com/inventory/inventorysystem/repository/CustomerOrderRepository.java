package com.inventory.inventorysystem.repository;

import com.inventory.inventorysystem.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
}