package com.example.practice.controllers;

import com.example.practice.dto.OrderDTO;
import com.example.practice.dto.OrderMapper;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final ProductService productService;
    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public OrderController(OrderService orderService, CustomerService customerService, ProductService productService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @Operation(summary = "Get an order by ID")
    @ApiResponse(responseCode = "200", description = "Order found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderDTO.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"customerId\": 1, \"productId\": 1, \"quantity\": 2}")
            )
    )
    @ApiResponse(responseCode = "404", description = "Order not found")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@Parameter(description = "ID of the order") @PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            LOG.info("Order with ID: '{}' found", id);
            OrderDTO orderDTO = OrderMapper.INSTANCE.orderToDTO(order);
            return ResponseEntity.ok().body(orderDTO);
        } else {
            LOG.info("Order with ID: '{}' not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class),
                            examples = @ExampleObject(value = "{\n"
                                    + "  \"id\": 1,\n"
                                    + "  \"amount\": 1,\n"
                                    + "  \"productId\": 1,\n"
                                    + "  \"customerId\": 1\n"
                                    + "}")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Order not created")
    })
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        Long customerId = orderDTO.getCustomerId();
        Long productId = orderDTO.getProductId();
        int quantity = orderDTO.getAmount();
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            LOG.info("Order not created. Customer with id:'{}' doesn't exist", customerId);
            return ResponseEntity.notFound().build();
        }
        Product product = productService.getProductById(productId);
        if (product == null) {
            LOG.info("Order not created. Product with id:'{}' doesn't exist", productId);
            return ResponseEntity.notFound().build();
        }
        if (product.getQuantity() < quantity) {
            LOG.info("Order not created. The amount of product is less than required", productId);
            return ResponseEntity.notFound().build();
        }
        Order order = OrderMapper.INSTANCE.dtoToOrder(orderDTO);
        Order createdOrder = orderService.saveOrder(order);
        LOG.info("Order created: {}", order.getId());

        product.setQuantity(product.getQuantity() - quantity);
        productService.saveProduct(product);
        LOG.info("The amount of the remaining product has been changed. Current product number: {}", order.getProduct().getQuantity());

        OrderDTO createdOrderDTO = OrderMapper.INSTANCE.orderToDTO(createdOrder);
        return ResponseEntity.ok().body(createdOrderDTO);
    }

    @Operation(summary = "Delete an order by ID")
    @ApiResponse(responseCode = "204", description = "Order deleted")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@Parameter(description = "ID of the order") @PathVariable Long id) {
        boolean deleted = orderService.deleteOrder(id);
        if (!deleted) {
            LOG.info("Order with ID: '{}' deleted", id);
            return ResponseEntity.noContent().build();
        } else {
            LOG.info("Order with ID: '{}' not deleted", id);
            return ResponseEntity.notFound().build();
        }
    }
}