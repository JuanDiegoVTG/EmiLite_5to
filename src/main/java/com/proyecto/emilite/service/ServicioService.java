package com.proyecto.emilite.service;

import com.proyecto.emilite.model.Servicio;
import com.proyecto.emilite.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    // Método para obtener todos los servicios
    public List<Servicio> findAll() {
        return servicioRepository.findAll();
    }

    // Método para obtener un servicio por ID
    public Optional<Servicio> findById(Long id) {
        return servicioRepository.findById(id);
    }

    // Método para guardar (crear o actualizar) un servicio
    public Servicio save(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    // Método para eliminar un servicio por ID
    public void deleteById(Long id) {
        servicioRepository.deleteById(id);
    }

    // Método para buscar servicios activos 
    public List<Servicio> findActiveServices() {
        
        return servicioRepository.findAll().stream()
                .filter(Servicio::getActivo)
                .toList();
    }

    public List<Servicio> findByActivo(Boolean activo) {
    return servicioRepository.findByActivo(activo);
}
}