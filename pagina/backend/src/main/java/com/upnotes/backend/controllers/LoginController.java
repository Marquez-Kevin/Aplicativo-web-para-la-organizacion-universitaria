package com.upnotes.backend.controllers;

import com.upnotes.backend.models.Materia;
import com.upnotes.backend.models.Usuario;
import com.upnotes.backend.repositories.MateriaRepository;
import com.upnotes.backend.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @GetMapping("/")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    @GetMapping("/horario")
    public String mostrarHorario(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/";

        // 1) Materias colocadas en el horario (con día/hora)
        List<Materia> materiasHorario =
                materiaRepository.findByUsuarioAndDiaIsNotNullAndHoraIsNotNull(usuario);

        // 2) Materias registradas en "Mis Materias" (sin día/hora)
        List<Materia> materiasRegistradas =
                materiaRepository.findByUsuarioAndDiaIsNullAndHoraIsNull(usuario);

        model.addAttribute("materias", materiasHorario);
        model.addAttribute("materiasRegistradas", materiasRegistradas); // <-- ESTA ES LA CLAVE
        model.addAttribute("usuario", usuario);

        return "horario";
    }

    @PostMapping("/registrar")
    public String registrarUsuario(@RequestParam String nombre,
                                   @RequestParam String correo,
                                   @RequestParam String password) {
        Usuario nuevoUsuario = new Usuario(nombre, correo, password);
        usuarioRepository.save(nuevoUsuario);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
                                @RequestParam String password,
                                HttpSession session) {

        Usuario usuarioEncontrado = usuarioRepository.findByCorreo(correo);

        if (usuarioEncontrado != null && usuarioEncontrado.getPassword().equals(password)) {
            session.setAttribute("usuarioLogueado", usuarioEncontrado);
            return "redirect:/horario";
        } else {
            return "redirect:/?error=true";
        }
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
