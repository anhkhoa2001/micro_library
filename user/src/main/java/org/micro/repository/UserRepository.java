package org.micro.repository;

import org.micro.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {

    @Query(value="select * from mu_user m where m.username= :username", nativeQuery=true)
    UserModel findUserModelByUsername(@Param("username") String username);

    List<UserModel> findUserModelsByIdIsIn(List<Integer> ids);
}
