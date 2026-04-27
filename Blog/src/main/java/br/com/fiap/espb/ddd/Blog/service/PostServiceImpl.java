package br.com.fiap.espb.ddd.Blog.service;

import br.com.fiap.espb.ddd.Blog.datasource.repositories.PostRepository;
import br.com.fiap.espb.ddd.Blog.domainModel.entities.Post;
import br.com.fiap.espb.ddd.Blog.domainModel.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public Post create(Post post){
        return this.postRepository.save(post);
    }

    @Override
    public void delete(Post post){
        this.postRepository.delete(post);
    }

    @Override
    public Post update(Post post){
        return this.postRepository.save(post);
    }

    @Override
    public Post partialUpdate(Post post){
        return this.postRepository.save(post);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public Optional<Post> findById(UUID id){
        return this.postRepository.findById(id);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public List<Post> findAll(){
        return this.postRepository.findAll();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public Page<Post> findAll(Pageable pageable){
        return this.postRepository.findAll(pageable);
    }



}
