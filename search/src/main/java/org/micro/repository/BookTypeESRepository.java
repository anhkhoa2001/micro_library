package org.micro.repository;

import org.micro.model.BookTypeES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookTypeESRepository extends ElasticsearchRepository<BookTypeES, Integer> {
    BookTypeES getBookTypeESById(Integer id);
}
