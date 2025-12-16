package com.upnotes.backend.models;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tareas")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;        // recomendado
    private String descripcion;   // opcional

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaLimite;

    private boolean completada = false;

    @ManyToOne
    @JoinColumn(name = "materia_usuario_id")
    private MateriaUsuario materiaUsuario;

    public Tarea() {}

    public Tarea(String nombre, String descripcion, LocalDate fechaLimite, MateriaUsuario materiaUsuario) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        this.materiaUsuario = materiaUsuario;
        this.completada = false;
    }
}
