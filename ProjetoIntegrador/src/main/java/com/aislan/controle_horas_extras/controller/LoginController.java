package com.aislan.controle_horas_extras.controller;

import com.aislan.controle_horas_extras.data.FuncionarioEntity;
import com.aislan.controle_horas_extras.service.FuncionarioService;
import com.aislan.controle_horas_extras.service.criptografia.MD5CriptografiaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/")
public class LoginController {

    private final FuncionarioService funcionarioService;

    @Autowired
    public LoginController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @Autowired
    private MD5CriptografiaService criptografiaService;

    @GetMapping("/")
    public String home(@CookieValue(name = "pref-estilo", defaultValue = "claro") String tema,
            HttpSession session,
            Model model) {

        model.addAttribute("css", tema);

        FuncionarioEntity funcionario = (FuncionarioEntity) session.getAttribute("func");

        if (funcionario == null) {
            return "login"; // 🔐 impede acesso sem login
        }

        model.addAttribute("func", funcionario); // ✅ Adiciona objeto inteiro
        return "index";
    }

    @PostMapping("/login")
    public String processaLogin(@CookieValue(name = "pref-estilo", defaultValue = "claro") String tema,
            @RequestParam String cpf,
            @RequestParam String senha,
            HttpSession session,
            Model model) {
        model.addAttribute("css", tema);

        FuncionarioEntity funcionario = funcionarioService.pesquisarFuncionarioPorCpf(cpf);

        if (funcionario != null && criptografiaService.verificarSenha(senha, funcionario.getSenha())) {
            session.setAttribute("func", funcionario); // ✅ Agora salva a sessão
            model.addAttribute("func", funcionario);
            return "redirect:/"; // importante usar redirect
        } else {
            model.addAttribute("erro", "Usuário ou senha inválidos.");
            return "login";
        }
    }

    @GetMapping("/login")
    public String webHome(@CookieValue(name = "pref-estilo", defaultValue = "claro") String tema,
            Model model,
            HttpSession session) {
        FuncionarioEntity func = (FuncionarioEntity) session.getAttribute("func");
        model.addAttribute("css", tema);
        if (func != null) {
            model.addAttribute("func", func);
            
            return "index";
        }

        model.addAttribute("erro", "Sessão finalizada.");
        return "login"; // Retorna para a página de login com mensagem de erro
    }

    @PostMapping("/logout")
    public String logout(Model model,
            @CookieValue(name = "pref-estilo", 
            defaultValue = "claro") String tema, 
            HttpSession session) {
        model.addAttribute("css", tema);
        session.invalidate(); // remove todos os dados da sessão
        return "login";
    }
}
