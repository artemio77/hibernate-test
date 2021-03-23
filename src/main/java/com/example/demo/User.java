package com.example.demo;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Post> posts = new HashSet<>();


}
