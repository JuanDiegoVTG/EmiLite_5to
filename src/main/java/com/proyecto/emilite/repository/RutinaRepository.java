package com.proyecto.emilite.repository;

import com.proyecto.emilite.model.Rutina;
import com.proyecto.emilite.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutinaRepository extends JpaRepository<Rutina, Long> {
    // Método para encontrar rutinas por ID del cliente
    List<Rutina> findByClienteId(Long clienteId);
}