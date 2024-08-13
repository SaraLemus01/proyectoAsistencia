package com.ESFE.Asistencias.Entidades;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Embeddable
@Table(name = "docentes")
public class Docente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message =  "Ingrese el nombre del docente")
    private String nombre;
    @Nullable
    private String apellido;
    @Nullable
    private String email;
    @Nullable
    private String telefono;
    @Nullable
    private String escuela;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotBlank(message = "Ingrese el nombre del docente") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "Ingrese el nombre del docente") String nombre) {
        this.nombre = nombre;
    }

    @Nullable
    public String getApellido() {
        return apellido;
    }

    public void setApellido(@Nullable String apellido) {
        this.apellido = apellido;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @Nullable
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(@Nullable String telefono) {
        this.telefono = telefono;
    }

    @Nullable
    public String getEscuela() {
        return escuela;
    }

    public void setEscuela(@Nullable String escuela) {
        this.escuela = escuela;
    }

}
