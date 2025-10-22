package com.gokhancomert.b2bapplication.service;

import com.gokhancomert.b2bapplication.dto.ProductDto;
import com.gokhancomert.b2bapplication.dto.request.ProductCreateRequest;
import com.gokhancomert.b2bapplication.dto.request.ProductUpdateRequest;
import com.gokhancomert.b2bapplication.exception.ResourceNotFoundException;
import com.gokhancomert.b2bapplication.mapper.ProductMapper;
import com.gokhancomert.b2bapplication.model.Category;
import com.gokhancomert.b2bapplication.model.Product;
import com.gokhancomert.b2bapplication.repository.CategoryRepository;
import com.gokhancomert.b2bapplication.repository.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    public Page<ProductDto> searchProducts(String name, Long categoryId, Pageable pageable) {
        logger.info("Searching for products with name containing '{}' and categoryId '{}'", name, categoryId);

        Specification<Product> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.trim().toLowerCase() + "%"));
            }

            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Product> products = productRepository.findAll(specification, pageable);
        logger.info("Found {} products matching criteria.", products.getTotalElements());
        return products.map(productMapper::toDto);
    }

    public ProductDto createProduct(ProductCreateRequest productCreateRequest) {
        logger.info("Attempting to create a new product with name: '{}'", productCreateRequest.getName());
        Category category = categoryRepository.findById(productCreateRequest.getCategoryId())
                .orElseThrow(() -> {
                    logger.warn("Category not found with id: '{}'. Cannot create product.", productCreateRequest.getCategoryId());
                    return new ResourceNotFoundException("Category not found with id: " + productCreateRequest.getCategoryId());
                });

        Product product = productMapper.toProduct(productCreateRequest);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        logger.info("Successfully created product with id: '{}' and name: '{}'", savedProduct.getId(), savedProduct.getName());
        return productMapper.toDto(savedProduct);
    }

    public ProductDto updateProduct(Long id, ProductUpdateRequest productUpdateRequest) {
        logger.info("Attempting to update product with id: '{}'", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Update failed. Product not found with id: '{}'", id);
                    return new ResourceNotFoundException("Product not found with id: " + id);
                });

        Category category = categoryRepository.findById(productUpdateRequest.getCategoryId())
                .orElseThrow(() -> {
                    logger.warn("Update failed. Category not found with id: '{}'" ,productUpdateRequest.getCategoryId());
                    return new ResourceNotFoundException("Category not found with id: " + productUpdateRequest.getCategoryId());
                });

        productMapper.updateProductFromDto(productUpdateRequest, product);
        product.setCategory(category);

        Product updateProduct = productRepository.save(product);
        logger.info("Successfully updated product with id: {}", id);
        return productMapper.toDto(updateProduct);
    }

    public void deleteProductById(Long id) {
        logger.info("Attempting to delete product with id: {}", id);
        if (!productRepository.existsById(id)) {
            logger.warn("Delete failed. Product not found with id: '{}'" ,id);
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
        logger.info("Successfully deleted product with id: {}", id);
    }

    public ProductDto findByProductId(Long id) {
        logger.info("Attempting to find product with id: '{}'", id);
        return productRepository.findById(id)
                .map(product -> {
                    logger.info("Successfully found product with id: '{}'", id);
                    return productMapper.toDto(product);
                })
                .orElseThrow(() -> {
                    logger.warn("Product not found with id: '{}'", id);
                    return new ResourceNotFoundException("Product not found with id: " + id);
                });
    }
}
