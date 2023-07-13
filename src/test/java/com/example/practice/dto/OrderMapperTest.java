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
        Order order = new Order();
        order.setId(1L);
        order.setAmount(10);
        Product product = new Product();
        product.setId(2L);
        order.setProduct(product);
        Customer customer = new Customer();
        customer.setId(2L);
        order.setCustomer(customer);

        OrderDTO orderDTO = orderMapper.orderToDTO(order);

        assertEquals(order.getId(), orderDTO.getId());
        assertEquals(order.getAmount(), orderDTO.getAmount());
        assertEquals(order.getProduct().getId(), orderDTO.getProductId());
        assertEquals(order.getCustomer().getId(), orderDTO.getCustomerId());
    }

    @Test
    public void testDTOToOrder() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setAmount(10);
        orderDTO.setProductId(2L);
        orderDTO.setCustomerId(2L);

        Order order = orderMapper.dtoToOrder(orderDTO);

        assertEquals(orderDTO.getId(), order.getId());
        assertEquals(orderDTO.getAmount(), order.getAmount());
        assertEquals(orderDTO.getProductId(), order.getProduct().getId());
        assertEquals(orderDTO.getCustomerId(), order.getCustomer().getId());
    }
}
