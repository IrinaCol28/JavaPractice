package com.example.practice.integration;

import com.example.practice.dto.ProductDTO;
import com.example.practice.models.Product;
import com.example.practice.repositories.ProductRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@SpringJUnitConfig
@Testcontainers
class ProductIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Container
    private static final DatabaseContainer CONTAINER = DatabaseContainer.getInstance();

    @BeforeAll
    static void setup() {
        CONTAINER.start();
    }

    @AfterAll
    static void teardown() {
        CONTAINER.stop();
    }

    @Test
    void testCreateProduct() {
        ProductDTO productDTO = ProductDTO.builder()
                .name("Test Product")
                .quantity(5)
                .cost(100)
                .build();

        ProductDTO createdProduct = restTemplate.postForObject(
                "http://localhost:" + port + "/products",
                productDTO,
                ProductDTO.class
        );

        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getId()).isNotNull();
        assertThat(createdProduct.getName()).isEqualTo("Test Product");
        assertThat(createdProduct.getCost()).isEqualTo(100);
        assertThat(createdProduct.getQuantity()).isEqualTo(5);
    }

    @Test
    void testGetProductById() {
        Product product = Product.builder()
                .name("Test Product")
                .quantity(10)
                .cost(100)
                .build();
        productRepository.save(product);

        ProductDTO retrievedProduct = restTemplate.getForObject(
                "http://localhost:" + port + "/products/" + product.getId(),
                ProductDTO.class
        );

        assertThat(retrievedProduct).isNotNull();
        assertThat(retrievedProduct.getId()).isEqualTo(product.getId());
        assertThat(retrievedProduct.getName()).isEqualTo("Test Product");
        assertThat(retrievedProduct.getCost()).isEqualTo(100);
        assertThat(retrievedProduct.getQuantity()).isEqualTo(10);
    }

    @Test
   void testUpdateProductQuantity() {
        Product product = Product.builder()
                .name("Test Product")
                .quantity(10)
                .cost(100)
                .build();
        productRepository.save(product);

        int updatedQuantity = 10;

        ResponseEntity<ProductDTO> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/products/" + product.getId() + "/quantity?quantity=" + updatedQuantity,
                HttpMethod.PUT,
                null,
                ProductDTO.class
        );

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        ProductDTO updatedProduct = responseEntity.getBody();
        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct.getId()).isEqualTo(product.getId());
        assertThat(updatedProduct.getName()).isEqualTo("Test Product");
        assertThat(updatedProduct.getCost()).isEqualTo(100);
        assertThat(updatedProduct.getQuantity()).isEqualTo(updatedQuantity);
    }

    @Test
   void testDeleteProduct() {
        Product product = Product.builder()
                .name("Test Product")
                .quantity(10)
                .cost(100)
                .build();
        productRepository.save(product);

        restTemplate.delete("http://localhost:" + port + "/products/" + product.getId());

        assertThat(productRepository.findById(product.getId())).isEmpty();
    }
}

