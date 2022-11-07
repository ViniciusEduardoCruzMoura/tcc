package br.edu.ifms.tcc.controller;

import br.edu.ifms.tcc.model.Role;
import br.edu.ifms.tcc.model.User;
import br.edu.ifms.tcc.repository.IRoleRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @GetMapping("/new")
    public String addUser(Model model) {
        //carrega a pagina de criar usuario
        model.addAttribute("user", new User());
        return "/public-create-user";
    }

    @PostMapping("/save")
    public String saveUser(@Valid User user, BindingResult result, Model model, RedirectAttributes attributes) {
        //Retorna erros da validaçao dos campos
        if (result.hasErrors()) {
            return "/public-create-user";
        }

        //verificar se ja existe usuario registrado com o mesmo login
        User userDatabas = userRepository.findByLogin(user.getLogin());
        if (userDatabas != null) {
            model.addAttribute("loginMessage", "Login ja existe cadastrado");
            return "/public-create-user";
        }

        //atribui o papel para o novo registro de usuario
        Role role = roleRepository.findByRole("USER");
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);

        //salva o usuario no banco de dados e vai para a pagina de criar usuario com uma mensagem de sucesso
        userRepository.save(user);
        attributes.addFlashAttribute("message", "Usuario salvo com sucesso");
        return "redirect:/user/new";
    }

    @GetMapping("/admin/list")
    public String listUsers(Model model) {
        //busca todos os registros de usuarios no banco de dados e carrega a pagina de listar usuarios
        model.addAttribute("users", userRepository.findAll());
        return "/auth/admin/admin-list-user";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        //busca o usuario no banco de dados por id, e o remove, entao vai para a pagina de listar usuarios
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id invalido: " + id));
        userRepository.delete(user);
        return "redirect:/user/admin/list";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") long id, Model model) {
        //verificar se existe registro do usuario no banco
        Optional<User> userOld = userRepository.findById(id);
        if (!userOld.isPresent()) {
            throw new IllegalArgumentException("Usuario invalido: " + id);
        }
        //carrega a pagina de editar usuario com os dados do registro do usuario no banco
        User user = userOld.get();
        model.addAttribute("user", user);
        return "/auth/user/user-edit-user";
    }

    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable("id") long id, @Valid User user, BindingResult resutl) {
        //Retorna erros da validaçao dos campos
        if (resutl.hasErrors()) {
            user.setId(id);
            return "/auth/user/user-edit-user";
        }
        //salva no banco de dados e vai para a pagina de listar usuarios
        userRepository.save(user);
        return "redirect:/user/admin/list";
    }

}
