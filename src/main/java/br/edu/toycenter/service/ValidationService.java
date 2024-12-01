package br.edu.toycenter.service;

import br.edu.toycenter.service.exceptions.InvalidFormatException;
import br.edu.toycenter.model.entities.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ValidationService {

    public void checkFields(Administrator administrator) throws InvalidFormatException {
        if (administrator == null) throw new InvalidFormatException("Os campos não podem ser nulos");

        isNullOrBlank(administrator.getName(), "name");
        if (!administrator.getName().matches("^[a-zA-Z ]+$"))
            throw new InvalidFormatException("Name", administrator.getName());

        isNullOrBlank(administrator.getEmail(), "e-mail");
        if (!administrator.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
            throw new InvalidFormatException("E-mail", administrator.getEmail());

        isNullOrBlank(administrator.getPassword(), "password");
        if (!(administrator.getPassword().length() >= 8))
            throw new InvalidFormatException("Password");
    }

    public void checkFields(Category category) throws InvalidFormatException {
        if (category == null) throw new InvalidFormatException("The fields can not be null.");

        isNullOrBlank(category.getName(), "name");
        isNullOrBlank(category.getImage());

    }

    public void checkFields(Client client) throws InvalidFormatException {
        if (client == null) throw new InvalidFormatException("Os campos não podem ser nulos");

        isNullOrBlank(client.getCpf(), "CPF");
        if (!client.getCpf().matches("^[0-9]{3}.[0-9]{3}.[0-9]{3}-[0-9]{2}$"))
            throw new InvalidFormatException("CPF", client.getCpf());

        isNullOrBlank(client.getName(), "name");
        if (!client.getName().matches("^[a-zA-Z ]+$"))
            throw new InvalidFormatException("Name", client.getName());

        isNullOrBlank(client.getEmail(), "e-mail");
        if (!client.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
            throw new InvalidFormatException("E-mail", client.getEmail());

        isNullOrBlank(client.getPhone(), "phone");
        if (!client.getPhone().matches("\\(?\\d{2}\\)? ?(?:\\d{4,5}-?\\d{4}|\\d{4}-?\\d{4})$"))
            throw new InvalidFormatException("Phone", client.getPhone());

        isNullOrBlank(client.getPassword(), "password");
        if (!(client.getPassword().length() >= 8))
            throw new InvalidFormatException("Password");
    }

    public void checkFields(Order order) throws InvalidFormatException {
        if (order == null) throw new InvalidFormatException("The fields can not be null.");

        isNullOrBlank(order.getMoment());
        isNullOrBlank(order.getClientId());

        for (OrderItem oi : order.getOrderItens()) {
            isNullOrBlank(oi.getQuantity());
            isNullOrBlank(oi.getPrice());
            isNullOrBlank(oi.getProduct());
        }
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

    private void isNullOrBlank(String string) throws InvalidFormatException {
        if (string == null || string.isBlank())
            throw new InvalidFormatException("This client is not valid.");
    }

    private void isNullOrBlank(Integer interger) throws InvalidFormatException {
        if (interger == null || interger <= 0)
            throw new InvalidFormatException("This quantity is not valid.");
    }

    private void isNullOrBlank(Instant moment) throws InvalidFormatException {
        if (moment == null)
            throw new InvalidFormatException("This time is not valid.");
    }

    private void isNullOrBlank(Product product) throws InvalidFormatException {
        if (product == null)
            throw new InvalidFormatException("This product is not valid.");
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
