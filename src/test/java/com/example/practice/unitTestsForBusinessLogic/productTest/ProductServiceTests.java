package com.example.practice.unitTestsForBusinessLogic.productTest;

import com.example.practice.models.Product;
import com.example.practice.repositories.ProductRepository;
import com.example.practice.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

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
        Product product = new Product();
        Long productId = product.getId();
        product.setName("Название продукта");
        product.setQuantity(10);
        product.setCost(100);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(productId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(productId, result.getId());
        Assertions.assertEquals("Название продукта", result.getName());
        Assertions.assertEquals(10, result.getQuantity());
        Assertions.assertEquals(100, result.getCost());

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void testSaveProduct() {
        Product product = new Product();
        product.setName("Название продукта");
        product.setQuantity(10);
        product.setCost(100);

        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.saveProduct(product);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Название продукта", result.getName());
        Assertions.assertEquals(10, result.getQuantity());
        Assertions.assertEquals(100, result.getCost());

        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product();
        Long productId = product.getId();

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void testGetAllProducts() {
        Product product1 = new Product();
        product1.setName("Продукт 1");
        product1.setQuantity(10);
        product1.setCost(100);
        Product product2 = new Product();
        product2.setName("Продукт 2");
        product2.setQuantity(10);
        product2.setCost(100);
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(products.get(0), result.get(0));
        Assertions.assertEquals(products.get(1), result.get(1));

        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateProductQuantity() {
        int newQuantity = 5;
        Product product = new Product();
        Long productId = product.getId();
        product.setName("Название продукта");
        product.setQuantity(10);
        product.setCost(100);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.updateProductQuantity(productId, newQuantity);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(newQuantity, result.getQuantity());

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(product);
    }
}
