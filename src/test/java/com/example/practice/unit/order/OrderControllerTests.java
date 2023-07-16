package com.example.practice.unit.order;

import com.example.practice.models.Customer;
import com.example.practice.models.Order;
import com.example.practice.models.Product;
import com.example.practice.repositories.CustomerRepository;
import com.example.practice.repositories.ProductRepository;
import com.example.practice.services.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private OrderService orderService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testGetOrder() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .quantity(10)
                .cost(100)
                .build();

        Product savedProduct = productRepository.save(product);

        Order order = Order.builder()
                .id(1L)
                .amount(2)
                .product(savedProduct)
                .customer(savedCustomer)
                .build();

        when(orderService.getOrderById(order.getId())).thenReturn(order);

        ResponseEntity<Order> response = restTemplate.getForEntity("/orders/{id}", Order.class, order.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(order.getId());
        assertThat(response.getBody().getAmount()).isEqualTo(2);
    }

    @Test
    public void testDeleteOrder() {
        Long orderId = 1L;

        when(orderService.deleteOrder(orderId)).thenReturn(false);

        ResponseEntity<Void> response = restTemplate.exchange("/orders/{id}", HttpMethod.DELETE, null, Void.class, orderId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}