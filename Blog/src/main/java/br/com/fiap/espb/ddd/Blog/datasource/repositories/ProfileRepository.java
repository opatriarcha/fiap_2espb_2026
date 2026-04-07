package br.com.fiap.espb.ddd.Blog.datasource.repositories;

import br.com.fiap.espb.ddd.Blog.domainModel.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
}
