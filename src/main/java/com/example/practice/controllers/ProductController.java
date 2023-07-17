package com.example.practice.controllers;

import com.example.practice.dto.ProductDTO;
import com.example.practice.dto.ProductMapper;
import com.example.practice.models.Product;
import com.example.practice.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
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
                    schema = @Schema(implementation = ProductDTO.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Test\", \"cost\": 100, \"quantity\": 5}")
            )
    )
    @ApiResponse(responseCode = "404", description = "Product not found")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@Parameter(description = "ID of the product") @PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            log.info("Product with ID: '{}' found", id);
            ProductDTO productDTO = ProductMapper.INSTANCE.productToDTO(product);
            return ResponseEntity.ok().body(productDTO);
        } else {
            log.info("Product with ID: '{}' not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get all products")
    @ApiResponse(responseCode = "200", description = "List of products",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductDTO.class),
                    examples = @ExampleObject(value = "[{\"id\": 1, \"name\": \"Test\", \"cost\": 100, \"quantity\": 5}, {\"id\": 2, \"name\": \"Food\", \"cost\": 50, \"quantity\": 1}]")
            )
    )
    @ApiResponse(responseCode = "404", description = "Products not found")
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (products != null) {
            log.info("Products found");
            List<ProductDTO> productDTOs = ProductMapper.INSTANCE.productsToDTOs(products);
            return ResponseEntity.ok().body(productDTOs);
        } else {
            log.info("Products not found");
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new product")
    @ApiResponse(responseCode = "201", description = "Product created",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductDTO.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"NewProduct\", \"cost\": 100, \"quantity\": 5}")
            )
    )
    @ApiResponse(responseCode = "404", description = "Product not created")
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        Product product = ProductMapper.INSTANCE.dtoToProduct(productDTO);
        productService.saveProduct(product);
        if (product != null) {
           log.info("Product created: {}", product.getName());
            log.info("Product ID: {}", product.getId());
            ProductDTO createdProductDTO = ProductMapper.INSTANCE.productToDTO(product);
            return ResponseEntity.ok().body(createdProductDTO);
        } else {
            log.info("Product not created");
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a product by ID")
    @ApiResponse(responseCode = "204", description = "Product deleted")
    @ApiResponse(responseCode = "404", description = "Product not deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@Parameter(description = "ID of the product") @PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (!deleted) {
            log.info("Product with ID: '{}' deleted", id);
            return ResponseEntity.noContent().build();
        } else {
            log.info("Product with ID: '{}' not deleted", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update product quantity")
    @ApiResponse(responseCode = "200", description = "Product quantity updated",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductDTO.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Test\", \"cost\": 100, \"quantity\": 10}")
            )
    )
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PutMapping("/{id}/quantity")
    public ResponseEntity<ProductDTO> updateProductQuantity(
            @Parameter(description = "ID of the product") @PathVariable Long id,
            @Parameter(description = "New quantity value") @RequestParam int quantity) {
        Product product = productService.updateProductQuantity(id, quantity);
        if (product != null) {
            log.info("Update product quantity");
            ProductDTO updatedProductDTO = ProductMapper.INSTANCE.productToDTO(product);
            return ResponseEntity.ok().body(updatedProductDTO);
        } else {
           log.info("Update product quantity canceled. Product not found");
            return ResponseEntity.notFound().build();
        }
    }

}
