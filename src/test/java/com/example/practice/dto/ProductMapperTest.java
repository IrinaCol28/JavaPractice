package com.example.practice.dto;

import com.example.practice.models.Product;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class ProductMapperTest {
    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Test
    void testProductToDTO() {
        Product product = Product.builder()
                .name("Test Product")
                .quantity(10)
                .cost(100)
                .build();

        ProductDTO productDTO = productMapper.productToDTO(product);

        assertEquals(product.getId(), productDTO.getId());
        assertEquals(product.getName(), productDTO.getName());
        assertEquals(product.getQuantity(), productDTO.getQuantity());
        assertEquals(product.getCost(), productDTO.getCost());
    }

    @Test
     void testDTOToProduct() {
        ProductDTO productDTO = ProductDTO.builder()
                .name("Test Product")
                .quantity(10)
                .cost(100)
                .build();

        Product product = productMapper.dtoToProduct(productDTO);

        assertEquals(productDTO.getId(), product.getId());
        assertEquals(productDTO.getName(), product.getName());
        assertEquals(productDTO.getQuantity(), product.getQuantity());
        assertEquals(productDTO.getCost(), product.getCost());
    }

    @Test
    void testProductsToDTOs() {
        Product product1 = Product.builder()
                .name("Product 1")
                .quantity(10)
                .cost(100)
                .build();
        Product product2 = Product.builder()
                .name("Product 2")
                .quantity(20)
                .cost(200)
                .build();

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
