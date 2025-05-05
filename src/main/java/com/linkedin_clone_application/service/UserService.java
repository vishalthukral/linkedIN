package com.linkedin_clone_application.service;

import com.linkedin_clone_application.model.User;

import java.util.List;

public interface UserService {
    User registerNewUser(User user);

    User findByEmail(String email);

    User findById(int id);

    void saveUser(User user);

    User findByfirstName(String firstName);

    List<User> searchUsersByName(String searchName, int userId);

    List<User> findAllExcept(User user);
}