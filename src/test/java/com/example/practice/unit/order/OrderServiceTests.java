package com.example.practice.unit.order;

import com.example.practice.models.Order;
import com.example.practice.repositories.OrderRepository;
import com.example.practice.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceTests {
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository);
    }

    @Test
    void testGetOrderById() {
        Order order = new Order();
        Long orderId = order.getId();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());

        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testSaveOrder() {
        Order order = new Order();

        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.saveOrder(order);

        assertNotNull(result);

        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testDeleteOrder() {
        Order order = new Order();
        Long orderId = order.getId();

        boolean result = orderService.deleteOrder(orderId);

        assertTrue(!result);

        verify(orderRepository, times(1)).deleteById(orderId);
    }
}