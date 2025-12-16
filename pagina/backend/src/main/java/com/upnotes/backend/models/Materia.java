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
@Table(name = "materias") // HORARIO
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private int creditos;
    private String dia;
    private String hora;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Materia() {}

    public Materia(String nombre, int creditos, String dia, String hora, Usuario usuario) {
        this.nombre = nombre;
        this.creditos = creditos;
        this.dia = dia;
        this.hora = hora;
        this.usuario = usuario;
    }
}
