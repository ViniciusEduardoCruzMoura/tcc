package br.edu.ifms.tcc.repository;

import br.edu.ifms.tcc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {
}
