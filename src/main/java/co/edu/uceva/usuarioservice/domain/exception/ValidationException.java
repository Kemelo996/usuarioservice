package co.edu.uceva.usuarioservice.domain.exception;

import org.springframework.validation.BindingResult;


public class ValidationException extends RuntimeException {
    private final BindingResult result;

    public ValidationException(BindingResult result) {
        super("Error de validación en los datos del usuario");
        this.result = result;
    }

    public BindingResult getResult() {
        return this.result;
    }
}