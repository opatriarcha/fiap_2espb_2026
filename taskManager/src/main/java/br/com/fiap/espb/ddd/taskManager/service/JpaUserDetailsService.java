package br.com.fiap.espb.es.ddd.taskManager.service;

import br.com.fiap.espb.es.ddd.taskManager.domainmodel.AuthUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import br.com.fiap.espb.es.ddd.taskManager.datasource.repository.UserRepository;

public class JpaUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser user = this.userRepository
                .findByName(username)
                .map(AuthUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
        return user;
    }
}
