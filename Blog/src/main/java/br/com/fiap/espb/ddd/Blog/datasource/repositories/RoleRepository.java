package br.com.fiap.espb.ddd.Blog.datasource.repositories;

import br.com.fiap.espb.ddd.Blog.domainModel.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID>{
}
