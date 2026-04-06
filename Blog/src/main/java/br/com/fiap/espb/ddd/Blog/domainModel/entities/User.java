package br.com.fiap.espb.ddd.Blog.domainModel.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table( name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    private @Getter
    @Setter UUID id;

    @Column(name="NAME", nullable = false, length=150)
    @NotBlank( message = "O nome é obrigatório")
    @Size( max= 150, message = "O nome deve te rno máximo 150 caraacteres")
    private @Getter @Setter String name;

    @Column(name="EMAIL", nullable = false, length=100)
    @NotBlank( message = "O email é obrigatório")
    @Size( max= 100, message = "O email deve te rno máximo 100 caraacteres")
    @Email(message = "O email deve ser válido")
    private @Getter @Setter String email;

    @Column(name="PASSWORD", nullable = false, length=6)
    @NotBlank( message = "O password é obrigatório")
    @Size( min = 6, message = "O password deve ter no minimo 6 caracteres")
    private @Getter @Setter String password;

    @OneToOne( mappedBy = "user", cascade = CascadeType.ALL)
    private @Getter @Setter Profile profile;

    @OneToMany( mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private @Getter @Setter Set<Post> posts;

    @ManyToMany( fetch = FetchType.EAGER)
    @JoinTable(
            name = "USERS_ROLES",
            joinColumns = @JoinColumn( name = "user_id"),
            inverseJoinColumns = @JoinColumn( name = "role_id")
    )
    private @Getter @Setter Set<Role> roles;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
