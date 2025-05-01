package co.edu.uceva.usuarioservice.domain.repository;

import co.edu.uceva.usuarioservice.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByCorreo(String correo);

    // Método para buscar un usuario por correo
    Optional<Usuario> findByCorreo(String correo);
}
