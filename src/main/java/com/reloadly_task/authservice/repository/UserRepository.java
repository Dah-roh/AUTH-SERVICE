package com.reloadly_task.authservice.repository;
import com.reloadly_task.authservice.model.UserDao;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserDao, Integer> {
    UserDao findByUsername(String username);
}