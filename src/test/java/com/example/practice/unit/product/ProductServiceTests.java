package com.example.practice.unit.product;

import com.example.practice.models.Product;
import com.example.practice.repositories.ProductRepository;
import com.example.practice.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository);
    }

    @Test
    public void testGetProductById() {
        Product product = Product.builder()
                .name("NewProduct")
                .quantity(10)
                .cost(100)
                .build();

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        Product result = productService.getProductById(product.getId());

        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals("NewProduct", result.getName());
        assertEquals(10, result.getQuantity());
        assertEquals(100, result.getCost());

        verify(productRepository, times(1)).findById(product.getId());
    }

    @Test
    public void testSaveProduct() {
        Product product = Product.builder()
                .name("NewProduct")
                .quantity(10)
                .cost(100)
                .build();

        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.saveProduct(product);

        assertNotNull(result);
        assertEquals("NewProduct", result.getName());
        assertEquals(10, result.getQuantity());
        assertEquals(100, result.getCost());

        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testDeleteProduct() {
        Product product = Product.builder()
                .name("NewProduct")
                .quantity(10)
                .cost(100)
                .build();

        productService.deleteProduct(product.getId());

        verify(productRepository, times(1)).deleteById(product.getId());
    }

    @Test
    public void testGetAllProducts() {
        Product product1 = Product.builder()
                .name("Product 1")
                .quantity(10)
                .cost(100)
                .build();
        Product product2 = Product.builder()
                .name("Product 2")
                .quantity(10)
                .cost(100)
                .build();

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals(products.get(0), result.get(0));
        assertEquals(products.get(1), result.get(1));

        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateProductQuantity() {
        int newQuantity = 5;
        Product product = Product.builder()
                .name("NewProduct")
                .quantity(10)
                .cost(100)
                .build();

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.updateProductQuantity(product.getId(), newQuantity);

        assertNotNull(result);
        assertEquals(newQuantity, result.getQuantity());

        verify(productRepository, times(1)).findById(product.getId());
        verify(productRepository, times(1)).save(product);
    }
}
