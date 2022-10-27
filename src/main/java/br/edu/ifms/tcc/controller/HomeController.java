package br.edu.ifms.tcc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("msnBemVindo", "Bem Vindo ao Sistema de Adoçao de Animais de Estimaçao");
        return "public-index";
    }

}
