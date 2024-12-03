package br.edu.toycenter.controller.exceptions;

import br.edu.toycenter.service.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;

@ControllerAdvice
public class ControllerWebExceptionHandler {

    @ExceptionHandler(InvalidFormatException.class)
    public String invalidFormat(InvalidFormatException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        if (request.getRequestURI().equals("/client/insertClient")) return "redirect:/client/signup";
        return "redirect:" + request.getRequestURI();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String resourceNotFound(ResourceNotFoundException e, HttpServletRequest request, Model model) {
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("message", "Resource not found.");
        model.addAttribute("description", e.getMessage());
        model.addAttribute("timestamp", Instant.now());
        model.addAttribute("path", request.getRequestURI());
        return "error/error";
    }

    @ExceptionHandler(DatabaseException.class)
    public String database(DatabaseException e, HttpServletRequest request, Model model) {
        model.addAttribute("status", HttpStatus.FORBIDDEN.value());
        model.addAttribute("message", "Operation not allowed.");
        model.addAttribute("description", e.getMessage());
        model.addAttribute("timestamp", Instant.now());
        model.addAttribute("path", request.getRequestURI());
        return "error/error";
    }

    @ExceptionHandler(InternalErrorException.class)
    public String internalError(InternalErrorException e, HttpServletRequest request, Model model) {
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("message", "An internal error occurred.");
        model.addAttribute("description", e.getMessage());
        model.addAttribute("timestamp", Instant.now());
        model.addAttribute("path", request.getRequestURI());
        return "error/error";
    }
}