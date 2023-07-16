package com.example.practice.unit.customer;

import com.example.practice.models.Customer;
import com.example.practice.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


@SpringBootTest
public class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testSaveCustomer() {
        Customer customer = Customer.builder()
                .name("NewCustomer")
                .email("test@example.com")
                .phone("88005553535")
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        assertNotNull(savedCustomer.getId());
        assertEquals("NewCustomer", savedCustomer.getName());
        assertEquals("test@example.com", savedCustomer.getEmail());
        assertEquals("88005553535", savedCustomer.getPhone());
    }

    @Test
    public void testFindCustomerById() {
        Customer customer = Customer.builder()
                .name("NewCustomer")
                .email("test@example.com")
                .phone("88005553535")
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        Customer foundCustomer = customerRepository.findById(savedCustomer.getId()).orElse(null);

        assertNotNull(foundCustomer);
        assertEquals("NewCustomer", foundCustomer.getName());
        assertEquals("test@example.com", foundCustomer.getEmail());
        assertEquals("88005553535", foundCustomer.getPhone());
    }

    @Test
    public void testDeleteCustomer() {
        Customer customer = Customer.builder()
                .name("NewCustomer")
                .email("test@example.com")
                .phone("88005553535")
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        customerRepository.delete(savedCustomer);

        Customer deletedCustomer = customerRepository.findById(savedCustomer.getId()).orElse(null);
        assertNull(deletedCustomer);
    }

}
