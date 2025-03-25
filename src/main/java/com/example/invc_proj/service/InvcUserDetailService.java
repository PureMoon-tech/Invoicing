package com.example.invc_proj.service;

import com.example.invc_proj.model.USERS;
import com.example.invc_proj.model.UserPrincipal;
import com.example.invc_proj.repository.ServicesRepo;
import com.example.invc_proj.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class InvcUserDetailService implements UserDetailsService
{
    @Autowired
    private  final UserRepo repositoryuser;

    public InvcUserDetailService(UserRepo repositoryUser) {
        repositoryuser = repositoryUser;
    }


    @Override
    public UserDetails loadUserByUsername(String user_name)
            throws UsernameNotFoundException
    {
        System.out.println("User  "+user_name);
       USERS user = repositoryuser.findByUserName(user_name);
        if (user == null)
        {
            System.out.println("User Not Found "+user_name);
            throw new UsernameNotFoundException("User Not Found");
        }
        return new UserPrincipal(user);
    }




    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);

    public USERS saveUser(USERS user) {
        user.setPassword(encoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        return repositoryuser.save(user) ;

    }

}
