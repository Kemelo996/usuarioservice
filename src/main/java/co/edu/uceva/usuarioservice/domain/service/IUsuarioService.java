package co.edu.uceva.usuarioservice.domain.service;

import co.edu.uceva.usuarioservice.domain.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    Usuario save(Usuario usuario);
    void delete(Usuario usuario);
    Optional<Usuario> findById(Long id);
    Usuario update(Usuario usuario);
    List<Usuario> findAll();
    Page<Usuario> findAll(Pageable pageable);
    boolean existsByCorreo(String correo);
    Optional<Usuario> findByCorreo(String correo);
}