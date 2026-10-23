package com.inventory.inventorysystem.service;

import com.inventory.inventorysystem.exception.InsufficientStockException;
import com.inventory.inventorysystem.model.CustomerOrder;
import com.inventory.inventorysystem.model.Product;
import com.inventory.inventorysystem.model.StockTransaction;
import com.inventory.inventorysystem.repository.CustomerOrderRepository;
import com.inventory.inventorysystem.repository.ProductRepository;
import com.inventory.inventorysystem.repository.StockTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final CustomerOrderRepository orderRepository;
    private final StockTransactionRepository transactionRepository;

    public OrderService(ProductRepository productRepository,
                        CustomerOrderRepository orderRepository,
                        StockTransactionRepository transactionRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void placeOrder(Long productId, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        Product product = productRepository.findById(productId).orElseThrow();

        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException(
                    "Only " + product.getQuantity() + " of " + product.getName() + " in stock");
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        CustomerOrder order = new CustomerOrder();
        order.setProduct(product);
        order.setQuantity(quantity);
        order.setOrderDate(LocalDateTime.now());
        orderRepository.save(order);

        StockTransaction transaction = new StockTransaction();
        transaction.setProduct(product);
        transaction.setQuantityChange(-quantity);
        transaction.setTransactionType("SALE");
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);
    }
}