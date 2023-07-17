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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@SpringJUnitConfig
@Testcontainers
 class CustomerIntegrationTest {

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
    void testCreateCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("Test Customer")
                .email("test@example.com")
                .phone("1234567890")
                .build();

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
    void testGetCustomerById() {
        Customer customer = Customer.builder()
                .name("Test Customer")
                .email("test@example.com")
                .phone("1234567890")
                .build();
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
    void testDeleteCustomer() {
        Customer customer = Customer.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .build();
        customerService.saveCustomer(customer);

        restTemplate.delete("http://localhost:" + port + "/customers/" + customer.getId());

        assertThat(customerService.getCustomerById(customer.getId())).isNull();
    }
}
