package com.proyecto.emilite.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data; 
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@NotNull
@Table(name = "usuario")
@Data 
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @Column(name = "username", unique = true, nullable = false)
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String userName; 
    @Column(name = "password", nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Column(name = "email", unique = true)
    @Email(message = "El email debe tener un formato válido")
    private String email; 

    @Column(name = "nombres", nullable = false)
    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @Column(name = "apellidos", nullable = false)
    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos; 

    @Column(name = "telefono")
    private String telefono; 

    @Column(name = "direccion")
    private String direccion; 

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento; 

    // Relación con la tabla rol
     @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;


    @Column(name = "activo", nullable = false)
    private Boolean activo = true; 
}