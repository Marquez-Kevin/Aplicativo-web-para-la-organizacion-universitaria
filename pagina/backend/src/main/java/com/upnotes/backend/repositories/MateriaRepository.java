package com.upnotes.backend.repositories;

import com.upnotes.backend.models.Materia;
import com.upnotes.backend.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MateriaRepository extends JpaRepository<Materia, Long> {

    // Materias que están en el HORARIO (tienen dia y hora)
    List<Materia> findByUsuarioAndDiaIsNotNullAndHoraIsNotNull(Usuario usuario);

    // Materias registradas en "Mis Materias" (NO tienen dia ni hora)
    List<Materia> findByUsuarioAndDiaIsNullAndHoraIsNull(Usuario usuario);

    // (Si aún la usas en otras partes, puedes dejarla)
    List<Materia> findByUsuario(Usuario usuario);
}
