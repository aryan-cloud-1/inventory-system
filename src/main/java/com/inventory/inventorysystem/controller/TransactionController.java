package com.inventory.inventorysystem.controller;

import com.inventory.inventorysystem.repository.StockTransactionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TransactionController {

    private final StockTransactionRepository transactionRepository;

    public TransactionController(StockTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/transactions")
    public String listTransactions(Model model) {
        model.addAttribute("transactions", transactionRepository.findAllByOrderByTransactionDateDesc());
        return "transactions";
    }
}