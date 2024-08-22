package com.ESFE.Asistencias.Servicios.Implementaciones;

import com.ESFE.Asistencias.Entidades.EstudianteGrupo;
import com.ESFE.Asistencias.Repositorio.IEstudianteGrupoRepository;
import com.ESFE.Asistencias.Servicios.Interfaces.IEstudianteGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstudianteGrupoServices implements IEstudianteGrupoService {

    @Autowired
    private IEstudianteGrupoRepository estudianteGrupoRepository;

    @Override
    public Page<EstudianteGrupo> BuscarTodosPaginados(Pageable pageable) {
        return estudianteGrupoRepository.findByOrderByEstudianteDesc(pageable);
    }

    @Override
    public List<EstudianteGrupo> ObtenerTodos() {
        return estudianteGrupoRepository.findAll();
    }

    @Override
    public Optional<EstudianteGrupo> BuscarPorId(Integer id) {
        return estudianteGrupoRepository.findById(id);
    }

    @Override
    public EstudianteGrupo CrearOeditar(EstudianteGrupo estudianteGrupo) {
        return estudianteGrupoRepository.save(estudianteGrupo);
    }

    @Override
    public void EliminarPorId(Integer id) {
        if (estudianteGrupoRepository.existsById(id)) {
            estudianteGrupoRepository.deleteById(id);
        } else {
            // Opcionalmente, lanzar una excepción si el id no existe
            throw new RuntimeException("No se encontró el EstudianteGrupo con ID: " + id);
        }
    }
}
