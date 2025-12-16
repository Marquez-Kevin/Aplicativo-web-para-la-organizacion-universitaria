package com.upnotes.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upnotes.backend.models.MateriaUsuario;
import com.upnotes.backend.models.Usuario;

public interface MateriaUsuarioRepository extends JpaRepository<MateriaUsuario, Long> {
    List<MateriaUsuario> findByUsuarioOrderByIdDesc(Usuario usuario);
    Optional<MateriaUsuario> findByIdAndUsuario(Long id, Usuario usuario);
}
