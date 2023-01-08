package org.micro.service.impl;

import org.micro.model.Authority;
import org.micro.repository.AuthorityRepository;
import org.micro.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Authority findById(Integer id) {
        return authorityRepository.findById(id).orElse(null);
    }

    @Override
    public List<Authority> findAllById(Set<Integer> authority_ids) {
        try {
            return authorityRepository.findAllById(authority_ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Authority add(Authority authority) {
        try {
            return authorityRepository.save(authority);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Authority getByName(String name) {
        try {
            return authorityRepository.getAuthorityByAuthority(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
