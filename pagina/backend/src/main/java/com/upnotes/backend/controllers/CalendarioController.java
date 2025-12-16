package com.upnotes.backend.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.upnotes.backend.models.Tarea;
import com.upnotes.backend.models.Usuario;
import com.upnotes.backend.repositories.TareaRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class CalendarioController {

    @Autowired private TareaRepository tareaRepository;

    private Usuario getUsuario(HttpSession session) {
        return (Usuario) session.getAttribute("usuarioLogueado");
    }

    @GetMapping("/calendario")
    public String calendario(Model model, HttpSession session) {
        Usuario u = getUsuario(session);
        if (u == null) return "redirect:/";

        int year = LocalDate.now().getYear();
        List<Tarea> pendientes = tareaRepository.findPendientesByUsuarioOrderByFecha(u);

        Map<LocalDate, List<Tarea>> porFecha = new HashMap<>();
        for (Tarea t : pendientes) {
            if (t.getFechaLimite() != null && t.getFechaLimite().getYear() == year) {
                porFecha.computeIfAbsent(t.getFechaLimite(), k -> new ArrayList<>()).add(t);
            }
        }

        model.addAttribute("year", year);
        model.addAttribute("tareasPorFecha", porFecha);
        return "calendario";
    }
}
