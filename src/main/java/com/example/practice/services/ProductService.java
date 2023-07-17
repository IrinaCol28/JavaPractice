package com.example.practice.services;

import com.example.practice.models.Product;
import com.example.practice.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public boolean deleteProduct(Long id) {
        productRepository.deleteById(id);
        return false;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProductQuantity(Long id, int newQuantity) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setQuantity(newQuantity);
            return productRepository.save(product);
        }
        return null;
    }
}


