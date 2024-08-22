package com.ESFE.Asistencias.Servicios.Interfaces;

import com.ESFE.Asistencias.Entidades.Estudiante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IEstudianteServices {
    Page<Estudiante> BuscarTodosPaginados(Pageable pagaeble);
    List<Estudiante> ObtenerTodos();
    Optional<Estudiante> BuscarPorId(Integer id);
    Estudiante CrearOeditar(Estudiante estudiante);
    void  EliminarPorId(Integer id);
}
