package com.upnotes.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.upnotes.backend.models.Tarea;
import com.upnotes.backend.models.Usuario;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

    @Query("""
        SELECT t FROM Tarea t
        WHERE t.materiaUsuario.usuario = :usuario
        ORDER BY t.fechaLimite ASC, t.id DESC
    """)
    List<Tarea> findAllByUsuarioOrderByFecha(Usuario usuario);

    @Query("""
        SELECT t FROM Tarea t
        WHERE t.materiaUsuario.usuario = :usuario
          AND t.completada = false
        ORDER BY t.fechaLimite ASC, t.id DESC
    """)
    List<Tarea> findPendientesByUsuarioOrderByFecha(Usuario usuario);
}
