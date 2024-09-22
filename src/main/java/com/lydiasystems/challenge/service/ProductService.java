package com.lydiasystems.challenge.service;

import com.lydiasystems.challenge.entity.Product;
import com.lydiasystems.challenge.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Couldn't find product id : " + id));
        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setStock(updatedProduct.getStock());
        product.setPrice(updatedProduct.getPrice());
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> listProducts() {
        return productRepository.findAllByOrderByIdAsc();
    }
}
