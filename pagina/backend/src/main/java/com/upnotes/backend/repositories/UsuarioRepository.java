package com.upnotes.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upnotes.backend.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByCorreo(String correo);
}
