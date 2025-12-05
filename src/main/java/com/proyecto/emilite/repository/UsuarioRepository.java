package com.proyecto.emilite.repository;

import com.proyecto.emilite.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUserName(String userName);

    // Métodos para el reporte de usuarios con filtros
    // Buscar por nombre del rol (relación ManyToOne)
    List<Usuario> findByRolNombre(String rolNombre);

    // Buscar por estado activo
    List<Usuario> findByActivo(Boolean activo);

    // Buscar por nombre del rol Y estado activo
    List<Usuario> findByRolNombreAndActivo(String rolNombre, Boolean activo);

    // Opcional: Buscar por rol Y estado, pero usando el ID del rol
    // List<Usuario> findByRolIdAndActivo(Long rolId, Boolean activo);
}