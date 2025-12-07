package com.proyecto.emilite.service;

import com.proyecto.emilite.model.Rol;
import com.proyecto.emilite.model.Usuario;
import com.proyecto.emilite.model.dto.UsuarioRegistroDTO;
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

    // Inyectamos RolService para poder usarlo en métodos como crearUsuarioDesdeDTO
    @Autowired
    private RolService rolService; // <-- Añadida esta línea

    // Método para obtener todos los usuarios
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    // Método para encontrar un usuario por ID
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    // Método para encontrar un usuario por nombre de usuario
    public Optional<Usuario> findByUserName(String userName) {
        return usuarioRepository.findByUserName(userName);
    }

    // Método para guardar un usuario (crea o actualiza)
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Método para eliminar un usuario por ID
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    // --- MÉTODO CORREGIDO: Crear usuario desde DTO ---
    // Método para crear un usuario desde el DTO
    public void crearUsuarioDesdeDTO(UsuarioRegistroDTO dto) {
        if (usuarioRepository.findByUserName(dto.getUserName()).isPresent()) {
            throw new RuntimeException("El nombre de usuario '" + dto.getUserName() + "' ya está en uso.");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUserName(dto.getUserName());
        nuevoUsuario.setPassword(passwordEncoder.encode(dto.getPassword())); // Encriptar la contraseña
        nuevoUsuario.setEmail(dto.getEmail());
        nuevoUsuario.setNombres(dto.getNombres());
        nuevoUsuario.setApellidos(dto.getApellidos());
        nuevoUsuario.setTelefono(dto.getTelefono());
        nuevoUsuario.setDireccion(dto.getDireccion());
        nuevoUsuario.setFechaNacimiento(dto.getFechaNacimiento());

        // Obtener la entidad Rol desde la base de datos usando el ID del DTO
        
        Rol rol = rolService.findById(dto.getRolId()) 
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + dto.getRolId()));
        nuevoUsuario.setRol(rol); // Asegúrate de que Usuario.java tenga setRol(Rol rol)

        // Fecha de registro se asigna por defecto en la entidad Usuario

        // Guarda el nuevo usuario en la base de datos
      
        usuarioRepository.save(nuevoUsuario); 
    }
   

    
    // Método para obtener todos los roles (si lo necesitas en el controlador)
    public List<Rol> findAllRoles() {
        return rolService.findAll(); 
    }


    
    // Método para encontrar usuarios por filtros (rol y estado activo)
    
    public List<Usuario> findByFilters(String rolNombre, Boolean activo) {
        if (rolNombre == null && activo == null) {
            // Si no hay filtros, devolver todos los usuarios
            return findAll();
        } else if (rolNombre != null && activo != null) {
           
           
            return usuarioRepository.findByRolNombreAndActivo(rolNombre, activo);
        } else if (rolNombre != null) {
         
            return usuarioRepository.findByRolNombre(rolNombre);
        } else {
           
            return usuarioRepository.findByActivo(activo);
        }
    }
   
    // Método para encontrar usuarios por nombre de rol
  
    public List<Usuario> findByRolNombre(String rolNombre) {
       
        return findByFilters(rolNombre, null);
    }

    public Optional<Usuario> findByEmail(String email) {
    return usuarioRepository.findByEmail(email);
    }
   
}