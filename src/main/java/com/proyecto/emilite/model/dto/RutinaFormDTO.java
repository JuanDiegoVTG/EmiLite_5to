package com.proyecto.emilite.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RutinaFormDTO {

    @NotBlank(message = "El nombre de la rutina es obligatorio")
    private String nombre;

    private String descripcion;

    private String nivelDificultad; // Principiante, Intermedio, Avanzado

    private String tipo; 

    private Integer duracionSemanas;

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId; 
}