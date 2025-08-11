package com.aislan.controle_horas_extras.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.CookieValue;

@Controller
public class CustomErrorController implements ErrorController{
    @RequestMapping("/error")
    public String handleError(@CookieValue(name = "pref-estilo", defaultValue = "claro") String tema,
            HttpServletRequest request, Model model) {
        Object statusCode = request.getAttribute("jakarta.servlet.error.status_code");
        model.addAttribute("css", tema);
        if (statusCode != null && Integer.parseInt(statusCode.toString()) == 404) {
            model.addAttribute("mensagemErro", "Página não encontrada. Verifique o endereço digitado.");
            return "erro404"; // HTML personalizado
        }

        model.addAttribute("mensagemErro", "Ocorreu um erro inesperado.");
        return "erroGeral"; // Outro HTML para erros genéricos
    }
}
