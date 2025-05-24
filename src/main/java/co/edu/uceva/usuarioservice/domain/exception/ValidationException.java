package co.edu.uceva.usuarioservice.domain.exception;

import org.springframework.validation.BindingResult;


public class ValidationException extends RuntimeException {
    public final BindingResult result;

    public ValidationException(BindingResult result) {
        super("Error de validación en los datos del usuario");
        this.result = result;
    }
}