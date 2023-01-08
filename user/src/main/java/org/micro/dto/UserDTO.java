package org.micro.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private Boolean status;
    private Set<Integer> authority_ids;

    public UserDTO() {}

    public UserDTO(String username, String password, Set<Integer> authority_ids) {
        this.status = true;
        this.username = username;
        this.password = password;
        this.authority_ids = authority_ids;
    }
}
