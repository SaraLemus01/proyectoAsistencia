package com.ESFE.Asistencias.Servicios.Implementaciones;

import com.ESFE.Asistencias.Entidades.Docente;
import com.ESFE.Asistencias.Repositorio.IDocenteRepository;
import com.ESFE.Asistencias.Servicios.Interfaces.IDocenteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocenteServices implements IDocenteServices {

    @Autowired
    private IDocenteRepository docenteRepository;
    @Override
    public Page<Docente> BuscarTodosPaginados(Pageable pagaeble) {
        return docenteRepository.findAll(pagaeble);
    }

    @Override
    public List<Docente> ObtenerTodos() {
        return docenteRepository.findAll();
    }

    @Override
    public Optional<Docente> BuscarPorId(Integer id) {
        return docenteRepository.findById(id);
    }

    @Override
    public Docente CrearOeditar(Docente docente) {
        return docenteRepository.save(docente);
    }

    @Override
    public void EliminarPorId(Integer id) {
        docenteRepository.deleteById(id);
    }
}
