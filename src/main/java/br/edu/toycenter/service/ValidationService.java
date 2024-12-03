package br.edu.toycenter.service;

import br.edu.toycenter.service.exceptions.InvalidFormatException;
import br.edu.toycenter.model.entities.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidationService {


    public void checkFields(Category category) throws InvalidFormatException {
        if (category == null) throw new InvalidFormatException("The fields can not be null.");

        isNullOrBlank(category.getName(), "name");
        isNullOrBlank(category.getImage(), "image");

    }

    public void checkFields(Product product) throws InvalidFormatException {
        if (product == null) throw new InvalidFormatException("The fields can not be null.");

        isNullOrBlank(product.getName(), "name");
        isNullOrBlank(product.getImage(), "image");
        isNullOrBlank(product.getBrand(), "brand");
        isNullOrBlank(product.getPrice());
        isNullOrBlank(product.getDescription(), "description");
        isNullOrBlank(product.getDetails(), "details");
        isNullOrBlank(product.getCategoriesId());

    }

    private void isNullOrBlank(String string, String field) throws InvalidFormatException {
        if (string == null || string.isBlank())
            throw new InvalidFormatException("The " + field + " can not be null.");
    }

    private void isNullOrBlank(Double doub) throws InvalidFormatException {
        if (doub == null || doub <= 0f)
            throw new InvalidFormatException("This price is not valid.");
    }

    private void isNullOrBlank(List<String> list) throws InvalidFormatException {
        if (list == null || list.size() <= 0)
            throw new InvalidFormatException("The categories can not be null.");
    }
}
