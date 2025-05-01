-- Elimina primero los datos existentes si es necesario
TRUNCATE TABLE usuario RESTART IDENTITY CASCADE;

-- Inserta todos los datos en una sola sentencia
INSERT INTO usuario (nombre, apellido, correo, contrasena, telefono, direccion, activo) VALUES
                                                                                            ('Admin', 'Sistema', 'admin@empresa.com', '$2a$10$ABC123', '12345678', 'Calle Admin 123', true),
                                                                                            ('Juan', 'Pérez', 'juan@empresa.com', '$2a$10$XYZ456', '87654321', 'Calle Principal 456', true),
                                                                                            ('María', 'Gómez', 'maria@empresa.com', '$2a$10$DEF789', '11223344', 'Avenida Secundaria 789', true),
                                                                                            ('Carlos', 'Ruiz', 'carlos@test.com', '$2a$10$111111', '55667788', 'Calle 123', true),
                                                                                            ('Ana', 'Martinez', 'ana@test.com', '$2a$10$222222', '99887766', 'Av Libertad', true);