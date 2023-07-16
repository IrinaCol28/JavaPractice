package com.example.practice.unit.order;

import com.example.practice.models.Customer;
import com.example.practice.models.Order;
import com.example.practice.models.Product;
import com.example.practice.repositories.CustomerRepository;
import com.example.practice.repositories.OrderRepository;
import com.example.practice.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderRepositoryTests {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSaveOrder() {
        Customer customer = Customer.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        Product product = Product.builder()
                .name("Test Product")
                .quantity(10)
                .cost(100)
                .build();

        Product savedProduct = productRepository.save(product);

        Order order = Order.builder()
                .amount(2)
                .product(savedProduct)
                .customer(savedCustomer)
                .build();

        Order savedOrder = orderRepository.save(order);

        assertNotNull(savedOrder.getId());
        assertEquals(2, savedOrder.getAmount());
        assertEquals(savedCustomer.getId(), savedOrder.getCustomer().getId());
        assertEquals(savedProduct.getId(), savedOrder.getProduct().getId());
    }

    @Test
    public void testFindOrderById() {
        Order order = Order.builder()
                .id(1L)
                .amount(2)
                .build();
        Order savedOrder = orderRepository.save(order);

        Order foundOrder = orderRepository.findById(savedOrder.getId()).orElse(null);

        assertNotNull(foundOrder);
        assertEquals(2, foundOrder.getAmount());
    }

    @Test
    public void testDeleteOrder() {
        Order order = Order.builder()
                .id(1L)
                .amount(2)
                .build();

        Order savedOrder = orderRepository.save(order);

        orderRepository.delete(savedOrder);

        Order deletedOrder = orderRepository.findById(savedOrder.getId()).orElse(null);
        assertNull(deletedOrder);
    }
}
