package com.example.invc_proj.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class userLogin {

    private String user_name;
    private String Password;
    //private String PassowordSalt;
}
/*
@Entity
public class Users {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles; // e.g., ["ROLE_ADMIN", "ROLE_USER"]
}
*/