package com.gokhancomert.b2bapplication.service;

import com.gokhancomert.b2bapplication.dto.UserDto;
import com.gokhancomert.b2bapplication.dto.request.UserCreateRequest;
import com.gokhancomert.b2bapplication.dto.request.UserRegisterRequest;
import com.gokhancomert.b2bapplication.dto.request.UserUpdateRequest;
import com.gokhancomert.b2bapplication.exception.InvalidCredentialsException;
import com.gokhancomert.b2bapplication.exception.ResourceNotFoundException;
import com.gokhancomert.b2bapplication.mapper.UserMapper;
import com.gokhancomert.b2bapplication.model.User;
import com.gokhancomert.b2bapplication.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public List<UserDto> findAll() {
        logger.info("Attempting to find all users.");
        List<User> users = userRepository.findAll();
        logger.info("Found {} users.",users.size());
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public UserDto getById(Long id) {
        logger.info("Attempting to find user by id: {}", id);
        return userRepository.findById(id)
                .map(user -> {
                    logger.info("Successfully found user with id: {}", id);
                    return userMapper.toDto(user);
                })
                .orElseThrow(() -> {
                    logger.warn("User not found with id: {}", id);
                    return new ResourceNotFoundException("User not found with this id: " + id);
                });
    }

    public UserDto createUser(UserCreateRequest createRequest) {
        logger.info("Attempting to create a new user with username: {}", createRequest.getUsername());
        User user = userMapper.toUser(createRequest);
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of("USER"));
            logger.debug("Assigned default 'USER' role to new user: {}", user.getUsername());
        }
        User savedUser = userRepository.save(user);
        logger.info("Successfully created new user with id: {} and username: {}", savedUser.getId(), savedUser.getUsername());
        return userMapper.toDto(savedUser);
    }

    public UserDto registerUser(UserRegisterRequest registerRequest) {
        logger.info("Attempting to register a new user with username: {}", registerRequest.getUsername());
        User user = userMapper.toUser(registerRequest);
        user.setRoles(Set.of("USER")); // Default role for registered users
        logger.debug("Assigned default 'USER' role to registered user: {}", user.getUsername());
        User savedUser = userRepository.save(user);
        logger.info("Successfully registered new user with id: {} and username: {}", savedUser.getId(), savedUser.getUsername());
        return userMapper.toDto(savedUser);
    }

    public User loginUser(String username, String password) {
        logger.info("Login attempt for username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Login failed: User not found with username: {}", username);
                    return new ResourceNotFoundException("User not found with username: " + username);
                });

        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("Login failed: Invalid password for username: {}", username);
            throw new InvalidCredentialsException("Invalid username or password.");
        }
        logger.info("User '{}' successfully logged in.", username);
        return user;
    }

    public UserDto updateUserById(Long id, UserUpdateRequest updateRequest) {
        logger.info("Attempting to update user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Update failed: User not found with id: {}", id);
                    return new ResourceNotFoundException("User not found with this id: " + id);
                });

        userMapper.updateUserFromDto(updateRequest, user);
        User updatedUser = userRepository.save(user);
        logger.info("Successfully updated user with id: {}", id);
        return userMapper.toDto(updatedUser);
    }

    public void deleteUserById(Long id) {
        logger.info("Attempting to delete user with id: {}", id);
        if (!userRepository.existsById(id)) {
            logger.warn("Delete failed: User not found with id: {}", id);
            throw new ResourceNotFoundException("User not found with this id: " + id);
        }
        userRepository.deleteById(id);
        logger.info("Successfully deleted user with id: {}", id);
    }
}