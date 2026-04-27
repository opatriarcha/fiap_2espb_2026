package br.com.fiap.espb.ddd.Blog.resources;

import br.com.fiap.espb.ddd.Blog.domainModel.entities.Post;
import br.com.fiap.espb.ddd.Blog.resources.dtos.PostDTO;
import br.com.fiap.espb.ddd.Blog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts") // http://localhost:8080/api/posts
@RequiredArgsConstructor
public class PostResource {

    private final PostService postService;

    @GetMapping()
    public ResponseEntity<List<PostDTO>> findAll(){
       return ResponseEntity.ok(
               this.postService.findAll()
                       .stream()
                       .map(PostDTO::fromEntity)
                       .collect(Collectors.toList())
       );
    }

    @GetMapping("/find")
    public ResponseEntity<List<PostDTO>> findAll(Pageable pageable){
        return ResponseEntity.ok(
                this.postService.findAll(pageable)
                        .stream()
                        .map(PostDTO::fromEntity)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}") //http://localhost:8080/api/posts/{id}
    public ResponseEntity<PostDTO> findByID( @PathVariable UUID id){

        return this.postService.findById(id)
                .map(post -> ResponseEntity.ok(PostDTO.fromEntity(post)))
                .orElseGet( () -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PostDTO> create(@Valid @RequestBody PostDTO postDTO){
        Post post = PostDTO.fromDTO(postDTO);
        Post savedPost = this.postService.create(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body( PostDTO.fromEntity(savedPost));

    }










}
