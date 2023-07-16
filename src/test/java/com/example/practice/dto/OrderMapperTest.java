package com.example.practice.dto;

import com.example.practice.models.Customer;
import com.example.practice.models.Order;
import com.example.practice.models.Product;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderMapperTest {
    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @Test
    public void testOrderToDTO() {
        Product product = Product.builder()
                .id(2L)
                .name("Example Product")
                .quantity(10)
                .cost(100)
                .build();

        Customer customer = Customer.builder()
                .id(2L)
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .build();

        Order order = Order.builder()
                .amount(10)
                .product(product)
                .customer(customer)
                .build();

        OrderDTO orderDTO = orderMapper.orderToDTO(order);

        assertEquals(order.getId(), orderDTO.getId());
        assertEquals(order.getAmount(), orderDTO.getAmount());
        assertEquals(order.getProduct().getId(), orderDTO.getProductId());
        assertEquals(order.getCustomer().getId(), orderDTO.getCustomerId());
    }

    @Test
    public void testDTOToOrder() {
        OrderDTO orderDTO = OrderDTO.builder()
                .id(1L)
                .amount(10)
                .productId(2L)
                .customerId(2L)
                .build();

        Order order = orderMapper.dtoToOrder(orderDTO);

        assertEquals(orderDTO.getId(), order.getId());
        assertEquals(orderDTO.getAmount(), order.getAmount());
        assertEquals(orderDTO.getProductId(), order.getProduct().getId());
        assertEquals(orderDTO.getCustomerId(), order.getCustomer().getId());
    }
}
