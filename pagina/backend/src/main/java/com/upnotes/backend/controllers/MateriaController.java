package com.upnotes.backend.controllers;

import com.upnotes.backend.models.Materia;
import com.upnotes.backend.models.Usuario;
import com.upnotes.backend.repositories.MateriaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class MateriaController {

    @Autowired
    private MateriaRepository materiaRepository;

    /**
     * Guarda una materia en una casilla del horario.
     * - Si llega materiaId (desde "Mis Materias"), copia nombre/créditos de esa materia.
     * - nombre y creditos son opcionales.
     */
    @PostMapping("/guardarMateria")
    public String guardarMateria(
            @RequestParam(required = false) Long materiaId,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer creditos,
            @RequestParam String dia,
            @RequestParam String hora,
            HttpSession session
    ) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/";

        String nombreFinal = (nombre == null) ? "" : nombre.trim();
        Integer creditosFinal = creditos; // puede ser null

        // Si seleccionó desde "Mis Materias", usamos esos datos como base
        if (materiaId != null) {
            Optional<Materia> baseOpt = materiaRepository.findById(materiaId);
            if (baseOpt.isPresent()) {
                Materia base = baseOpt.get();

                // Seguridad: que sea del usuario
                if (base.getUsuario() != null && base.getUsuario().getId() != null
                        && base.getUsuario().getId().equals(usuario.getId())) {

                    // Tomar nombre/créditos desde Mis Materias si el usuario no escribió nada
                    if (nombreFinal.isEmpty() && base.getNombre() != null) {
                        nombreFinal = base.getNombre();
                    }
                    if (creditosFinal == null) {
                        creditosFinal = base.getCreditos();
                    }
                }
            }
        }

        // Si siguen vacíos, asignamos valores seguros
        if (nombreFinal.isEmpty()) nombreFinal = "(Sin nombre)";
        if (creditosFinal == null) creditosFinal = 0;

        Materia nueva = new Materia(nombreFinal, creditosFinal, dia, hora, usuario);
        materiaRepository.save(nueva);

        return "redirect:/horario";
    }

    /**
     * Elimina SOLO una materia del horario (una casilla específica).
     * Para evitar borrar una materia de "Mis Materias", validamos que tenga dia/hora.
     */
    @PostMapping("/eliminarMateriaHorario")
    public String eliminarMateriaHorario(@RequestParam Long id, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/";

        Optional<Materia> materiaOpt = materiaRepository.findById(id);
        if (materiaOpt.isEmpty()) return "redirect:/horario";

        Materia m = materiaOpt.get();

        // Seguridad: solo borra si es del usuario
        if (m.getUsuario() == null || m.getUsuario().getId() == null
                || !m.getUsuario().getId().equals(usuario.getId())) {
            return "redirect:/horario";
        }

        // Seguridad extra: solo borrar si es del horario (tiene dia y hora)
        if (m.getDia() == null || m.getHora() == null) {
            return "redirect:/horario";
        }

        materiaRepository.deleteById(id);
        return "redirect:/horario";
    }
}
