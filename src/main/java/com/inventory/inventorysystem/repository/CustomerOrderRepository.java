package com.inventory.inventorysystem.repository;

import com.inventory.inventorysystem.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    @Query("SELECT o.product.name, SUM(o.quantity) FROM CustomerOrder o " +
            "GROUP BY o.product.id, o.product.name ORDER BY SUM(o.quantity) DESC")
    List<Object[]> findTopSelling();
}