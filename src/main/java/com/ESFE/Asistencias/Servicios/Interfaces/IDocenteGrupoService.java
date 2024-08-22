package com.ESFE.Asistencias.Servicios.Interfaces;

import com.ESFE.Asistencias.Entidades.DocenteGrupo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IDocenteGrupoService {
    Page<DocenteGrupo> BuscarTodosPaginados(Pageable pagaeble);
    List<DocenteGrupo> ObtenerTodos();
    Optional<DocenteGrupo> BuscarPorId(Integer id);
    DocenteGrupo CrearOeditar(DocenteGrupo docenteGrupo);
    void  EliminarPorId(Integer id);
}
