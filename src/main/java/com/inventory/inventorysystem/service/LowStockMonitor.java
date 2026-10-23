package com.inventory.inventorysystem.service;

import com.inventory.inventorysystem.model.Product;
import com.inventory.inventorysystem.repository.ProductRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LowStockMonitor {

    public static final int LOW_STOCK_LIMIT = 10;

    private final ProductRepository productRepository;

    private volatile List<Product> lowStockProducts = List.of();

    public LowStockMonitor(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void checkStock() {
        List<Product> found = productRepository.findByQuantityLessThan(LOW_STOCK_LIMIT);
        lowStockProducts = found;
        for (Product p : found) {
            System.out.println("LOW STOCK ALERT: " + p.getName() + " has only " + p.getQuantity() + " left");
        }
    }

    public List<Product> getLowStockProducts() {
        return lowStockProducts;
    }
}