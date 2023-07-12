package com.example.practice.unittests.order;

import com.example.practice.models.Order;
import com.example.practice.repositories.OrderRepository;
import com.example.practice.services.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class OrderServiceTests {
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository);
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order();
        Long orderId = order.getId();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(orderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(orderId, result.getId());

        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    public void testSaveOrder() {
        Order order = new Order();

        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.saveOrder(order);

        Assertions.assertNotNull(result);

        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testDeleteOrder() {
        Order order = new Order();
        Long orderId = order.getId();

        boolean result = orderService.deleteOrder(orderId);

        Assertions.assertTrue(!result);

        verify(orderRepository, times(1)).deleteById(orderId);
    }
}