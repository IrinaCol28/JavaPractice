package com.example.practice.integration;

import com.example.practice.dto.CustomerDTO;
import com.example.practice.models.Customer;
import com.example.practice.services.CustomerService;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@SpringJUnitConfig
@Testcontainers
public class CustomerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerService customerService;

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
    public void testCreateCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("Test Customer");
        customerDTO.setEmail("test@example.com");
        customerDTO.setPhone("1234567890");

        CustomerDTO createdCustomer = restTemplate.postForObject(
                "http://localhost:" + port + "/customers",
                customerDTO,
                CustomerDTO.class
        );

        assertThat(createdCustomer).isNotNull();
        assertThat(createdCustomer.getId()).isNotNull();
        assertThat(createdCustomer.getName()).isEqualTo("Test Customer");
        assertThat(createdCustomer.getEmail()).isEqualTo("test@example.com");
        assertThat(createdCustomer.getPhone()).isEqualTo("1234567890");
    }

    @Test
    public void testGetCustomerById() {
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setEmail("test@example.com");
        customer.setPhone("1234567890");
        customerService.saveCustomer(customer);

        CustomerDTO retrievedCustomer = restTemplate.getForObject(
                "http://localhost:" + port + "/customers/" + customer.getId(),
                CustomerDTO.class
        );

        assertThat(retrievedCustomer).isNotNull();
        assertThat(retrievedCustomer.getId()).isEqualTo(customer.getId());
        assertThat(retrievedCustomer.getName()).isEqualTo("Test Customer");
        assertThat(retrievedCustomer.getEmail()).isEqualTo("test@example.com");
        assertThat(retrievedCustomer.getPhone()).isEqualTo("1234567890");
    }

    @Test
    public void testDeleteCustomer() {
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setEmail("test@example.com");
        customer.setPhone("1234567890");
        customerService.saveCustomer(customer);

        restTemplate.delete("http://localhost:" + port + "/customers/" + customer.getId());

        assertThat(customerService.getCustomerById(customer.getId())).isNull();
    }
}
