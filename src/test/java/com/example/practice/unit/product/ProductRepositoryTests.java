package com.example.practice.unit.product;

import com.example.practice.models.Product;
import com.example.practice.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSaveProduct() {
        Product product = Product.builder()
                .name("NewProduct")
                .quantity(10)
                .cost(100)
                .build();

        Product savedProduct = productRepository.save(product);

        assertNotNull(savedProduct.getId());
        assertEquals("NewProduct", savedProduct.getName());
        assertEquals(10, savedProduct.getQuantity());
        assertEquals(100, savedProduct.getCost());
    }

    @Test
    public void testFindProductById() {
        Product product = Product.builder()
                .name("NewProduct")
                .quantity(10)
                .cost(100)
                .build();

        Product savedProduct = productRepository.save(product);

        Product foundProduct = productRepository.findById(savedProduct.getId()).orElse(null);

        assertNotNull(foundProduct);
        assertEquals("NewProduct", foundProduct.getName());
        assertEquals(10, foundProduct.getQuantity());
        assertEquals(100, foundProduct.getCost());
    }

    @Test
    public void testDeleteProduct() {
        Product product = Product.builder()
                .name("NewProduct")
                .quantity(5)
                .cost(100)
                .build();

        Product savedProduct = productRepository.save(product);
        productRepository.delete(savedProduct);

        Product deletedProduct = productRepository.findById(savedProduct.getId()).orElse(null);
        assertNull(deletedProduct);
    }
}

