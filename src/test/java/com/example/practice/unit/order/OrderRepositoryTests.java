package com.example.practice.unit.order;

import com.example.practice.models.Customer;
import com.example.practice.models.Order;
import com.example.practice.models.Product;
import com.example.practice.repositories.CustomerRepository;
import com.example.practice.repositories.OrderRepository;
import com.example.practice.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderRepositoryTests {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSaveOrder() {
        Customer customer = new Customer();
        customer.setName("NewCustomer");
        customer.setEmail("test@example.com");
        customer.setPhone("88005553535");

        Customer savedCustomer = customerRepository.save(customer);

        Product product = new Product();

        Product savedProduct = productRepository.save(product);

        Order order = new Order();
        order.setAmount(10);
        order.setCustomer(savedCustomer);
        order.setProduct(savedProduct);

        Order savedOrder = orderRepository.save(order);

        Assertions.assertNotNull(savedOrder.getId());
        Assertions.assertEquals(10, savedOrder.getAmount());
        Assertions.assertEquals(savedCustomer.getId(), savedOrder.getCustomerId());
        Assertions.assertEquals(savedProduct.getId(), savedOrder.getProductId());
    }

    @Test
    public void testFindOrderById() {
        Order order = new Order();
        order.setAmount(10);

        Order savedOrder = orderRepository.save(order);

        Order foundOrder = orderRepository.findById(savedOrder.getId()).orElse(null);

        Assertions.assertNotNull(foundOrder);
        Assertions.assertEquals(10, foundOrder.getAmount());
    }

    @Test
    public void testDeleteOrder() {
        Order order = new Order();
        order.setAmount(10);

        Order savedOrder = orderRepository.save(order);

        orderRepository.delete(savedOrder);

        Order deletedOrder = orderRepository.findById(savedOrder.getId()).orElse(null);
        Assertions.assertNull(deletedOrder);
    }
}
