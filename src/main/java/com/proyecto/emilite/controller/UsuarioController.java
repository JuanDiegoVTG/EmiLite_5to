package com.proyecto.emilite.controller;

import com.proyecto.emilite.model.Usuario;
import com.proyecto.emilite.service.UsuarioService;
import jakarta.validation.Valid; // Importa esta anotación
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull; // Importa esta anotación
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

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
    public Usuario createUsuario(@NonNull @Valid @RequestBody Usuario usuario) { // Agregamos @Valid también
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