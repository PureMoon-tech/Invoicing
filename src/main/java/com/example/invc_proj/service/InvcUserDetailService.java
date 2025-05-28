package com.example.invc_proj.service;

import com.example.invc_proj.model.User;
import com.example.invc_proj.model.UserPrincipal;
import com.example.invc_proj.repository.ServicesRepo;
import com.example.invc_proj.repository.UserRepo;
import com.example.invc_proj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Service
public class InvcUserDetailService implements UserDetailsService
{
    @Autowired
    private  final UserRepo repositoryuser;

    public InvcUserDetailService(UserRepo repositoryUser,
                                 UserRepository userRepository) {
        repositoryuser = repositoryUser;
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName())));
    }




    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);
    private final UserRepository userRepository;

    public User saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        return repositoryuser.save(user) ;

    }

}
