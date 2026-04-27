package br.com.fiap.espb.ddd.Blog.infrastructure;


import br.com.fiap.espb.ddd.Blog.datasource.repositories.PostRepository;
import br.com.fiap.espb.ddd.Blog.domainModel.entities.Post;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDataLoad( PostRepository postRepository ){
        return args ->{
            for( int i = 0 ; i <= 10000; i++){
                postRepository.save(Post.builder()
                        .title("Sample Post " + i)
                        .content("Some fuckin content " + i)
                        .build()
                );

            }
        };
    }
}
