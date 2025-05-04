package com.linkedin_clone_application.service;

import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getEmail());
        return userRepo.save(user);
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User login(User user) {
        return userRepo.findByEmail(user.getEmail());
    }

    public User findById(int id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public void saveUser(User user) {
        userRepo.save(user);
    }

    @Override
    public User findByfirstName(String firstName) {
        return userRepo.findByfirstName(firstName);
    }

    @Override
    public List<User> searchUsersByName(String searchName) {
        return userRepo.searchByName(searchName);
    }
}