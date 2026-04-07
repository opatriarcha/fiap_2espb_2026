package br.com.fiap.espb.ddd.Blog.datasource.repositories;

import br.com.fiap.espb.ddd.Blog.domainModel.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {
    public List<Tag> findAllByName(String name );
}
