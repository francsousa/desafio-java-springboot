package com.compasso.projectms.service;

import com.compasso.projectms.entity.Product;
import com.compasso.projectms.repository.ProductRepository;
import com.compasso.projectms.service.exceptions.ProductNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product insert(Product product) {
        product = productRepository.save(product);
        return product;
    }

    @Transactional
    public Product update(String id, Product product) {
        Product obj = consult(id);
        BeanUtils.copyProperties(product, obj, "id");
        return productRepository.save(obj);
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product consult(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public void delete(String id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ProductNotFoundException("Id not found " + id);
        }
    }

    @Transactional(readOnly = true)
    public List<Product> findAllBySpecification(Specification<Product> specification) {
        return productRepository.findAll(specification);
    }
}
