package com.proyecto.emilite.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rutina")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "nivel_dificultad", length = 50)
    private String nivelDificultad; 

    @Column(name = "tipo", length = 50)
    private String tipo; 

    @Column(name = "duracion_semanas")
    private Integer duracionSemanas;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Muchas rutinas pueden tener un cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false) 
    private Usuario cliente;

    
}