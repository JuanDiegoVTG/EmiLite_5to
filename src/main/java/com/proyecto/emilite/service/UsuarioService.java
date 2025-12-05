package com.proyecto.emilite.service;


import com.proyecto.emilite.model.Usuario;
import com.proyecto.emilite.repository.UsuarioRepository;
import org.springframework.lang.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service 
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para obtener todos los usuarios
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    // Método para obtener un usuario por ID
    public Optional<Usuario> findById(@NonNull Long id) {
        return usuarioRepository.findById(id);
    }

    // Método para guardar (crear o actualizar) un usuario
    public @NonNull Usuario save(@NonNull Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Método para eliminar un usuario por ID
    public void deleteById(@NonNull Long id) {
        usuarioRepository.deleteById(id);
    }

    // Método para buscar un usuario por su nombre de usuario (userName)
    public Optional<Usuario> findByUserName(@NonNull String userName) {
        return usuarioRepository.findByUserName(userName);
    }

    // Método para buscar usuarios por rol y estado
public List<Usuario> findByFilters(String rol, Boolean activo) {
    if (rol == null && activo == null) {
        // Si no hay filtros, devolver todos los usuarios
        return findAll();
    } else if (rol != null && activo != null) {
        // Si hay ambos filtros, usar un método del repositorio que combine ambos
        return usuarioRepository.findByRolNombreAndActivo(rol, activo);
    } else if (rol != null) {
        // Si solo hay filtro por rol
        return usuarioRepository.findByRolNombre(rol);
    } else {
        // Si solo hay filtro por estado
        return usuarioRepository.findByActivo(activo);
        }
    }
}
