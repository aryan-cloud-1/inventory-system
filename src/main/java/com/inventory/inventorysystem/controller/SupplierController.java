package com.inventory.inventorysystem.controller;

import com.inventory.inventorysystem.model.Supplier;
import com.inventory.inventorysystem.repository.SupplierRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SupplierController {

    private final SupplierRepository supplierRepository;

    public SupplierController(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @GetMapping("/suppliers")
    public String listSuppliers(Model model) {
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "suppliers";
    }

    @GetMapping("/suppliers/new")
    public String showAddForm(Model model) {
        model.addAttribute("supplier", new Supplier());
        return "add-supplier";
    }

    @PostMapping("/suppliers")
    public String saveSupplier(@Valid @ModelAttribute Supplier supplier, BindingResult result) {
        if (result.hasErrors()) {
            return "add-supplier";
        }
        supplierRepository.save(supplier);
        return "redirect:/suppliers";
    }
}