package com.example.practice.controllers;

import com.example.practice.models.Customer;
import com.example.practice.models.Order;
import com.example.practice.models.Product;
import com.example.practice.services.OrderService;
import com.example.practice.services.ProductService;
import com.example.practice.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final ProductService productService;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public OrderController(OrderService orderService, CustomerService customerService, ProductService productService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @Operation(summary = "Get an order by ID")
    @ApiResponse(responseCode = "200", description = "Order found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Order.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"customerId\": 1, \"productId\": 1, \"quantity\": 2}")
            )
    )
    @ApiResponse(responseCode = "404", description = "Order not found")
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@Parameter(description = "ID of the order") @PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            log.info("Order with ID: '{}' found", id);
            return ResponseEntity.ok().body(order);
        } else {
            log.info("Order with ID: '{}' not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class),
                            examples = @ExampleObject(value = "{\n"
                                    + "  \"id\": 1,\n"
                                    + "  \"amount\": 1,\n"
                                    + "  \"product\": {\n"
                                    + "    \"id\": 1,\n"
                                    + "    \"name\": \"Test\",\n"
                                    + "    \"quantity\": 5,\n"
                                    + "    \"cost\": 100\n"
                                    + "  },\n"
                                    + "  \"customer\": {\n"
                                    + "    \"id\": 1,\n"
                                    + "    \"name\": \"User\",\n"
                                    + "    \"email\": \"test123@gmail.com\",\n"
                                    + "    \"phone\": \"88005553535\"\n"
                                    + "  },\n"
                                    + "  \"productId\": 1,\n"
                                    + "  \"customerId\": 1\n"
                                    + "}")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Order not created")
    })
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Long customerId = order.getCustomerId();
        Long productId = order.getProductId();
        int quantity = order.getAmount();
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            log.info("Order not created. Customer with id:'{}' doesn't exist", customerId);
            return ResponseEntity.notFound().build();
        }
        Product product = productService.getProductById(productId);
        if (product == null) {
            log.info("Order not created. Product with id:'{}' doesn't exist", productId);
            return ResponseEntity.notFound().build();
        }
        if (product.getQuantity() < quantity) {
            log.info("Order not created. The amount of product is less than required", productId);
            return ResponseEntity.notFound().build();
        }
        Order createdOrder = orderService.saveOrder(order);
        log.info("Order created:{}", productId);
        log.info("Customer ID: {}", order.getCustomerId());
        log.info("Product ID: {}", order.getProductId());
        log.info("Quantity: {}", order.getAmount());

        product.setQuantity(product.getQuantity() - quantity);
        productService.saveProduct(product);
        log.info("The amount of the remaining product has been changed.Current product number:'{}' ", order.getProduct().getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @Operation(summary = "Delete an order by ID")
    @ApiResponse(responseCode = "204", description = "Order deleted")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@Parameter(description = "ID of the order") @PathVariable Long id) {
        boolean deleted = orderService.deleteOrder(id);
        if (!deleted) {
            log.info("Order with id:'{}' deleted", id);
            return ResponseEntity.noContent().build();
        } else {
            log.info("Order with id:'{}' not deleted. Order not found", id);
            return ResponseEntity.notFound().build();
        }
    }
}
