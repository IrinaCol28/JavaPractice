package com.example.practice.unittests.product;
import com.example.practice.models.Product;
import com.example.practice.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSaveProduct() {
        Product product = new Product();
        product.setName("Название продукта");
        product.setQuantity(10);
        product.setCost(100);

        Product savedProduct = productRepository.save(product);

        Assertions.assertNotNull(savedProduct.getId());
        Assertions.assertEquals("Название продукта", savedProduct.getName());
        Assertions.assertEquals(10, savedProduct.getQuantity());
        Assertions.assertEquals(100, savedProduct.getCost());
    }

    @Test
    public void testFindProductById() {
        Product product = new Product();
        product.setName("Название продукта");
        product.setQuantity(10);
        product.setCost(100);

        Product savedProduct = productRepository.save(product);

        Product foundProduct = productRepository.findById(savedProduct.getId()).orElse(null);

        Assertions.assertNotNull(foundProduct);
        Assertions.assertEquals("Название продукта", foundProduct.getName());
        Assertions.assertEquals(10, foundProduct.getQuantity());
        Assertions.assertEquals(100, foundProduct.getCost());
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product();
        product.setName("Название продукта");
        product.setQuantity(10);
        product.setCost(100);

        Product savedProduct = productRepository.save(product);
        productRepository.delete(savedProduct);

        Product deletedProduct = productRepository.findById(savedProduct.getId()).orElse(null);
        Assertions.assertNull(deletedProduct);
    }
}

