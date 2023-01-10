package org.micro.service.impl;

import org.micro.config.token.JwtTokenSetup;
import org.micro.dto.UserDTO;
import org.micro.dto.UserDetailCustomize;
import org.micro.model.UserModel;
import org.micro.repository.UserRepository;
import org.micro.service.UserService;
import org.micro.service.converter.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private JwtTokenSetup jwt;

    @Override
    public UserModel getUserByUsername(String username) {
        return userRepository.findUserModelByUsername(username);
    }

    @Override
    public UserModel add(UserDTO dto) {
        UserModel userModel = userConverter.convertDTO2Model(dto);
        if(userModel == null) {
            return null;
        }
        userModel = userRepository.save(userModel);
        return userModel;
    }

    @Override
    public UserModel getById(final Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserModel getByToken(String token) {
        try {
            String username = jwt.getUsernamFromToken(token);
            return getUserByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<UserModel> getAll() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<UserModel> getUsersById(List<Integer> ids) {
        try {
            return userRepository.findUserModelsByIdIsIn(ids);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public UserDetailCustomize loadUserByUsername(String username) {
        try {
            UserModel userModel = getUserByUsername(username);
            if(userModel == null) {
                return null;
            }
            UserDetailCustomize user = new UserDetailCustomize();
            user.setUsername(userModel.getUsername());
            user.setPassword(userModel.getPassword());
            user.setAuthorities(userModel.getAuthorities());
            user.setStatus(userModel.getStatus());

            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
