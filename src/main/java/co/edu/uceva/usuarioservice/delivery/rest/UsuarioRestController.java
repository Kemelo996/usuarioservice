package co.edu.uceva.usuarioservice.delivery.rest;

import co.edu.uceva.usuarioservice.domain.exception.*;
import co.edu.uceva.usuarioservice.domain.model.Usuario;
import co.edu.uceva.usuarioservice.domain.service.IUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1/usuario-service")
public class UsuarioRestController {

    private final IUsuarioService usuarioService;

    private static final String MENSAJE = "mensaje";
    private static final String USUARIO = "usuario";
    private static final String USUARIOS = "usuarios";

    @Autowired
    public UsuarioRestController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
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

    @GetMapping("/usuarios/coordinadores")
    public ResponseEntity<Map<String, Object>> getUsuariosCoordinadores() {
        List<Usuario> usuarios = usuarioService.findAll();
        if (usuarios.isEmpty()) {
            throw new NoHayUsuariosException();
        }
        List<Usuario> coordinadores = usuarios.stream()
                .filter(usuario -> "Coordinador".equals(usuario.getRol()))
                .toList();
        if (coordinadores.isEmpty()) {
            throw new RuntimeException("No hay coordinadores registrados en el sistema");
        }
        Map<String, Object> response = new HashMap<>();
        response.put(USUARIOS, coordinadores);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuarios/decanos")
    public ResponseEntity<Map<String, Object>> getUsuariosDecanos() {
        List<Usuario> usuarios = usuarioService.findAll();
        if (usuarios.isEmpty()) {
            throw new NoHayUsuariosException();
        }
        List<Usuario> decanos = usuarios.stream()
                .filter(usuario -> "Decano".equals(usuario.getRol()))
                .toList();
        if (decanos.isEmpty()) {
            throw new RuntimeException("No hay decanos registrados en el sistema");
        }
        Map<String, Object> response = new HashMap<>();
        response.put(USUARIOS, decanos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuarios/docentes")
    public ResponseEntity<Map<String, Object>> getUsuariosDocentes() {
        List<Usuario> usuarios = usuarioService.findAll();
        if (usuarios.isEmpty()) {
            throw new NoHayUsuariosException();
        }
        List<Usuario> docentes = usuarios.stream()
                .filter(usuario -> "Docente".equals(usuario.getRol()))
                .toList();
        if (docentes.isEmpty()) {
            throw new RuntimeException("No hay docentes registrados en el sistema");
        }
        Map<String, Object> response = new HashMap<>();
        response.put(USUARIOS, docentes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/page/{page}")
    public ResponseEntity<Object> index(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 4);
        Page<Usuario> usuarios = usuarioService.findAll(pageable);
        if (usuarios.isEmpty()) {
            throw new PaginaSinUsuariosException(page);
        }
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/usuarios")
    public ResponseEntity<Map<String, Object>> save(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
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