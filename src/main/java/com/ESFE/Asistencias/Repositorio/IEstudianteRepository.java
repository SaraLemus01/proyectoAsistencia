package com.ESFE.Asistencias.Repositorio;

import com.ESFE.Asistencias.Entidades.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstudianteRepository extends JpaRepository<Estudiante,Integer> {
}
