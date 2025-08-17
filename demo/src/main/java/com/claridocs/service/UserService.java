package com.claridocs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.claridocs.domain.User;
import com.claridocs.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(User user) {
        // Hash password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> authenticateUser(String email, String rawPassword) {
        System.out.println("DEBUG: Attempting to authenticate user with email: " + email);
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("DEBUG: User found with email: " + user.getEmail());
            System.out.println("DEBUG: Stored password hash: " + user.getPassword());
            System.out.println("DEBUG: Raw password provided: " + rawPassword);

            boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
            System.out.println("DEBUG: Password matches: " + matches);

            if (matches) {
                return userOpt;
            }
        } else {
            System.out.println("DEBUG: No user found with email: " + email);
        }
        return Optional.empty();
    }

    public void changePassword(UUID userId, String newPassword) {
        Optional<User> userOpt = getUserById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            updateUser(user);
        }
    }
}