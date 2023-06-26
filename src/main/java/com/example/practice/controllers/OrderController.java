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
            return ResponseEntity.ok().body(order);
        } else {
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

        // Покупатель не существует
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        // Продукт не существует
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        // Хватит ли продукта
        if (product.getQuantity() < quantity) {
            return ResponseEntity.notFound().build();
        }

        // Создаем заказ
        Order createdOrder = orderService.saveOrder(order);

        // Меняем количество оставшегося проукта
        product.setQuantity(product.getQuantity() - quantity);
        productService.saveProduct(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @Operation(summary = "Delete an order by ID")
    @ApiResponse(responseCode = "204", description = "Order deleted")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@Parameter(description = "ID of the order") @PathVariable Long id) {
        boolean deleted = orderService.deleteOrder(id);
        if (!deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}


//    @GetMapping
//    public Order createOrder( ) {
//        Order order=new Order();
//        order.setProduct(productService.getProductById(Long.valueOf(1)));
//        order.setCustomer(customerService.getCustomerById(Long.valueOf(1)));
//order.setAmount(10);
//        return orderService.saveOrder(order);
//    }