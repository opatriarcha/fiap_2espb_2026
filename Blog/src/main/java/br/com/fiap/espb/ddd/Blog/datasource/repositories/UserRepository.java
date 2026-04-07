package br.com.fiap.espb.ddd.Blog.datasource.repositories;

import br.com.fiap.espb.ddd.Blog.domainModel.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
