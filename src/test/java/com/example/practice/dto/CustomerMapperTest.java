package com.example.practice.dto;

import com.example.practice.models.Customer;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerMapperTest {
    private final CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);

    @Test
   void testCustomerToDTO() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .build();

        CustomerDTO customerDTO = customerMapper.customerToDTO(customer);

        assertEquals(customer.getId(), customerDTO.getId());
        assertEquals(customer.getName(), customerDTO.getName());
        assertEquals(customer.getEmail(), customerDTO.getEmail());
        assertEquals(customer.getPhone(), customerDTO.getPhone());
    }

    @Test
     void testDTOToCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .build();

        Customer customer = customerMapper.dtoToCustomer(customerDTO);

        assertEquals(customerDTO.getId(), customer.getId());
        assertEquals(customerDTO.getName(), customer.getName());
        assertEquals(customerDTO.getEmail(), customer.getEmail());
        assertEquals(customerDTO.getPhone(), customer.getPhone());
    }
}
