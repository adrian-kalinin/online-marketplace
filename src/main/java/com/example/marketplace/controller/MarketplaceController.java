package com.example.marketplace.controller;

import com.example.marketplace.model.Product;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
public class MarketplaceController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public MarketplaceController(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
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

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        String username = principal.getName();
        User currentUser = userRepository.findByEmail(username);
        List<Product> products = productRepository.findAllByUser(currentUser);

        model.addAttribute("user", currentUser);
        model.addAttribute("products", products);

        return "profile";
    }

    @PostMapping("/profile/update")
    public String profileEdit(@Valid @ModelAttribute("user") User user, Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "profile";
        }

        String username = principal.getName();
        User currentUser = userRepository.findByEmail(username);

        if (Objects.equals(user.getId(), currentUser.getId())) {
            userRepository.save(user);
        }

        return "redirect:/profile";
    }

    @GetMapping("/product/{id}/delete")
    public String deleteBook(@PathVariable("id") Long productId, Principal principal) {
        if (productRepository.existsById(productId)) {
            String username = principal.getName();
            User currentUser = userRepository.findByEmail(username);
            Product product = productRepository.getById(productId);

            if (Objects.equals(product.getUser().getId(), currentUser.getId())) {
                productRepository.deleteById(productId);
            }
        }

        return "redirect:/profile";
    }

}
