package com.example.practice.integration;

import com.example.practice.dto.OrderDTO;
import com.example.practice.models.Customer;
import com.example.practice.models.Order;
import com.example.practice.models.Product;
import com.example.practice.services.CustomerService;
import com.example.practice.services.OrderService;
import com.example.practice.services.ProductService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@SpringJUnitConfig
@Testcontainers
public class OrderIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Container
    private static final DatabaseContainer CONTAINER = DatabaseContainer.getInstance();

    @BeforeAll
    public static void setup() {
        CONTAINER.start();
    }

    @AfterAll
    public static void teardown() {
        CONTAINER.stop();
    }

    @Test
    public void testGetOrderById() {
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setEmail("test@example.com");
        customer.setPhone("1234567890");
        customerService.saveCustomer(customer);

        Product product = new Product();
        product.setName("Test Product");
        product.setCost(100);
        product.setQuantity(5);
        productService.saveProduct(product);

        Order order = new Order();
        order.setCustomer(customer);
        order.setProduct(product);
        order.setAmount(2);
        orderService.saveOrder(order);

        ResponseEntity<OrderDTO> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/orders/" + order.getId(),
                HttpMethod.GET,
                null,
                OrderDTO.class
        );

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        OrderDTO retrievedOrder = responseEntity.getBody();
        assertThat(retrievedOrder).isNotNull();
        assertThat(retrievedOrder.getId()).isEqualTo(order.getId());
        assertThat(retrievedOrder.getCustomerId()).isEqualTo(customer.getId());
        assertThat(retrievedOrder.getProductId()).isEqualTo(product.getId());
        assertThat(retrievedOrder.getAmount()).isEqualTo(2);
    }

    @Test
    public void testDeleteOrder() {
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setEmail("test@example.com");
        customer.setPhone("1234567890");
        customerService.saveCustomer(customer);

        Product product = new Product();
        product.setName("Test Product");
        product.setCost(100);
        product.setQuantity(5);
        productService.saveProduct(product);

        Order order = new Order();
        order.setCustomer(customer);
        order.setProduct(product);
        order.setAmount(2);
        orderService.saveOrder(order);

        restTemplate.delete("http://localhost:" + port + "/orders/" + order.getId());

        assertThat(orderService.getOrderById(order.getId())).isNull();
    }
}
