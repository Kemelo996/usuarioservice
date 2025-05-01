package co.edu.uceva.usuarioservice.delivery.rest;

import co.edu.uceva.usuarioservice.domain.exception.*;
import co.edu.uceva.usuarioservice.domain.model.Usuario;
import co.edu.uceva.usuarioservice.domain.service.IUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuario-service")
public class UsuarioRestController {

    private final IUsuarioService usuarioService;

    private static final String MENSAJE = "mensaje";
    private static final String USUARIO = "usuario";
    private static final String USUARIOS = "usuarios";

    public UsuarioRestController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    @RestController
    @RequestMapping("/api/test")
    public class TestController {

        @Autowired
        private DataSource dataSource;

        @GetMapping("/db")
        public ResponseEntity<String> testDatabase() {
            try (Connection conn = dataSource.getConnection()) {
                return ResponseEntity.ok("Conexión exitosa a: " + conn.getMetaData().getURL());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error de conexión: " + e.getMessage());
            }
        }
    }

    @GetMapping("/usuarios")
    public ResponseEntity<Map<String, Object>> getUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();
        if (usuarios.isEmpty()) {
            throw new NoHayUsuariosException();
        }
        Map<String, Object> response = new HashMap<>();
        response.put(USUARIOS, usuarios);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/page/{page}")
    public ResponseEntity<?> getUsuariosPaginated(@PathVariable Integer page) {
        try {
            if (page < 0) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "El número de página no puede ser negativo",
                        "pagina_solicitada", page
                ));
            }

            Pageable pageable = PageRequest.of(page, 4); // 4 usuarios por página
            Page<Usuario> usuarios = usuarioService.findAll(pageable);

            return ResponseEntity.ok(Map.of(
                    "pagina_actual", page,
                    "total_paginas", usuarios.getTotalPages(),
                    "total_usuarios", usuarios.getTotalElements(),
                    "usuarios", usuarios.getContent()
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Error al recuperar usuarios",
                    "mensaje", e.getMessage()
            ));
        }
    }

    @PostMapping("/usuarios")
    public ResponseEntity<Map<String, Object>> save(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }

        // Verificar si el correo ya existe
        if (usuarioService.existsByCorreo(usuario.getCorreo())) {
            throw new CorreoExistenteException(usuario.getCorreo());
        }

        Map<String, Object> response = new HashMap<>();
        Usuario nuevoUsuario = usuarioService.save(usuario);
        response.put(MENSAJE, "El usuario ha sido creado con éxito!");
        response.put(USUARIO, nuevoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/usuarios")
    public ResponseEntity<Map<String, Object>> delete(@RequestBody Usuario usuario) {
        usuarioService.findById(usuario.getId())
                .orElseThrow(() -> new UsuarioNoEncontradoException(usuario.getId()));
        usuarioService.delete(usuario);
        Map<String, Object> response = new HashMap<>();
        response.put(MENSAJE, "El usuario ha sido eliminado con éxito!");
        response.put(USUARIO, null);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/usuarios")
    public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        usuarioService.findById(usuario.getId())
                .orElseThrow(() -> new UsuarioNoEncontradoException(usuario.getId()));

        // Validar que el correo no esté siendo usado por otro usuario
        Usuario usuarioExistente = usuarioService.findByCorreo(usuario.getCorreo())
                .orElse(null);
        if (usuarioExistente != null && !usuarioExistente.getId().equals(usuario.getId())) {
            throw new CorreoExistenteException(usuario.getCorreo());
        }

        Map<String, Object> response = new HashMap<>();
        Usuario usuarioActualizado = usuarioService.update(usuario);
        response.put(MENSAJE, "El usuario ha sido actualizado con éxito!");
        response.put(USUARIO, usuarioActualizado);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
        Map<String, Object> response = new HashMap<>();
        response.put(MENSAJE, "El usuario ha sido encontrado con éxito!");
        response.put(USUARIO, usuario);
        return ResponseEntity.ok(response);
    }
}