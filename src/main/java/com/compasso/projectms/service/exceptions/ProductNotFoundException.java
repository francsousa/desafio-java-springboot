package com.compasso.projectms.service.exceptions;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super (message);
    }
}
