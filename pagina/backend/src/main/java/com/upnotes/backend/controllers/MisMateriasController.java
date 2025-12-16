package com.upnotes.backend.controllers;

import com.upnotes.backend.models.MateriaUsuario;
import com.upnotes.backend.models.Tarea;
import com.upnotes.backend.models.Usuario;
import com.upnotes.backend.repositories.MateriaUsuarioRepository;
import com.upnotes.backend.repositories.TareaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Controller
public class MisMateriasController {

    @Autowired private MateriaUsuarioRepository materiaUsuarioRepository;
    @Autowired private TareaRepository tareaRepository;

    private Usuario getUsuario(HttpSession session) {
        return (Usuario) session.getAttribute("usuarioLogueado");
    }

    @GetMapping("/materias")
    public String ver(Model model, HttpSession session) {
        Usuario u = getUsuario(session);
        if (u == null) return "redirect:/";

        List<MateriaUsuario> materias = materiaUsuarioRepository.findByUsuarioOrderByIdDesc(u);
        List<Tarea> tareas = tareaRepository.findAllByUsuarioOrderByFecha(u);

        Map<Long, List<Tarea>> tareasPorMateria = new HashMap<>();
        for (Tarea t : tareas) {
            Long mid = t.getMateriaUsuario().getId();
            tareasPorMateria.computeIfAbsent(mid, k -> new ArrayList<>()).add(t);
        }

        model.addAttribute("materiasUsuario", materias);
        model.addAttribute("tareasPorMateria", tareasPorMateria);
        return "materias";
    }

    @PostMapping("/materias/guardar")
    public String guardarMateria(@RequestParam(required = false) String nombre,
                                 @RequestParam(required = false) Integer creditos,
                                 @RequestParam(required = false) Integer semestre,
                                 @RequestParam(required = false) String profesor,
                                 HttpSession session) {

        Usuario u = getUsuario(session);
        if (u == null) return "redirect:/";

        String n = (nombre == null) ? "" : nombre.trim();
        String p = (profesor == null) ? "" : profesor.trim();
        if (n.isEmpty()) n = "Materia sin nombre";

        materiaUsuarioRepository.save(new MateriaUsuario(n, creditos, semestre, p, u));
        return "redirect:/materias";
    }

    @PostMapping("/materias/eliminar")
    public String eliminarMateria(@RequestParam Long id, HttpSession session) {
        Usuario u = getUsuario(session);
        if (u == null) return "redirect:/";

        materiaUsuarioRepository.findByIdAndUsuario(id, u).ifPresent(materiaUsuarioRepository::delete);
        return "redirect:/materias";
    }

    @PostMapping("/tareas/guardar")
    public String guardarTarea(@RequestParam Long materiaUsuarioId,
                               @RequestParam String nombre,
                               @RequestParam(required = false) String descripcion,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaLimite,
                               HttpSession session) {

        Usuario u = getUsuario(session);
        if (u == null) return "redirect:/";

        Optional<MateriaUsuario> muOpt = materiaUsuarioRepository.findByIdAndUsuario(materiaUsuarioId, u);
        if (muOpt.isEmpty()) return "redirect:/materias";

        String n = (nombre == null) ? "" : nombre.trim();
        if (n.isEmpty()) n = "Tarea";

        String d = (descripcion == null) ? "" : descripcion.trim();

        tareaRepository.save(new Tarea(n, d, fechaLimite, muOpt.get()));
        return "redirect:/materias";
    }

    @PostMapping("/tareas/toggle")
    public String toggleTarea(@RequestParam Long id, HttpSession session) {
        Usuario u = getUsuario(session);
        if (u == null) return "redirect:/";

        // Verificación por usuario (evita editar tareas de otro)
        List<Tarea> todas = tareaRepository.findAllByUsuarioOrderByFecha(u);
        for (Tarea t : todas) {
            if (t.getId().equals(id)) {
                t.setCompletada(!t.isCompletada());
                tareaRepository.save(t);
                break;
            }
        }
        return "redirect:/materias";
    }

    @PostMapping("/tareas/eliminar")
    public String eliminarTarea(@RequestParam Long id, HttpSession session) {
        Usuario u = getUsuario(session);
        if (u == null) return "redirect:/";

        List<Tarea> todas = tareaRepository.findAllByUsuarioOrderByFecha(u);
        for (Tarea t : todas) {
            if (t.getId().equals(id)) {
                tareaRepository.delete(t);
                break;
            }
        }
        return "redirect:/materias";
    }
}
