package com.example.practice.unitTestsForBusinessLogic.productTest;

import com.example.practice.models.Product;
import com.example.practice.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTests {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ProductService productService;

    @Test
    public void testGetProduct() {
        Product product = new Product();
        Long productId = 1L;
        product.setId(productId);
        product.setName("Test");
        product.setCost(100);
        product.setQuantity(5);

        when(productService.getProductById(productId)).thenReturn(product);

        ResponseEntity<Product> response = restTemplate.getForEntity("/products/{id}", Product.class, productId);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().getId().equals(productId);
        assert response.getBody().getName().equals("Test");
        assert response.getBody().getCost() == 100;
        assert response.getBody().getQuantity() == 5;
    }

    @Test
    public void testCreateProduct() {
        Product product = new Product();
        product.setName("NewProduct");
        Long productId = 1L;
        product.setId(productId);
        product.setCost(100);
        product.setQuantity(5);

        Product createdProduct = new Product();
        createdProduct.setName("NewProduct");
        createdProduct.setCost(100);
        createdProduct.setQuantity(5);

        when(productService.saveProduct(product)).thenReturn(createdProduct);

        ResponseEntity<Product> response = restTemplate.postForEntity("/products", product, Product.class);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().getId().equals(1L);
        assert response.getBody().getName().equals("NewProduct");
        assert response.getBody().getCost() == 100;
        assert response.getBody().getQuantity() == 5;
    }

    @Test
    public void testDeleteProduct() {
        Long productId = 1L;

        when(productService.deleteProduct(productId)).thenReturn(false);

        ResponseEntity<Void> response = restTemplate.exchange("/products/{id}", HttpMethod.DELETE, null, Void.class, productId);

        assert response.getStatusCode() == HttpStatus.NO_CONTENT;
    }

    @Test
    public void testUpdateProductQuantity() {
        Long productId = 1L;
        int newQuantity = 10;

        Product product = new Product();
        product.setId(productId);
        product.setName("Test");
        product.setCost(100);
        product.setQuantity(newQuantity);

        when(productService.updateProductQuantity(productId, newQuantity)).thenReturn(product);

        ResponseEntity<Product> response = restTemplate.exchange(
                "/products/{id}/quantity?quantity={quantity}",
                HttpMethod.PUT,
                null,
                Product.class,
                productId,
                newQuantity
        );

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().getId().equals(productId);
        assert response.getBody().getName().equals("Test");
        assert response.getBody().getCost() == 100;
        assert response.getBody().getQuantity() == newQuantity;
    }
}
