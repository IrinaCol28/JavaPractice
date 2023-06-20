package com.example.practice.controllers;

import com.example.practice.models.Customer;
import com.example.practice.services.CustomerService;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
            return ResponseEntity.ok().body(customer);
        } else {
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
        Customer createdCustomer = customerService.saveCustomer(customer);
        return ResponseEntity.status(201).body(createdCustomer);
    }

    @Operation(summary = "Delete a customer by ID")
    @ApiResponse(responseCode = "204", description = "Customer deleted")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@Parameter(description = "ID of the customer") @PathVariable Long id) {
        boolean deleted = customerService.deleteCustomer(id);
        if (!deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

//    @GetMapping
//    public Customer createCustomer() {
//        Customer product=new Customer();
//        product.setName("User");
//        product.setEmail("test123@gmail.com");
//        product.setPhone("88005553535");
//        return customerService.saveCustomer(product);
//    }