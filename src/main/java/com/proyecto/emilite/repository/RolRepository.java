package com.proyecto.emilite.repository;

import com.proyecto.emilite.model.Rol; // Asegúrate de importar la entidad Rol
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    
}