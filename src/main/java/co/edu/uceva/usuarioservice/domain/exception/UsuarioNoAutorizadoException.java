package co.edu.uceva.usuarioservice.domain.exception;

public class UsuarioNoAutorizadoException extends RuntimeException {
    public UsuarioNoAutorizadoException(String mensaje) {
        super(mensaje);
    }
}