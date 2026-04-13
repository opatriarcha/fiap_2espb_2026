package br.com.fiap.espb.ddd.Blog.resources;

import br.com.fiap.espb.ddd.Blog.domainModel.entities.Post;
import br.com.fiap.espb.ddd.Blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts") // http://localhost:8080/api/posts
@RequiredArgsConstructor
public class PostResource {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> findAll(){
        return ResponseEntity.ok(  this.postService.findAll() );
    }


}
