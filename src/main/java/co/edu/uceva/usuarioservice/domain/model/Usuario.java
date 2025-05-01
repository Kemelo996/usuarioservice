package co.edu.uceva.usuarioservice.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Column(nullable = false)
    private String nombre;

    @NotEmpty(message = "El apellido no puede estar vacío")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    @Column(nullable = false)
    private String apellido;

    @NotEmpty(message = "El correo no puede estar vacío")
    @Email(message = "Debe ser un correo electrónico válido")
    @Column(nullable = false, unique = true)
    private String correo;

    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Column(nullable = false)
    private String contrasena;

    @Pattern(regexp = "\\d{8,15}", message = "El teléfono debe contener solo números (8-15 dígitos)")
    private String telefono;

    @Size(max = 255, message = "La dirección no puede exceder los 255 caracteres")
    private String direccion;

    private Boolean activo = true;
}

