package org.micro.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "mu_authority")
@Data
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "authority", nullable = false, unique = true)
    private String authority;

    public Authority(String authority) {
        this.authority = authority;
    }

    public Authority() {

    }



    @Override
    public String getAuthority() {
        return authority;
    }
}