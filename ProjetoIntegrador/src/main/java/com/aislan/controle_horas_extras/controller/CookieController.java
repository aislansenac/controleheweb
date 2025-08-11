package com.aislan.controle_horas_extras.controller;

import com.aislan.controle_horas_extras.model.Preferencia;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin(origins = "*")
@Controller
public class CookieController {

    @PostMapping("/preferencias")
    public ModelAndView gravaPreferencias(@ModelAttribute Preferencia pref,
            HttpServletResponse response,
            HttpServletRequest request) {
        Cookie cookiePrefEstilo = new Cookie("pref-estilo", pref.getEstilo());
        cookiePrefEstilo.setDomain("localhost");
        cookiePrefEstilo.setHttpOnly(true);
        cookiePrefEstilo.setMaxAge(86400);
        response.addCookie(cookiePrefEstilo);
        // Recupera a página anterior do cabeçalho Referer
        String referer = request.getHeader("Referer");
        
        // Redireciona de volta para a página anterior (fallback: / se null)
        return new ModelAndView("redirect:" + (referer != null ? referer : "/"));
    }
}
