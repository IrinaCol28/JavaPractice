package com.example.practice.unit.customer;

import com.example.practice.models.Customer;
import com.example.practice.services.CustomerService;
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
public class CustomerControllerTests {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private CustomerService customerService;

    @Test
    public void testGetCustomer() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test John")
                .email("john.test@gmail.com")
                .phone("88005553535")
                .build();

        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);

        ResponseEntity<Customer> response = restTemplate.getForEntity("/customers/{id}", Customer.class, customer.getId());

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().getId().equals(customer.getId());
        assert response.getBody().getName().equals("Test John");
        assert response.getBody().getEmail().equals("john.test@gmail.com");
        assert response.getBody().getPhone().equals("88005553535");
    }

    @Test
    public void testCreateCustomer() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test John")
                .email("john.test@gmail.com")
                .phone("88005553535")
                .build();
        Customer createdCustomer = Customer.builder()
                .id(1L)
                .name("Test John")
                .email("john.test@gmail.com")
                .phone("88005553535")
                .build();

        when(customerService.saveCustomer(customer)).thenReturn(createdCustomer);

        ResponseEntity<Customer> response = restTemplate.postForEntity("/customers", customer, Customer.class);

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().getId().equals(createdCustomer.getId());
        assert response.getBody().getName().equals("Test John");
        assert response.getBody().getEmail().equals("john.test@gmail.com");
        assert response.getBody().getPhone().equals("88005553535");
    }

    @Test
    public void testDeleteCustomer() {
        Long customerId = 1L;

        when(customerService.deleteCustomer(customerId)).thenReturn(false);

        ResponseEntity<Void> response = restTemplate.exchange("/customers/{id}", HttpMethod.DELETE, null, Void.class, customerId);

        assert response.getStatusCode() == HttpStatus.NO_CONTENT;
    }
}