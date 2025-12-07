package com.proyecto.emilite.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data // Genera getters, setters, toString, equals, hashCode
public class ServicioFormDTO {

    @NotBlank(message = "El nombre del servicio es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "La duración en minutos es obligatoria")
    @Positive(message = "La duración debe ser un número positivo")
    private Integer duracionMinutos;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser un número positivo")
    private BigDecimal precio;

}