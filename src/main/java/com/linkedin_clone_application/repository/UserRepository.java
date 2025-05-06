package com.linkedin_clone_application.repository;


import com.linkedin_clone_application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    User findByfirstName(String currentUsername);

    @Query("SELECT u FROM User u WHERE (LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%'))) AND u.id <> :currentUserId")
    List<User> searchByNameExcludingCurrentUser(@Param("name") String searchName,
                                                @Param("currentUserId") int currentUserId);

    @Query("SELECT u FROM User u WHERE u.id != :id")
    List<User> findAllExcept(@Param("id") int id);
}