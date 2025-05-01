package co.edu.uceva.usuarioservice.domain.exception;

public class UsuarioNoEncontradoException extends RuntimeException {
    private final Long usuarioId;

    public UsuarioNoEncontradoException(Long usuarioId) {
        super("No se encontró el usuario con ID: " + usuarioId);
        this.usuarioId = usuarioId;
    }

    public Long getUsuarioId() {
        return this.usuarioId;
    }
}