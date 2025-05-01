package co.edu.uceva.usuarioservice.domain.exception;

public class CorreoExistenteException extends RuntimeException {
    private final String correo;

    public CorreoExistenteException(String correo) {
        super("El correo '" + correo + "' ya está registrado");
        this.correo = correo;
    }

    public String getCorreo() {
        return this.correo;
    }
}
