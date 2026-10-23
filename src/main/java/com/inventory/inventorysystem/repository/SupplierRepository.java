package com.inventory.inventorysystem.repository;

import com.inventory.inventorysystem.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}