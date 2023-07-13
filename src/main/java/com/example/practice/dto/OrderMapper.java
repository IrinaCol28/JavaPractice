package com.example.practice.dto;

import com.example.practice.models.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "id", source = "order.id")
    @Mapping(target = "amount", source = "order.amount")
    @Mapping(target = "productId", source = "order.product.id")
    @Mapping(target = "customerId", source = "order.customer.id")
    OrderDTO orderToDTO(Order order);

    @Mapping(target = "id", source = "orderDTO.id")
    @Mapping(target = "amount", source = "orderDTO.amount")
    @Mapping(target = "product.id", source = "orderDTO.productId")
    @Mapping(target = "customer.id", source = "orderDTO.customerId")
    Order dtoToOrder(OrderDTO orderDTO);

}
