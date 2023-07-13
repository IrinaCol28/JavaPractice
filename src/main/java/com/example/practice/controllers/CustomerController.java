package com.example.practice.controllers;

import com.example.practice.dto.CustomerDTO;
import com.example.practice.dto.CustomerMapper;
import com.example.practice.models.Customer;
import com.example.practice.services.CustomerService;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;
    private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

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
            LOG.info("Customer with ID: '{}' found", id);
            CustomerDTO customerDTO = CustomerMapper.INSTANCE.customerToDTO(customer);
            return ResponseEntity.ok().body(customerDTO);
        } else {
            LOG.info("Customer with ID: '{}' not found", id);
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
            LOG.info("Customer created: {}", customer.getName());
            LOG.info("Customer ID: {}", customer.getId());
            CustomerDTO createdCustomerDTO = CustomerMapper.INSTANCE.customerToDTO(customer);
            return ResponseEntity.ok().body(createdCustomerDTO);
        } else {
            LOG.info("Customer not created");
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
            LOG.info("Customer with ID: '{}' deleted", id);
            return ResponseEntity.noContent().build();
        } else {
            LOG.info("Customer with ID: '{}' not deleted", id);
            return ResponseEntity.notFound().build();
        }
    }
}
