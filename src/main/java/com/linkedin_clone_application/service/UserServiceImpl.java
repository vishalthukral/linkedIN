package com.linkedin_clone_application.service;

import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getEmail());
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User login(User user) {
        return userRepository.findByEmail(user.getEmail());
    }

    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByfirstName(String firstName) {
        return userRepository.findByfirstName(firstName);
    }

    @Override
    public List<User> searchUsersByName(String searchName, int userId) {
        return userRepository.searchByNameExcludingCurrentUser(searchName, userId);
    }

    @Override
    public List<User> findAllExcept(User user) {
        return userRepository.findAllExcept(user.getId());
    }
}