package com.proyecto.emilite.controller;

import com.proyecto.emilite.model.Rol; // Importa la entidad Rol
import com.proyecto.emilite.model.Usuario;
import com.proyecto.emilite.model.dto.UsuarioRegistroDTO;
import com.proyecto.emilite.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint: GET /usuarios/registro
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuarioForm", new UsuarioRegistroDTO());
        // Cambia el tipo de la variable a List<Rol>
        List<Rol> roles = usuarioService.findAllRoles();
        model.addAttribute("roles", roles);
        return "registro_usuario";
    }

    // Endpoint: POST /usuarios/crear
    @PostMapping("/crear")
    public String crearUsuario(@Valid @ModelAttribute("usuarioForm") UsuarioRegistroDTO usuarioForm,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            // Cambia el tipo de la variable a List<Rol>
            List<Rol> roles = usuarioService.findAllRoles(); 
            model.addAttribute("roles", roles);
            return "registro_usuario";
        }

        try {
            usuarioService.crearUsuarioDesdeDTO(usuarioForm);
            return "redirect:/usuarios?registrado=success";
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar el usuario: " + e.getMessage());
            // Cambia el tipo de la variable a List<Rol>
            List<Rol> roles = usuarioService.findAllRoles(); 
            model.addAttribute("roles", roles);
            return "registro_usuario";
        }
    }
    // Endpoint: GET /usuarios
@GetMapping
public String listarUsuarios(Model model) {
    List<Usuario> usuarios = usuarioService.findAll();
    model.addAttribute("usuarios", usuarios);
    return "lista_usuarios"; // Devuelve la vista que muestre la lista de usuarios
}
}

// Creamos un nuevo controlador para la API REST
@RestController
@RequestMapping("/api/usuarios")
class UsuarioRestController {

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint: GET /api/usuarios
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.findAll();
    }

    // Endpoint: GET /api/usuarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@NonNull @PathVariable Long id) {
        return usuarioService.findById(id)
                .map(usuario -> ResponseEntity.ok().body(usuario))
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint: POST /api/usuarios
    @PostMapping
    public Usuario createUsuario(@NonNull @Valid @RequestBody Usuario usuario) {
        return usuarioService.save(usuario);
    }

    // Endpoint: PUT /api/usuarios/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@NonNull @PathVariable Long id, @NonNull @Valid @RequestBody Usuario usuario) {
        if (!usuarioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        usuario.setId(id);
        Usuario updatedUsuario = usuarioService.save(usuario);
        return ResponseEntity.ok(updatedUsuario);
    }

    // Endpoint: DELETE /api/usuarios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@NonNull @PathVariable Long id) {
        if (!usuarioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint: GET /api/usuarios/username/{userName}
    @GetMapping("/username/{userName}")
    public ResponseEntity<Usuario> getUsuarioByUserName(@NonNull @PathVariable String userName) {
        return usuarioService.findByUserName(userName)
                .map(usuario -> ResponseEntity.ok().body(usuario))
                .orElse(ResponseEntity.notFound().build());
    }
}