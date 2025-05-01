package co.edu.uceva.usuarioservice.delivery.exception;

import co.edu.uceva.usuarioservice.domain.exception.*;
import co.edu.uceva.usuarioservice.domain.model.Usuario;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GlobalExceptionHandler {
    private static final String ERROR = "error";
    private static final String ERRORES = "errores";
    private static final String MENSAJE = "mensaje";
    private static final String USUARIO = "usuario";
    private static final String USUARIOS = "usuarios";
    private static final String STATUS = "status";
    private static final String DETALLES = "detalles";

    @ExceptionHandler(NoHayUsuariosException.class)
    public ResponseEntity<Map<String, Object>> handleNoHayUsuarios() {
        Map<String, Object> response = new HashMap<>();
        response.put(MENSAJE, "No hay usuarios registrados en el sistema");
        response.put(USUARIOS, List.of());
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(PaginaSinUsuariosException.class)
    public ResponseEntity<Map<String, Object>> handlePaginaSinUsuarios(PaginaSinUsuariosException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(MENSAJE, ex.getMessage());
        response.put("pagina_solicitada", ex.getPage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR, "Usuario no encontrado");
        response.put(MENSAJE, ex.getMessage());
        response.put("usuario_id", ex.getUsuarioId());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(CorreoExistenteException.class)
    public ResponseEntity<Map<String, Object>> handleCorreoExistente(CorreoExistenteException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR, "Conflicto con el correo electrónico");
        response.put(MENSAJE, ex.getMessage());
        response.put("correo", ex.getCorreo()); // Cambiado de getUsuarioId() a getCorreo()
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR, "Error de validación");

        List<String> errores;

        if (ex instanceof ValidationException) {
            ValidationException validationEx = (ValidationException) ex;
            errores = validationEx.getResult().getFieldErrors().stream()
                    .map(this::formatError)
                    .collect(Collectors.toList());
        } else {
            MethodArgumentNotValidException methodEx = (MethodArgumentNotValidException) ex;
            errores = methodEx.getBindingResult().getFieldErrors().stream()
                    .map(this::formatError)
                    .collect(Collectors.toList());
        }

        response.put(ERRORES, errores);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private String formatError(FieldError error) {
        return String.format("Campo '%s': %s", error.getField(), error.getDefaultMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR, "Error en la base de datos");
        response.put(MENSAJE, "Ocurrió un error al acceder a los datos");
        response.put(DETALLES, ex.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR, "Parámetro inválido");
        response.put(MENSAJE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException() {
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR, "Acceso denegado");
        response.put(MENSAJE, "No tienes permisos para realizar esta acción");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(UsuarioNoAutorizadoException.class)
    public ResponseEntity<Map<String, Object>> handleUsuarioNoAutorizado(UsuarioNoAutorizadoException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR, "No autorizado");
        response.put(MENSAJE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(OperacionNoPermitidaException.class)
    public ResponseEntity<Map<String, Object>> handleOperacionNoPermitida(OperacionNoPermitidaException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR, "Operación no permitida");
        response.put(MENSAJE, ex.getMessage());
        response.put(DETALLES, ex.getDetalles());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR, "Error interno del servidor");
        response.put(MENSAJE, "Ocurrió un error inesperado");
        response.put(DETALLES, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
