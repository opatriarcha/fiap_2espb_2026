package br.com.fiap.espb.ddd.taskManager.datasource.repository;

import br.com.fiap.espb.ddd.taskManager.domainmodel.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    public User fetchByEmail(final String email);
    public Optional<User> findByName(final String name);
}
