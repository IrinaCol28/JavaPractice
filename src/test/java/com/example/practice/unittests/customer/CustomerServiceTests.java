package com.example.practice.unittests.customer;

import com.example.practice.models.Customer;
import com.example.practice.repositories.CustomerRepository;
import com.example.practice.services.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerServiceTests {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCustomerById() {
        Customer customer = new Customer();
        Long customerId = customer.getId();
        customer.setName("Имя покупателя");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(customerId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(customerId, result.getId());
        Assertions.assertEquals("Имя покупателя", result.getName());

        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    public void testSaveCustomer() {
        Customer customer = new Customer();
        customer.setName("Имя покупателя");

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.saveCustomer(customer);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Имя покупателя", result.getName());

        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testDeleteCustomer() {
        Long customerId = 100L;

        boolean result = customerService.deleteCustomer(customerId);

        Assertions.assertFalse(result);

        verify(customerRepository, times(1)).deleteById(customerId);
    }
}
