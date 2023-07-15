package com.example.practice.controllers;

import com.example.practice.dto.CustomerDTO;
import com.example.practice.dto.CustomerMapper;
import com.example.practice.models.Customer;
import com.example.practice.services.CustomerService;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Get a customer by ID")
    @ApiResponse(responseCode = "200", description = "Customer found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomerDTO.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Test John \", \"email\": \"john.test@gmail.com\", \"phone\": \"88005553535\"}")
            )
    )
    @ApiResponse(responseCode = "404", description = "Customer not found")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@Parameter(description = "ID of the customer") @PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            log.info("Customer with ID: '{}' found", id);
            CustomerDTO customerDTO = CustomerMapper.INSTANCE.customerToDTO(customer);
            return ResponseEntity.ok().body(customerDTO);
        } else {
            log.info("Customer with ID: '{}' not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new customer")
    @ApiResponse(responseCode = "201", description = "Customer created",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomerDTO.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Test John \", \"email\": \"john.test@gmail.com\", \"phone\": \"88005553535\"}")
            )
    )
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = CustomerMapper.INSTANCE.dtoToCustomer(customerDTO);
        customerService.saveCustomer(customer);
        if (customer != null) {
            log.info("Customer created: {}", customer.getName());
            log.info("Customer ID: {}", customer.getId());
            CustomerDTO createdCustomerDTO = CustomerMapper.INSTANCE.customerToDTO(customer);
            return ResponseEntity.ok().body(createdCustomerDTO);
        } else {
            log.info("Customer not created");
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a customer by ID")
    @ApiResponse(responseCode = "204", description = "Customer deleted")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@Parameter(description = "ID of the customer") @PathVariable Long id) {
        boolean deleted = customerService.deleteCustomer(id);
        if (!deleted) {
            log.info("Customer with ID: '{}' deleted", id);
            return ResponseEntity.noContent().build();
        } else {
            log.info("Customer with ID: '{}' not deleted", id);
            return ResponseEntity.notFound().build();
        }
    }
}
