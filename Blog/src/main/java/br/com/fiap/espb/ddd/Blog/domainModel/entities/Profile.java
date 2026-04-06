package br.com.fiap.espb.ddd.Blog.domainModel.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table( name = "PROFILES")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    private @Getter @Setter UUID id;


    private @Getter @Setter String bio;

    private @Getter @Setter String profilePictureUrl;

    @OneToOne
    @JoinColumn( name = "user_id")
    private @Getter @Setter User user;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(id, profile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
