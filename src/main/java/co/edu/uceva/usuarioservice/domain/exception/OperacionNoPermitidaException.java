package co.edu.uceva.usuarioservice.domain.exception;

public class OperacionNoPermitidaException extends RuntimeException {
    private final String detalles;

    public OperacionNoPermitidaException(String mensaje, String detalles) {
        super(mensaje);
        this.detalles = detalles;
    }

    public String getDetalles() {
        return detalles;
    }

}