package com.inventory.inventorysystem.controller;

import com.inventory.inventorysystem.model.Product;
import com.inventory.inventorysystem.repository.ProductRepository;
import com.inventory.inventorysystem.repository.SupplierRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    public ProductController(ProductRepository productRepository, SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    @GetMapping("/products")
    public String listProducts(@RequestParam(required = false) String keyword, Model model) {
        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("products", productRepository.findByNameContainingIgnoreCase(keyword));
        } else {
            model.addAttribute("products", productRepository.findAll());
        }
        model.addAttribute("keyword", keyword);
        return "products";
    }

    @GetMapping("/products/new")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "add-product";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id).orElseThrow();
        model.addAttribute("product", product);
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "edit-product";
    }

    @PostMapping("/products")
    public String saveProduct(@Valid @ModelAttribute Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("suppliers", supplierRepository.findAll());
            return product.getId() == null ? "add-product" : "edit-product";
        }
        productRepository.save(product);
        return "redirect:/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Product deleted");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Cannot delete this product because it has orders or stock history");
        }
        return "redirect:/products";
    }
}