package com.example.practice.controllers;

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
    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Get a customer by ID")
    @ApiResponse(responseCode = "200", description = "Customer found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Customer.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Test John \", \"email\": \"john.test@gmail.com\", \"phone\": \"88005553535\"}")
            )
    )
    @ApiResponse(responseCode = "404", description = "Customer not found")
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@Parameter(description = "ID of the customer") @PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            LOG.info("Customer with ID: '{}' found", id);
            return ResponseEntity.ok().body(customer);
        } else {
            LOG.info("Customer with ID: '{}' not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new customer")
    @ApiResponse(responseCode = "201", description = "Customer created",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Customer.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Test John \", \"email\": \"john.test@gmail.com\", \"phone\": \"88005553535\"}")
            )
    )
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        customerService.saveCustomer(customer);
        if (customer != null) {
            LOG.info("Customer created:{}", customer.getName());
            LOG.info("Customer created:{}", customer.getId());
            return ResponseEntity.ok().body(customer);
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
