package com.example.marketplace.controller;

import com.example.marketplace.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MarketplaceController {

    private final ProductRepository productRepository;

    @Autowired
    public MarketplaceController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/feed";
    }

    @GetMapping("/feed")
    public String feed(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "feed";
    }

}
