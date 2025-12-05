package com.proyecto.emilite.service;

import com.proyecto.emilite.model.Rol;
import com.proyecto.emilite.model.Usuario;
import com.proyecto.emilite.model.dto.UsuarioRegistroDTO;
import com.proyecto.emilite.repository.RolRepository;
import com.proyecto.emilite.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolRepository rolRepository;

    // Método para obtener todos los usuarios
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    // Método para obtener un usuario por ID
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    // Método para guardar (crear o actualizar) un usuario
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Método para eliminar un usuario por ID
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Método para buscar un usuario por su nombre de usuario (userName)
    public Optional<Usuario> findByUserName(String userName) {
        return usuarioRepository.findByUserName(userName);
    }

    // --- Nuevos métodos para registro ---

    // Método para obtener todos los ROLES
    public List<Rol> findAllRoles() {
        return rolRepository.findAll();
    }

    // Método para crear un usuario desde el DTO
    public void crearUsuarioDesdeDTO(UsuarioRegistroDTO dto) {
        if (usuarioRepository.findByUserName(dto.getUserName()).isPresent()) {
            throw new RuntimeException("El nombre de usuario '" + dto.getUserName() + "' ya está en uso.");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUserName(dto.getUserName());
        nuevoUsuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        nuevoUsuario.setEmail(dto.getEmail());
        nuevoUsuario.setNombres(dto.getNombres());
        nuevoUsuario.setApellidos(dto.getApellidos());
        nuevoUsuario.setTelefono(dto.getTelefono());
        nuevoUsuario.setDireccion(dto.getDireccion());
        nuevoUsuario.setFechaNacimiento(dto.getFechaNacimiento());

        // Obtener la entidad Rol desde la base de datos usando el ID del DTO
        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + dto.getRolId()));
        nuevoUsuario.setRol(rol);

        usuarioRepository.save(nuevoUsuario);
    }

    // --- Método para reportes con filtros ---
    public List<Usuario> findByFilters(String rolNombre, Boolean activo) {
        if (rolNombre == null && activo == null) {
            return findAll();
        } else if (rolNombre != null && activo != null) {
            return usuarioRepository.findByRolNombreAndActivo(rolNombre, activo);
        } else if (rolNombre != null) {
            return usuarioRepository.findByRolNombre(rolNombre);
        } else {
            return usuarioRepository.findByActivo(activo);
        }
    }
}