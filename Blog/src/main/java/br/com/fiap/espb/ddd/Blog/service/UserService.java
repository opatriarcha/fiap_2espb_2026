package br.com.fiap.espb.ddd.Blog.service;

import br.com.fiap.espb.ddd.Blog.domainModel.entities.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User create(User user);

    void delete(User user);

    User update(User user);

    User partialUpdate(User user);

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    Optional<User> findById(UUID id);

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    List<User> findAll();
}
