package com.example.practice.dto;

import com.example.practice.models.Product;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductMapperTest {
    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Test
    public void testProductToDTO() {
        Product product = new Product();
        product.setName("Test Product");
        product.setQuantity(10);
        product.setCost(100.0);

        ProductDTO productDTO = productMapper.productToDTO(product);

        assertEquals(product.getId(), productDTO.getId());
        assertEquals(product.getName(), productDTO.getName());
        assertEquals(product.getQuantity(), productDTO.getQuantity());
        assertEquals(product.getCost(), productDTO.getCost());
    }

    @Test
    public void testDTOToProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setQuantity(10);
        productDTO.setCost(100.0);

        Product product = productMapper.dtoToProduct(productDTO);

        assertEquals(productDTO.getId(), product.getId());
        assertEquals(productDTO.getName(), product.getName());
        assertEquals(productDTO.getQuantity(), product.getQuantity());
        assertEquals(productDTO.getCost(), product.getCost());
    }

    @Test
    public void testProductsToDTOs() {
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setQuantity(10);
        product1.setCost(100);
        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setQuantity(10);
        product2.setCost(100);
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        List<ProductDTO> productDTOs = productMapper.productsToDTOs(products);

        assertEquals(products.size(), productDTOs.size());

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            ProductDTO productDTO = productDTOs.get(i);
            assertEquals(product.getId(), productDTO.getId());
            assertEquals(product.getName(), productDTO.getName());
            assertEquals(product.getQuantity(), productDTO.getQuantity());
            assertEquals(product.getCost(), productDTO.getCost());
        }
    }
}
