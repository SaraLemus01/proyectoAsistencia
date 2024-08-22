package com.ESFE.Asistencias.Servicios.Implementaciones;

import com.ESFE.Asistencias.Entidades.DocenteGrupo;
import com.ESFE.Asistencias.Repositorio.IDocenteGrupoRepository;
import com.ESFE.Asistencias.Servicios.Interfaces.IDocenteGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocenteGrupoService implements IDocenteGrupoService {

    @Autowired
    private IDocenteGrupoRepository docenteGrupoRepository;

    @Override
    public Page<DocenteGrupo> BuscarTodosPaginados(Pageable pageable) {
        return docenteGrupoRepository.findByOrderByDocenteDesc(pageable);
    }

    @Override
    public List<DocenteGrupo> ObtenerTodos() {
        return docenteGrupoRepository.findAll();
    }

    @Override
    public Optional<DocenteGrupo> BuscarPorId(Integer id) {
        return docenteGrupoRepository.findById(id);
    }

    @Override
    public DocenteGrupo CrearOeditar(DocenteGrupo docenteGrupo) {
        return docenteGrupoRepository.save(docenteGrupo);
    }

    @Override
    public void EliminarPorId(Integer id) {
        if (docenteGrupoRepository.existsById(id)) {
            docenteGrupoRepository.deleteById(id);
        } else {
            // Opcionalmente, lanzar una excepción si el id no existe
            throw new RuntimeException("No se encontró el DocenteGrupo con ID: " + id);
        }
    }
}