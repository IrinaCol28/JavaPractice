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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private CustomerService customerService;

    @Test
    void testGetCustomer() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test John")
                .email("john.test@gmail.com")
                .phone("88005553535")
                .build();

        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);

        ResponseEntity<Customer> response = restTemplate.getForEntity("/customers/{id}", Customer.class, customer.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(customer.getId());
        assertThat(response.getBody().getName()).isEqualTo("Test John");
        assertThat(response.getBody().getEmail()).isEqualTo("john.test@gmail.com");
        assertThat(response.getBody().getPhone()).isEqualTo("88005553535");
    }

    @Test
    void testCreateCustomer() {
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

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(createdCustomer.getId());
        assertThat(response.getBody().getName()).isEqualTo("Test John");
        assertThat(response.getBody().getEmail()).isEqualTo("john.test@gmail.com");
        assertThat(response.getBody().getPhone()).isEqualTo("88005553535");
    }

    @Test
    void testDeleteCustomer() {
        Long customerId = 1L;

        when(customerService.deleteCustomer(customerId)).thenReturn(false);

        ResponseEntity<Void> response = restTemplate.exchange("/customers/{id}", HttpMethod.DELETE, null, Void.class, customerId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}