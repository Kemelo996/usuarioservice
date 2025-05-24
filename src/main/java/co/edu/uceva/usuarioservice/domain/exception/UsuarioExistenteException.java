package co.edu.uceva.usuarioservice.domain.exception;

public class UsuarioExistenteException extends RuntimeException {
    public UsuarioExistenteException(String nombre) {
        super("El usuario con cedula " + nombre + " ya existe.");
    }
}
