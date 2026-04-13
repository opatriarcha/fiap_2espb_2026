package br.com.fiap.espb.ddd.Blog.resources.dtos;

import br.com.fiap.espb.ddd.Blog.domainModel.entities.Post;
import br.com.fiap.espb.ddd.Blog.domainModel.entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Builder
public class PostDTO {
    private @Getter @Setter UUID id;

    @NotBlank(message="O título é obrigatorio")
    @Size(max=100, message="O titulo deve ter no maximo 100 caracteres")
    private @Getter @Setter String title;

    @NotBlank(message="O Conteúdo é obrigatorio")
    @Size(max=255, message="O Conteudo deve ter no maximo 255 caracteres")
    private @Getter @Setter String content;

    private @Getter @Setter UUID userId;

    public static PostDTO fromEntity(final Post post ){
        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(post.getUser() == null ? null : post.getUser().getId())
                .build();
    }

    public static Post fromDTO( final PostDTO dto ){
        User user = null;
        if( dto.getId() != null ){
            user = new User();
            user.setId(dto.getId());
        }
        return Post.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .build();
    }

}
