package org.micro.repository;

import org.micro.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    Authority getAuthorityByAuthority(String authority);
}
