package com.vodica.order_system.controller;

import com.vodica.order_system.dto.PageableDTO;
import com.vodica.order_system.dto.ProductDTO;
import com.vodica.order_system.reponse.ApiResponse;
import com.vodica.order_system.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final ProductService productService;

    //get product all
    @GetMapping("/lists")
    public ResponseEntity<PageableDTO<ProductDTO>> getAllProducts(@PageableDefault(size = 10) Pageable pageable){
            Page<ProductDTO> productPage = productService.getProductAll(pageable);
            return new ResponseEntity<>(
                    new PageableDTO<>(productPage),
                    HttpStatus.OK
            );
    }

    // get product id
    @GetMapping("/{id}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id){
        ProductDTO productDTO = productService.getProductById(id);
        return new ResponseEntity<>(
                new ApiResponse("success","Get Product Successfully",productDTO)
                , HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductDTO productDTO){
        ProductDTO product = productService.createProduct(productDTO);
        return new ResponseEntity<>(
                new ApiResponse("created","Create Product Successfully",product),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO){
        ProductDTO product = productService.updateProduct(productDTO, id);
        return new ResponseEntity<>(
                new ApiResponse("success","Update Product successfully",product),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id){
        ProductDTO product = productService.deleteProduct(id);
        return new ResponseEntity<>(
                new ApiResponse("success","Delete Product Successfully",product),
                HttpStatus.OK
        );
    }
}
