package com.example.practice.unit.customer;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


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
        Customer customer = Customer.builder()
                .name("NewCustomer")
                .email("test@example.com")
                .phone("88005553535")
                .build();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(customer.getId());

        assertNotNull(result);
        assertEquals(customer.getId(), result.getId());
        assertEquals("NewCustomer", result.getName());

        verify(customerRepository, times(1)).findById(customer.getId());
    }

    @Test
    public void testSaveCustomer() {
        Customer customer = Customer.builder()
                .name("NewCustomer")
                .email("test@example.com")
                .phone("88005553535")
                .build();

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.saveCustomer(customer);

        assertNotNull(result);
        assertEquals("NewCustomer", result.getName());

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
