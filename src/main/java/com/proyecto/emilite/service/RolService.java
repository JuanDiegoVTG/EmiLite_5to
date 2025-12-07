package com.proyecto.emilite.service;

import com.proyecto.emilite.model.Rol;
import com.proyecto.emilite.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    // Método para obtener todos los roles
    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    // Método para obtener un rol por ID
    public Optional<Rol> findById(Long id) {
        return rolRepository.findById(id);
    }

    // Método para guardar (crear o actualizar) un rol
    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }

    // Método para eliminar un rol por ID
    public void deleteById(Long id) {
        rolRepository.deleteById(id);
    }

    // Método para encontrar un rol por nombre
    public List<Rol> findByNombre(String nombre) {
        return rolRepository.findByNombre(nombre); // Asegúrate de que RolRepository tenga este método
    }
}