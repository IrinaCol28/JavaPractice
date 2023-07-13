package com.example.practice.dto;

import com.example.practice.models.Customer;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerMapperTest {
    private final CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);

    @Test
    public void testCustomerToDTO() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhone("1234567890");

        CustomerDTO customerDTO = customerMapper.customerToDTO(customer);

        assertEquals(customer.getId(), customerDTO.getId());
        assertEquals(customer.getName(), customerDTO.getName());
        assertEquals(customer.getEmail(), customerDTO.getEmail());
        assertEquals(customer.getPhone(), customerDTO.getPhone());
    }

    @Test
    public void testDTOToCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("John Doe");
        customerDTO.setEmail("john.doe@example.com");
        customerDTO.setPhone("1234567890");

        Customer customer = customerMapper.dtoToCustomer(customerDTO);

        assertEquals(customerDTO.getId(), customer.getId());
        assertEquals(customerDTO.getName(), customer.getName());
        assertEquals(customerDTO.getEmail(), customer.getEmail());
        assertEquals(customerDTO.getPhone(), customer.getPhone());
    }
}
