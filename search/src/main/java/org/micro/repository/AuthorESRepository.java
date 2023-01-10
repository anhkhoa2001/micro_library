package org.micro.repository;

import org.micro.model.AuthorES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorESRepository extends ElasticsearchRepository<AuthorES, Integer> {

    AuthorES getAuthorESById(Integer id);
}
