package org.micro.service;

import org.micro.model.Authority;

import java.util.List;
import java.util.Set;

public interface AuthorityService {
    Authority findById(Integer id);

    List<Authority> findAllById(Set<Integer> authority_ids);

    Authority add(Authority authority);

    Authority getByName(String name);
}
