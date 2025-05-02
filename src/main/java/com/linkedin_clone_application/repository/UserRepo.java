package com.linkedin_clone_application.repository;


import com.linkedin_clone_application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    User findByfirstName(String currentUsername);
}
