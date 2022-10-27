package br.edu.ifms.tcc.controller;

import br.edu.ifms.tcc.model.User;
import br.edu.ifms.tcc.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @GetMapping("/new")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "/public-create-user";
    }

    @PostMapping("/save")
    public String saveUser(@Valid User user, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "/public-create-user";
        }
        userRepository.save(user);
        attributes.addFlashAttribute("message", "Usuario salvo com sucesso");
        return "redirect:/user/new";
    }

    @GetMapping("/admin/list")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "/auth/admin/admin-list-user";
    }

}