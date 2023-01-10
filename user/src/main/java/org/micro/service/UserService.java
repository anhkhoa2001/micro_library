package org.micro.service;

import org.micro.dto.UserDTO;
import org.micro.model.UserModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserModel getUserByUsername(String username);
    UserModel add(UserDTO dto);
    UserModel getById(Integer id);
    UserModel getByToken(String token);
    List<UserModel> getAll();
    List<UserModel> getUsersById(List<Integer> ids);
}
