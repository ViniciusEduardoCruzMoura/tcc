package br.edu.ifms.tcc.component;

import br.edu.ifms.tcc.model.Role;
import br.edu.ifms.tcc.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        String[] roles = {"ADMIN", "USER", "CUSTOMER"};
        for(String roleString : roles) {
            Role role = roleRepository.findByRole(roleString);
            if (role == null) {
                role = new Role();
                role.setRole(roleString);
                roleRepository.save(role);
            }
        }
    }
}
