package com.example.marketplace.repositorytest;

import com.example.marketplace.model.Product;
import com.example.marketplace.model.User;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createProduct() {
        User user = new User("john@example.com", "John", "Doe", "password", "USER");
        userRepository.save(user);

        Product product = new Product("Title", "Description", 10.00, "Location", user);
        productRepository.save(product);

        List<Product> foundProducts = productRepository.findAll();
        assertThat(foundProducts).hasSize(1);
    }

    @Test
    void deleteProduct() {
        User user = new User("john@example.com", "John", "Doe", "password", "USER");
        userRepository.save(user);

        Product product = new Product("Title", "Description", 10.00, "Location", user);
        productRepository.save(product);

        productRepository.delete(product);

        List<Product> foundProducts = productRepository.findAll();
        assertThat(foundProducts).hasSize(0);
    }

    @Test
    void findProductsByUser() {
        User user = new User("john@example.com", "John", "Doe", "password", "USER");
        userRepository.save(user);

        Product product = new Product("Title", "Description", 10.00, "Location", user);
        productRepository.save(product);

        List<Product> foundProducts = productRepository.findAllByUserOrderByIdDesc(user);
        assertThat(foundProducts.get(0).getUser()).isEqualTo(user);
    }

    @Test
    void findProductsByTitle() {
        User user = new User("john@example.com", "John", "Doe", "password", "USER");
        userRepository.save(user);

        Product product = new Product("Title", "Description", 10.00, "Location", user);
        productRepository.save(product);

        List<Product> foundProducts = productRepository.findAllByTitleContainingIgnoreCaseOrderByIdDesc("Tit");
        assertThat(foundProducts).hasSize(1);
    }

}
