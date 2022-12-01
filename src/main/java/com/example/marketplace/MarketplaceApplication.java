package com.example.marketplace;

import com.example.marketplace.model.Product;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class MarketplaceApplication {
    private static final Logger log = LoggerFactory.getLogger(MarketplaceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MarketplaceApplication.class, args);
    }

    @Bean
    @Profile({"dev & !test"})
    public CommandLineRunner createDemo(UserRepository userRepository, ProductRepository productRepository) {
        return (args) -> {
            log.info("Creating and saving demo data.");

            // Create users
            User user1 = new User("user@example.com", "John", "Doe", "$2a$06$3jYRJrg0ghaaypjZ/.g4SethoeA51ph3UD4kZi9oPkeMTpjKU5uo6", "USER");
            User user2 = new User("admin@example.com", "Admin", "Admin", "$2a$10$0MMwY.IQqpsVc1jC8u7IJ.2rT8b0Cd3b3sfIBGV2zfgnPGtT4r0.C", "ADMIN");

            userRepository.save(user1);
            userRepository.save(user2);

            // Create products
            Product product1 = new Product("Sofa from IKEA", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Nibh tellus molestie nunc non blandit massa enim nec dui. Proin sagittis nisl rhoncus mattis rhoncus urna. Nec ultrices dui sapien eget mi proin sed.", 150, "Leppävaara, Espoo", user1);
            Product product2 = new Product("Apple iPhone 11 Pro", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Viverra vitae congue eu consequat ac felis donec. Non consectetur a erat nam at lectus urna duis convallis. Dolor magna eget est lorem ipsum dolor sit amet. Vel facilisis volutpat est velit egestas dui. Odio ut sem nulla pharetra diam sit amet.", 430, "Kalasatama, Helsinki", user2);
            Product product3 = new Product("T-shirt", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Scelerisque eleifend donec pretium vulputate sapien nec.", 7.50, "Pasila, Helsinki", user1);

            productRepository.save(product1);
            productRepository.save(product2);
            productRepository.save(product3);
        };
    }

}
