package com.gokhancomert.b2bapplication.service;

import com.gokhancomert.b2bapplication.dto.UserDto;
import com.gokhancomert.b2bapplication.exception.ResourceNotFoundException;
import com.gokhancomert.b2bapplication.mapper.UserMapper;
import com.gokhancomert.b2bapplication.model.User;
import com.gokhancomert.b2bapplication.repository.UserRepository;
import com.gokhancomert.b2bapplication.request.UserCreateRequest;
import com.gokhancomert.b2bapplication.request.UserUpdateRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new RuntimeException("User not found with this id: " + id));
    }

    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of("USER"));
        }

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserDto updateUserById(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with this id: " + id));

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        if (userDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
            user.setRoles(userDto.getRoles());
        }

        return userMapper.toDto(userRepository.save(user));
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with this id: " + id);
        }
        userRepository.deleteById(id);
    }
}
