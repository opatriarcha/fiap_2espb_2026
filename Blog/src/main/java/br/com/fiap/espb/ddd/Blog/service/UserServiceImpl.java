package br.com.fiap.espb.ddd.Blog.service;

import br.com.fiap.espb.ddd.Blog.datasource.repositories.UserRepository;
import br.com.fiap.espb.ddd.Blog.domainModel.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional( propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user){
        return this.userRepository.save(user);
    }

    @Override
    public void delete(User user){
        this.userRepository.delete(user);
    }

    @Override
    public User update(User user){
        return this.userRepository.save(user);
    }

    @Override
    public User partialUpdate(User user){
        return this.userRepository.save(user);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public Optional<User> findById(UUID id){
        return this.userRepository.findById(id);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public List<User> findAll(){
        return this.userRepository.findAll();
    }




}
