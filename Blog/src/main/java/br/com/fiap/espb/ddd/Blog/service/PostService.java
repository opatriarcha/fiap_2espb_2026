package br.com.fiap.espb.ddd.Blog.service;

import br.com.fiap.espb.ddd.Blog.domainModel.entities.Post;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostService {
    Post create(Post post);

    void delete(Post post);

    Post update(Post post);

    Post partialUpdate(Post post);

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    Optional<Post> findById(UUID id);

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    List<Post> findAll();
}
