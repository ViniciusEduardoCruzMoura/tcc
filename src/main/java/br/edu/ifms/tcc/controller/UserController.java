package br.edu.ifms.tcc.controller;

import br.edu.ifms.tcc.model.User;
import br.edu.ifms.tcc.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

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

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id invalido: " + id));
        userRepository.delete(user);
        return "redirect:/user/admin/list";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") long id, Model model) {
        Optional<User> userOld = userRepository.findById(id);
        if (!userOld.isPresent()) {
            throw new IllegalArgumentException("Usuario invalido: " + id);
        }
        User user = userOld.get();
        model.addAttribute("user", user);
        return "/auth/user/user-edit-user";
    }

    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable("id") long id, @Valid User user, BindingResult resutl) {
        if (resutl.hasErrors()) {
            user.setId(id);
            return "/auth/user/user-edit-user";
        }
        userRepository.save(user);
        return "redirect:/user/admin/list";
    }

}
