package com.example.practice.dto;

import com.example.practice.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "id", source = "product.id")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "quantity", source = "product.quantity")
    @Mapping(target = "cost", source = "product.cost")
    ProductDTO productToDTO(Product product);

    @Mapping(target = "id", source = "productDTO.id")
    @Mapping(target = "name", source = "productDTO.name")
    @Mapping(target = "quantity", source = "productDTO.quantity")
    @Mapping(target = "cost", source = "productDTO.cost")
    Product dtoToProduct(ProductDTO productDTO);

    default List<ProductDTO> productsToDTOs(List<Product> products) {
        return products.stream()
                .map(this::productToDTO)
                .toList();
    }
}