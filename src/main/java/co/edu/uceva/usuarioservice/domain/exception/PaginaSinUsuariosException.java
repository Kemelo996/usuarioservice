package co.edu.uceva.usuarioservice.domain.exception;

public class PaginaSinUsuariosException extends RuntimeException {
    private final int page;

    public PaginaSinUsuariosException(int page) {
        super("No se encontraron usuarios en la página " + page);
        this.page = page;
    }

    public int getPage() {
        return this.page;
    }
}