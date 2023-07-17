package com.example.practice.controllers;

import com.example.practice.dto.OrderDTO;
import com.example.practice.dto.OrderMapper;
import com.example.practice.models.Order;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final ProductService productService;

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
            log.info("Order with ID: '{}' found", id);
            OrderDTO orderDTO = OrderMapper.INSTANCE.orderToDTO(order);
            return ResponseEntity.ok().body(orderDTO);
        } else {
            log.info("Order with ID: '{}' not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Заказ поставлен в очередь")
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
   @PostMapping("/place")
    public ResponseEntity<String> placeOrder() {
        log.info("Заказ поставлен в очередь");
    return ResponseEntity.status(HttpStatus.CREATED).body("Заказ поставлен в очередь.");
    }

    @Operation(summary = "Delete an order by ID")
    @ApiResponse(responseCode = "204", description = "Order deleted")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@Parameter(description = "ID of the order") @PathVariable Long id) {
        boolean deleted = orderService.deleteOrder(id);
        if (!deleted) {
            log.info("Order with ID: '{}' deleted", id);
            return ResponseEntity.noContent().build();
        } else {
            log.info("Order with ID: '{}' not deleted", id);
            return ResponseEntity.notFound().build();
        }
    }
}