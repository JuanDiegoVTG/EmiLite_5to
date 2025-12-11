package com.proyecto.emilite.service;

import com.proyecto.emilite.model.Pago;
import com.proyecto.emilite.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public List<Pago> findAll() {
        return pagoRepository.findAll();
    }

    //devuelve Pago directo, NO Optional
    public Pago findById(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
    }

    public Pago save(Pago pago) {
        return pagoRepository.save(pago);
    }

    public void deleteById(Long id) {
        pagoRepository.deleteById(id);
    }

    public List<Pago> findByUsuarioId(Long usuarioId) {
        return pagoRepository.findByUsuarioId(usuarioId);
    }
}
