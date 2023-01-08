package org.micro.service.converter;

import org.micro.dto.UserDTO;
import org.micro.model.Authority;
import org.micro.model.UserModel;
import org.micro.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
public class UserConverter {

    @Autowired
    private AuthorityService authorityService;

    public UserModel convertDTO2Model(UserDTO source) {
        UserModel target = new UserModel();
        if(source.getId() != null && !source.getId().equals(0)) {
            target.setId(source.getId());
        }
        target.setPassword(source.getPassword());
        target.setUsername(source.getUsername());
        target.setStatus(source.getStatus());

        List<Authority> authorities = authorityService.findAllById(source.getAuthority_ids());
        target.setAuthorities(new HashSet<>(authorities));

        return target;
    }
}
