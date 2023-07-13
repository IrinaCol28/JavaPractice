package com.example.practice.unit.customer;

import com.example.practice.models.Customer;
import com.example.practice.repositories.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testSaveCustomer() {
        Customer customer = new Customer();
        customer.setName("NewCustomer");
        customer.setEmail("test@example.com");
        customer.setPhone("88005553535");

        Customer savedCustomer = customerRepository.save(customer);

        Assertions.assertNotNull(savedCustomer.getId());
        Assertions.assertEquals("NewCustomer", savedCustomer.getName());
        Assertions.assertEquals("test@example.com", savedCustomer.getEmail());
        Assertions.assertEquals("88005553535", savedCustomer.getPhone());
    }

    @Test
    public void testFindCustomerById() {
        Customer customer = new Customer();
        customer.setName("NewCustomer");
        customer.setEmail("test@example.com");
        customer.setPhone("88005553535");

        Customer savedCustomer = customerRepository.save(customer);

        Customer foundCustomer = customerRepository.findById(savedCustomer.getId()).orElse(null);

        Assertions.assertNotNull(foundCustomer);
        Assertions.assertEquals("NewCustomer", foundCustomer.getName());
        Assertions.assertEquals("test@example.com", foundCustomer.getEmail());
        Assertions.assertEquals("88005553535", foundCustomer.getPhone());
    }

    @Test
    public void testDeleteCustomer() {
        Customer customer = new Customer();
        customer.setName("NewCustomer");
        customer.setEmail("test@example.com");
        customer.setPhone("88005553535");

        Customer savedCustomer = customerRepository.save(customer);
        customerRepository.delete(savedCustomer);

        Customer deletedCustomer = customerRepository.findById(savedCustomer.getId()).orElse(null);
        Assertions.assertNull(deletedCustomer);
    }

}
