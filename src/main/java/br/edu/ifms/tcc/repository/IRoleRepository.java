package br.edu.ifms.tcc.repository;

import br.edu.ifms.tcc.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Long> {

    Role findByRole(String role);

}
