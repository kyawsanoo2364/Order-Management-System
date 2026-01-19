package com.vodica.order_system.service;

import com.vodica.order_system.dto.ProductDTO;
import com.vodica.order_system.entity.Product;
import com.vodica.order_system.exceptions.ResourceNotFoundException;
import com.vodica.order_system.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        product = productRepository.save(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Product not found with id " + id)
                );
        return modelMapper.map(product, ProductDTO.class);
    }

    public Page<ProductDTO> getProductAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(product->
                modelMapper.map(product, ProductDTO.class)
                );
    }

    public ProductDTO updateProduct(ProductDTO productDTO, Long id) {
         Product existsProduct =productRepository.findById(id).orElseThrow(()->
                new  ResourceNotFoundException("Product not found with id " + id)
                );

            existsProduct.setName(productDTO.getName());
            existsProduct.setPrice(productDTO.getPrice());
            existsProduct.setDescription(productDTO.getDescription());
            existsProduct.setStock(productDTO.getStock());

            Product updatedProduct = productRepository.save(existsProduct);
            return  modelMapper.map(updatedProduct, ProductDTO.class);

    }

    public ProductDTO deleteProduct(Long id) {
        Product existsProduct = productRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Product not found with id " + id));
        productRepository.delete(existsProduct);
        return modelMapper.map(existsProduct, ProductDTO.class);
    }
}
