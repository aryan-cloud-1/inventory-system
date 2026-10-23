package com.inventory.inventorysystem.controller;

import com.inventory.inventorysystem.exception.InsufficientStockException;
import com.inventory.inventorysystem.repository.ProductRepository;
import com.inventory.inventorysystem.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrderController {

    private final ProductRepository productRepository;
    private final OrderService orderService;

    public OrderController(ProductRepository productRepository, OrderService orderService) {
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    @GetMapping("/orders/new")
    public String showOrderForm(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "place-order";
    }

    @PostMapping("/orders")
    public String placeOrder(@RequestParam Long productId,
                             @RequestParam int quantity,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            orderService.placeOrder(productId, quantity);
            redirectAttributes.addFlashAttribute("success", "Order placed successfully");
            return "redirect:/orders/new";
        } catch (InsufficientStockException | IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("products", productRepository.findAll());
            return "place-order";
        }
    }
}