package com.example.marketplace.controller;

import com.example.marketplace.model.Product;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public String feed(@RequestParam Optional<String> search, Model model) {
        List<Product> products;

        if (search.isPresent()) {
            products = productRepository.findAllByTitleContainingIgnoreCaseOrderByIdDesc(search.get());
        } else {
            products = productRepository.findAllByOrderByIdDesc();
        }
        model.addAttribute("products", products);
        model.addAttribute("query", search.orElse(""));

        return "feed";
    }

    @GetMapping("/account")
    public String profile(Principal principal, Model model) {
        String username = principal.getName();
        User currentUser = userRepository.findByEmail(username);
        List<Product> products = productRepository.findAllByUserOrderByIdDesc(currentUser);

        model.addAttribute("user", currentUser);
        model.addAttribute("products", products);

        return "account";
    }

    @PostMapping("/account/edit")
    public String profileEdit(@Valid @ModelAttribute("user") User user, Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "account";
        }

        String username = principal.getName();
        User currentUser = userRepository.findByEmail(username);

        if (Objects.equals(user.getId(), currentUser.getId())) {
            userRepository.save(user);
        }

        return "redirect:/account";
    }

    @GetMapping("/product/create")
    public String productCreate(Model model) {
        model.addAttribute("product", new Product());
        return "productCreate";
    }

    @PostMapping("/product/create")
    public String productCreate(@Valid @ModelAttribute("product") Product product, Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "productCreate";
        }

        String username = principal.getName();
        User currentUser = userRepository.findByEmail(username);

        product.setUser(currentUser);
        productRepository.save(product);

        return "redirect:/account";
    }

    @GetMapping("/product/{id}/edit")
    public String productEdit(@PathVariable("id") Long productId, Principal principal, Model model) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            User currentUser = userRepository.findByEmail(principal.getName());
            User productOwner = product.get().getUser();

            if (currentUser.getId().equals(productOwner.getId()) || currentUser.getRole().equals("ADMIN")) {
                model.addAttribute("product", product);
                return "productEdit";
            }
        }

        return "redirect:/account";
    }

    @PostMapping("/product/{id}/edit")
    public String productEdit(@Valid @ModelAttribute("product") Product product, Principal principal, BindingResult bindingResult) {
        User currentUser = userRepository.findByEmail(principal.getName());
        User productOwner = product.getUser();

        if (currentUser.getId().equals(productOwner.getId()) || currentUser.getRole().equals("ADMIN")) {
            if (bindingResult.hasErrors()) {
                return "productEdit";
            }

            productRepository.save(product);
        }

        return "redirect:/account";
    }

    @GetMapping("/product/{id}/delete")
    public String productDelete(@PathVariable("id") Long productId, Principal principal) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            User currentUser = userRepository.findByEmail(principal.getName());
            User productOwner = product.get().getUser();

            if (currentUser.getId().equals(productOwner.getId()) || currentUser.getRole().equals("ADMIN")) {
                productRepository.deleteById(productId);
            }
        }

        return "redirect:/account";
    }

}
