package com.upnotes.backend.models;

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
@Table(name = "materias_usuario") // CATÁLOGO DEL USUARIO
public class MateriaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;       // opcional
    private Integer creditos;    // opcional
    private Integer semestre;    // opcional
    private String profesor;     // opcional

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public MateriaUsuario() {}

    public MateriaUsuario(String nombre, Integer creditos, Integer semestre, String profesor, Usuario usuario) {
        this.nombre = nombre;
        this.creditos = creditos;
        this.semestre = semestre;
        this.profesor = profesor;
        this.usuario = usuario;
    }
}
