package com.gokhancomert.b2bapplication;

import com.gokhancomert.b2bapplication.dto.UserDto;
import com.gokhancomert.b2bapplication.dto.request.UserCreateRequest;
import com.gokhancomert.b2bapplication.dto.request.UserRegisterRequest;
import com.gokhancomert.b2bapplication.dto.request.UserUpdateRequest;
import com.gokhancomert.b2bapplication.mapper.UserMapper;
import com.gokhancomert.b2bapplication.model.User;
import com.gokhancomert.b2bapplication.repository.UserRepository;
import com.gokhancomert.b2bapplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;
    private UserCreateRequest userCreateRequest;
    private UserRegisterRequest userRegisterRequest;
    private UserUpdateRequest userUpdateRequest;

    @BeforeEach
    void setUp() {
        user = new User(1L, "testuser2", "test@example.com", "encodedpassword", Set.of("USER"));
        userDto = new UserDto("testuser2", "test@example.com", "encodedpassword", Set.of("USER"));
        userCreateRequest = new UserCreateRequest("newuser", "newuser@example.com", "newpass", Set.of("USER"));
        userRegisterRequest = new UserRegisterRequest("registeruser", "register@example.com", "registerpass");
        userUpdateRequest = new UserUpdateRequest("updateduser", "updatedpass", Set.of("ADMIN"));
    }

    //Tüm Kullanıcıları Getirme: findAll() metodunun, veritabanındaki tüm kullanıcıları UserDto listesi olarak doğru bir şekilde döndürdüğünü test et.
    @Test
    void findAll_shouldReturnAllUsersAsDtoList() {
        User user1 = new User(2L, "user1", "user@example.com", "encodedpassword", Set.of("USER"));
        UserDto userDto1 = new UserDto("user1", "user@example.com", "encodedpassword", Set.of("USER"));

        List<User> users = Arrays.asList(user, user1);
        List<UserDto> userDtos = Arrays.asList(userDto, userDto1);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(user)).thenReturn(userDto);
        when(userMapper.toDto(user1)).thenReturn(userDto1);

        List<UserDto> result = userService.findAll();

        assertNotNull(result);
        assertEquals(2L, result.size());
        assertEquals("testuser2", result.get(0).getUsername());
        assertEquals("user1", result.get(1).getUsername());

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(2)).toDto(any(User.class));
    }

    //ID ile Kullanıcı Getirme (Başarılı): getById() metodunun, mevcut bir ID verildiğinde ilgili UserDto nesnesini döndürdüğünü test et.
    @Test
    void getById_shouldReturnUserDto_whenUserExists() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.getById(1L);

        assertNotNull(result);
        assertEquals(userDto, result);

        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toDto(user);
    }
}
