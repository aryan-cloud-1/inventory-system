package com.inventory.inventorysystem.controller;

import com.inventory.inventorysystem.model.Product;
import com.inventory.inventorysystem.repository.CustomerOrderRepository;
import com.inventory.inventorysystem.repository.ProductRepository;
import com.inventory.inventorysystem.service.LowStockMonitor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final ProductRepository productRepository;
    private final CustomerOrderRepository orderRepository;
    private final LowStockMonitor lowStockMonitor;

    public DashboardController(ProductRepository productRepository,
                               CustomerOrderRepository orderRepository,
                               LowStockMonitor lowStockMonitor) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.lowStockMonitor = lowStockMonitor;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<Product> products = productRepository.findAll();

        double totalValue = 0;
        for (Product p : products) {
            totalValue += p.getPrice() * p.getQuantity();
        }

        List<Object[]> topSelling = orderRepository.findTopSelling();
        if (topSelling.size() > 5) {
            topSelling = topSelling.subList(0, 5);
        }

        model.addAttribute("totalProducts", products.size());
        model.addAttribute("totalValue", totalValue);
        model.addAttribute("lowStockProducts", lowStockMonitor.getLowStockProducts());
        model.addAttribute("lowStockLimit", LowStockMonitor.LOW_STOCK_LIMIT);
        model.addAttribute("topSelling", topSelling);
        return "dashboard";
    }
}