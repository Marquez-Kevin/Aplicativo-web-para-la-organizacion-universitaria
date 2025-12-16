package com.upnotes.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "notas_actividad")
public class NotaActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int corte; // 1,2,3

    @Enumerated(EnumType.STRING)
    private TipoNota tipo; // QUINCE o VEINTE

    private String nombreActividad; // para QUINCE (y opcionalmente para VEINTE)
    private Double valor; // nota

    @ManyToOne
    @JoinColumn(name = "materia_usuario_id")
    private MateriaUsuario materiaUsuario;

    public NotaActividad() {}

    public NotaActividad(int corte, TipoNota tipo, String nombreActividad, Double valor, MateriaUsuario materiaUsuario) {
        this.corte = corte;
        this.tipo = tipo;
        this.nombreActividad = nombreActividad;
        this.valor = valor;
        this.materiaUsuario = materiaUsuario;
    }
}
