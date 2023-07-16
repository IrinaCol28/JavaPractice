package com.example.practice.unit.product;

import com.example.practice.models.Product;
import com.example.practice.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ProductService productService;

    @Test
    void testGetProduct() {
        Product product = Product.builder()
                .id(1L)
                .name("Test")
                .quantity(5)
                .cost(100)
                .build();

        when(productService.getProductById(product.getId())).thenReturn(product);

        ResponseEntity<Product> response = restTemplate.getForEntity("/products/{id}", Product.class, product.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(product.getId());
        assertThat(response.getBody().getName()).isEqualTo("Test");
        assertThat(response.getBody().getCost()).isEqualTo(100);
        assertThat(response.getBody().getQuantity()).isEqualTo(5);
    }

    @Test
    void testCreateProduct() {
        Product product = Product.builder()
                .id(1L)
                .name("Test")
                .quantity(5)
                .cost(100)
                .build();

        Product createdProduct = Product.builder()
                .id(1L)
                .name("Test")
                .quantity(5)
                .cost(100)
                .build();

        when(productService.saveProduct(product)).thenReturn(createdProduct);

        ResponseEntity<Product> response = restTemplate.postForEntity("/products", product, Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(product.getId());
        assertThat(response.getBody().getName()).isEqualTo("Test");
        assertThat(response.getBody().getCost()).isEqualTo(100);
        assertThat(response.getBody().getQuantity()).isEqualTo(5);
    }

    @Test
    void testDeleteProduct() {
        Long productId = 1L;

        when(productService.deleteProduct(productId)).thenReturn(false);

        ResponseEntity<Void> response = restTemplate.exchange("/products/{id}", HttpMethod.DELETE, null, Void.class, productId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void testUpdateProductQuantity() {
        Long productId = 1L;
        int newQuantity = 10;

        Product product = Product.builder()
                .id(1L)
                .name("Test")
                .quantity(newQuantity)
                .cost(100)
                .build();

        when(productService.updateProductQuantity(productId, newQuantity)).thenReturn(product);

        ResponseEntity<Product> response = restTemplate.exchange(
                "/products/{id}/quantity?quantity={quantity}",
                HttpMethod.PUT,
                null,
                Product.class,
                productId,
                newQuantity
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(productId);
        assertThat(response.getBody().getName()).isEqualTo("Test");
        assertThat(response.getBody().getCost()).isEqualTo(100);
        assertThat(response.getBody().getQuantity()).isEqualTo(newQuantity);
    }
}