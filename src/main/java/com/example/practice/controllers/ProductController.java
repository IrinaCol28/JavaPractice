package com.example.practice.controllers;
import com.example.practice.models.Product;
import com.example.practice.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get a product by ID")
    @ApiResponse(responseCode = "200", description = "Product found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Test\", \"cost\": 100, \"quantity\": 5}")
            )
    )
    @ApiResponse(responseCode = "404", description = "Product not found")
    @GetMapping("/{id}")
    public Product getProduct(@Parameter(description = "ID of the product") @PathVariable Long id) {
        return productService.getProductById(id);
    }

    @Operation(summary = "Get all products")
    @ApiResponse(responseCode = "200", description = "List of products",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class),
                    examples = @ExampleObject(value = "[{\"id\": 1, \"name\": \"Test\", \"cost\": 100, \"quantity\": 5}, {\"id\": 2, \"name\": \"Food\", \"cost\": 50, \"quantity\": 1}]")
            )
    )
    @ApiResponse(responseCode = "404", description = "Products not found")
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok().body(products);
    }

    @Operation(summary = "Create a new product")
    @ApiResponse(responseCode = "201", description = "Product created",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"NewProduct\", \"cost\": 100, \"quantity\": 5}")
            )
    )
    @ApiResponse(responseCode = "404", description = "Product not created")
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @Operation(summary = "Delete a product by ID")
    @ApiResponse(responseCode = "204", description = "Product deleted")
    @ApiResponse(responseCode = "404", description = "Product not deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@Parameter(description = "ID of the product") @PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (!deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update product quantity")
    @ApiResponse(responseCode = "200", description = "Product quantity updated",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Test\", \"cost\": 100, \"quantity\": 10}")
            )
    )
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PutMapping("/{id}/quantity")
    public ResponseEntity<Product> updateProductQuantity(
            @Parameter(description = "ID of the product") @PathVariable Long id,
            @Parameter(description = "New quantity value") @RequestParam int quantity) {
        Product product = productService.updateProductQuantity(id, quantity);
        if (product != null) {
            return ResponseEntity.ok().body(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
//    @GetMapping
//    public Product createProduct( ){
//    Product product=new Product();
//        product.setName("Test");
//        product.setCost(100);
//        product.setQuantity(5);
//        return productService.saveProduct(product);
//    }