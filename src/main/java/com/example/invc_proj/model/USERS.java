package com.example.invc_proj.model;

import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class USERS {

    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
   // @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false)
    private Integer user_id;

   // @Column(name = "user_name", nullable = false, unique = true, length = 255)

    private String userName;

    private String password;
    private String password_salt;

    //@Column(name = "first_name", nullable = false, length = 255)
    private String first_name;

    //@Column(name = "last_name", length = 255)
    private String last_name;

    //@Column(name = "role_name", length = 255)
    private String role_name;

    //@Column(name = "role_id", nullable = false)
    private Integer role_id;

    //@Enumerated(EnumType.STRING)
    //@Column(name = "status", nullable = false)
    private String status;

    //@Column(name = "inserted_on", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date inserted_on;

 public String getUserName() {
  return userName;
 }

 public Integer getUser_id() {
  return user_id;
 }

 public String getPassword() {
  return password;
 }
}
