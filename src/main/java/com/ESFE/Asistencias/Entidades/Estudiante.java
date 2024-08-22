package com.ESFE.Asistencias.Entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "estudiantes")
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Ingrese el nombre del estudiante")
    private String nombre;

    @NotBlank(message = "Ingrese el email del estudiante")
    private String email;

    @NotBlank(message = "Ingrese el pin del estudiante")
    private String pin;

    @ManyToMany
    @JoinTable(
            name = "estudiantes_grupos",
            joinColumns = @JoinColumn(name = "estudiante_id"),
            inverseJoinColumns = @JoinColumn(name = "grupo_id")
    )

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotBlank(message = "Ingrese el nombre del estudiante") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "Ingrese el nombre del estudiante") String nombre) {
        this.nombre = nombre;
    }

    public @NotBlank(message = "Ingrese el email del estudiante") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Ingrese el email del estudiante") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Ingrese el pin del estudiante") String getPin() {
        return pin;
    }

    public void setPin(@NotBlank(message = "Ingrese el pin del estudiante") String pin) {
        this.pin = pin;
    }
}
