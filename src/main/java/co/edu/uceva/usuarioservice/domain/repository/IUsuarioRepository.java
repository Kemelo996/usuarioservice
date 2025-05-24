package co.edu.uceva.usuarioservice.domain.repository;

import co.edu.uceva.usuarioservice.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
}
