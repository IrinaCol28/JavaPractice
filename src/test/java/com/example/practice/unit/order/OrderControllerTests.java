package com.example.practice.unit.order;

import com.example.practice.models.Customer;
import com.example.practice.models.Order;
import com.example.practice.models.Product;
import com.example.practice.repositories.CustomerRepository;
import com.example.practice.repositories.ProductRepository;
import com.example.practice.services.CustomerService;
import com.example.practice.services.OrderService;
import com.example.practice.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTests {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private ProductService productService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testGetOrder() {
        Order order = new Order();
        Long orderId = 100L;
        order.setId(orderId);

        Customer customer = new Customer();
        customer.setName("NewCustomer");
        customer.setEmail("test@example.com");
        customer.setPhone("88005553535");

        Customer savedCustomer = customerRepository.save(customer);

        Product product = new Product();

        Product savedProduct = productRepository.save(product);

        order.setCustomer(savedCustomer);
        order.setProduct(savedProduct);
        order.setAmount(2);

        when(orderService.getOrderById(orderId)).thenReturn(order);

        ResponseEntity<Order> response = restTemplate.getForEntity("/orders/{id}", Order.class, orderId);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().getId().equals(orderId);
        assert response.getBody().getCustomerId().equals(customer.getId());
        assert response.getBody().getProductId().equals(product.getId());
        assert response.getBody().getAmount() == 2;
    }

    @Test
    public void testCreateOrder() {
        Order order = new Order();
        Long orderId = 100L;
        order.setId(orderId);
        order.setAmount(2);

        Customer customer = new Customer();
        customer.setName("NewCustomer");
        customer.setEmail("test@example.com");
        customer.setPhone("88005553535");

        Customer savedCustomer = customerRepository.save(customer);

        Product product = new Product();
        product.setName("NewProduct");
        product.setCost(100);
        product.setQuantity(5);
        Product savedProduct = productRepository.save(product);

        order.setCustomer(savedCustomer);
        order.setProduct(savedProduct);

        when(customerService.getCustomerById(order.getCustomerId())).thenReturn(savedCustomer);
        when(productService.getProductById(order.getProductId())).thenReturn(savedProduct);
        when(orderService.saveOrder(order)).thenReturn(order);

        ResponseEntity<Order> response = restTemplate.postForEntity("/orders", order, Order.class);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().getCustomerId().equals(savedCustomer.getId());
        assert response.getBody().getProductId().equals(savedProduct.getId());
        assert response.getBody().getAmount() == 2;
    }

    @Test
    public void testDeleteOrder() {
        Long orderId = 1L;

        when(orderService.deleteOrder(orderId)).thenReturn(false);

        ResponseEntity<Void> response = restTemplate.exchange("/orders/{id}", HttpMethod.DELETE, null, Void.class, orderId);

        assert response.getStatusCode() == HttpStatus.NO_CONTENT;
    }
}
