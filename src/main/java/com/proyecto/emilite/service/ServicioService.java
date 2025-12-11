package com.proyecto.emilite.service;

import com.proyecto.emilite.model.Servicio;
import com.proyecto.emilite.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    public List<Servicio> findAll() {
        return servicioRepository.findAll();
    }

    // ⚠ CORREGIDO: devuelve Servicio directo, NO Optional
    public Servicio findById(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));
    }

    public Servicio save(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    public void deleteById(Long id) {
        servicioRepository.deleteById(id);
    }

    public List<Servicio> findActiveServices() {
        return servicioRepository.findAll()
                .stream()
                .filter(Servicio::getActivo)
                .toList();
    }

    public List<Servicio> findByActivo(Boolean activo) {
        return servicioRepository.findByActivo(activo);
    }
}
