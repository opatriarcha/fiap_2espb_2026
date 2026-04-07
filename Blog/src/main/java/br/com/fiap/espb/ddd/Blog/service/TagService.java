package br.com.fiap.espb.ddd.Blog.service;

import br.com.fiap.espb.ddd.Blog.datasource.repositories.TagRepository;
import br.com.fiap.espb.ddd.Blog.domainModel.entities.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag create(Tag tag){
        return this.tagRepository.save(tag);
    }

    public void delete( Tag tag ){
        this.tagRepository.delete(tag);
    }

    public Tag update(Tag tag){
        return this.tagRepository.save(tag);
    }

    public Tag partialUpdate(Tag tag ){
        return this.tagRepository.save(tag);
    }

    public List<Tag> findAllTags(){
        return this.tagRepository.findAll();
    }

    public List<Tag> findAllTagsByName( final String name ){
        return this.tagRepository.findAllByName(name);
    }

}
