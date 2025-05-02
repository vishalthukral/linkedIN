package com.linkedin_clone_application.service;

import com.linkedin_clone_application.model.User;

public interface UserService {
    User registerNewUser(User user);
    User findByEmail(String email);
    User findById(int id);
    void saveUser(User user);
    User findByfirstName(String firstName);
}
