package com.upnotes.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upnotes.backend.models.MateriaUsuario;
import com.upnotes.backend.models.NotaActividad;
import com.upnotes.backend.models.TipoNota;

public interface NotaActividadRepository extends JpaRepository<NotaActividad, Long> {
    List<NotaActividad> findByMateriaUsuarioAndCorteOrderByIdDesc(MateriaUsuario materiaUsuario, int corte);
    List<NotaActividad> findByMateriaUsuarioAndCorteAndTipoOrderByIdDesc(MateriaUsuario materiaUsuario, int corte, TipoNota tipo);

    Optional<NotaActividad> findFirstByMateriaUsuarioAndCorteAndTipoOrderByIdDesc(MateriaUsuario materiaUsuario, int corte, TipoNota tipo);
}
