package com.proyecto.emilite.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "perfiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre completo es obligatorio")
    private String nombreCompleto;

    @NotNull(message = "La edad es obligatoria")
    @Positive(message = "La edad debe ser un número positivo")
    private Integer edad;

    @NotNull(message = "El peso es obligatorio")
    @Positive(message = "El peso debe ser un número positivo")
    private Double peso;

    @NotNull(message = "La estatura es obligatoria")
    @Positive(message = "La estatura debe ser un número positivo")
    private Double estatura;

    private String telefono;

    @Email(message = "El email debe tener un formato válido")
    private String email;

    private String observaciones;

    // Relación bidireccional con Usuario
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}