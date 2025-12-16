package com.upnotes.backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.upnotes.backend.models.MateriaUsuario;
import com.upnotes.backend.models.NotaActividad;
import com.upnotes.backend.models.TipoNota;
import com.upnotes.backend.models.Usuario;
import com.upnotes.backend.repositories.MateriaUsuarioRepository;
import com.upnotes.backend.repositories.NotaActividadRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class NotasController {

    @Autowired private MateriaUsuarioRepository materiaUsuarioRepository;
    @Autowired private NotaActividadRepository notaActividadRepository;

    private Usuario getUsuario(HttpSession session) {
        return (Usuario) session.getAttribute("usuarioLogueado");
    }

    @GetMapping("/notas")
    public String notas(@RequestParam(required = false) Long materiaId,
                        Model model,
                        HttpSession session) {
        Usuario u = getUsuario(session);
        if (u == null) return "redirect:/";

        List<MateriaUsuario> materias = materiaUsuarioRepository.findByUsuarioOrderByIdDesc(u);
        model.addAttribute("materiasUsuario", materias);

        MateriaUsuario seleccionada = null;
        if (!materias.isEmpty()) {
            if (materiaId != null) {
                for (MateriaUsuario m : materias) {
                    if (m.getId().equals(materiaId)) { seleccionada = m; break; }
                }
            }
            if (seleccionada == null) seleccionada = materias.get(0);
        }
        model.addAttribute("materiaSeleccionada", seleccionada);

        Map<Integer, List<NotaActividad>> notas15 = new HashMap<>();
        Map<Integer, NotaActividad> nota20 = new HashMap<>();
        Map<Integer, Double> notaCorte = new HashMap<>();

        if (seleccionada != null) {
            for (int corte = 1; corte <= 3; corte++) {
                List<NotaActividad> lista15 = notaActividadRepository
                        .findByMateriaUsuarioAndCorteAndTipoOrderByIdDesc(seleccionada, corte, TipoNota.QUINCE);

                Optional<NotaActividad> n20Opt = notaActividadRepository
                        .findFirstByMateriaUsuarioAndCorteAndTipoOrderByIdDesc(seleccionada, corte, TipoNota.VEINTE);

                notas15.put(corte, lista15);
                nota20.put(corte, n20Opt.orElse(null));

                double promedio15 = 0.0;
                if (!lista15.isEmpty()) {
                    double sum = 0.0;
                    int c = 0;
                    for (NotaActividad n : lista15) {
                        if (n.getValor() != null) { sum += n.getValor(); c++; }
                    }
                    promedio15 = (c == 0) ? 0.0 : (sum / c);
                }

                double valor20 = (n20Opt.isPresent() && n20Opt.get().getValor() != null) ? n20Opt.get().getValor() : 0.0;

                double peso15 = (corte == 3) ? 0.10 : 0.15;
                double peso20 = 0.20;

                double corteFinal = (promedio15 * peso15) + (valor20 * peso20);
                notaCorte.put(corte, redondear2(corteFinal));
            }
        }

        model.addAttribute("notas15", notas15);
        model.addAttribute("nota20", nota20);
        model.addAttribute("notaCorte", notaCorte);

        return "notas";
    }

    private double redondear2(double x) {
        return Math.round(x * 100.0) / 100.0;
    }

    @PostMapping("/notas/guardar15")
    public String guardar15(@RequestParam Long materiaId,
                            @RequestParam int corte,
                            @RequestParam String actividad,
                            @RequestParam Double valor,
                            HttpSession session) {
        Usuario u = getUsuario(session);
        if (u == null) return "redirect:/";

        var muOpt = materiaUsuarioRepository.findByIdAndUsuario(materiaId, u);
        if (muOpt.isEmpty()) return "redirect:/notas";

        String a = (actividad == null) ? "" : actividad.trim();
        if (a.isEmpty()) a = "Actividad";

        notaActividadRepository.save(new NotaActividad(corte, TipoNota.QUINCE, a, valor, muOpt.get()));
        return "redirect:/notas?materiaId=" + materiaId;
    }

    @PostMapping("/notas/guardar20")
    public String guardar20(@RequestParam Long materiaId,
                            @RequestParam int corte,
                            @RequestParam Double valor,
                            HttpSession session) {
        Usuario u = getUsuario(session);
        if (u == null) return "redirect:/";

        var muOpt = materiaUsuarioRepository.findByIdAndUsuario(materiaId, u);
        if (muOpt.isEmpty()) return "redirect:/notas";

        // Solo 1 nota 20 por corte: si existe, la actualizamos
        var existenteOpt = notaActividadRepository.findFirstByMateriaUsuarioAndCorteAndTipoOrderByIdDesc(muOpt.get(), corte, TipoNota.VEINTE);
        if (existenteOpt.isPresent()) {
            NotaActividad n = existenteOpt.get();
            n.setValor(valor);
            n.setNombreActividad("Nota 20%");
            notaActividadRepository.save(n);
        } else {
            notaActividadRepository.save(new NotaActividad(corte, TipoNota.VEINTE, "Nota 20%", valor, muOpt.get()));
        }

        return "redirect:/notas?materiaId=" + materiaId;
    }

    @PostMapping("/notas/eliminar")
    public String eliminarNota(@RequestParam Long materiaId,
                               @RequestParam Long id,
                               HttpSession session) {
        Usuario u = getUsuario(session);
        if (u == null) return "redirect:/";

        // borrar seguro: solo si pertenece al usuario por su materia
        var muOpt = materiaUsuarioRepository.findByIdAndUsuario(materiaId, u);
        if (muOpt.isPresent()) {
            NotaActividad nota = notaActividadRepository.findById(id).orElse(null);
            if (nota != null && nota.getMateriaUsuario() != null
                    && nota.getMateriaUsuario().getId().equals(muOpt.get().getId())) {
                notaActividadRepository.delete(nota);
            }
        }

        return "redirect:/notas?materiaId=" + materiaId;
    }
}
