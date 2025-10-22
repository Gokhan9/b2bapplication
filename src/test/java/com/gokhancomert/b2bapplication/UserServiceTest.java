package com.gokhancomert.b2bapplication;

import com.gokhancomert.b2bapplication.dto.UserDto;
import com.gokhancomert.b2bapplication.dto.request.UserCreateRequest;
import com.gokhancomert.b2bapplication.dto.request.UserRegisterRequest;
import com.gokhancomert.b2bapplication.dto.request.UserUpdateRequest;
import com.gokhancomert.b2bapplication.exception.InvalidCredentialsException;
import com.gokhancomert.b2bapplication.exception.ResourceNotFoundException;
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

    //ID ile Kullanıcı Getirme (Başarısız): getById() metodunun, mevcut olmayan bir ID verildiğinde ResourceNotFoundException fırlattığını test et.
    @Test
    void getById_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getById(1L));

        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, never()).toDto(any(User.class));
    }

    //Yeni Kullanıcı Oluşturma: createUser() metodunun, yeni bir kullanıcı oluşturma isteğini doğru bir şekilde işleyip oluşturulan UserDto'yu döndürdüğünü test et.
    @Test
    void createUser_shouldCreateAndReturnUserDto() {
        User newUser = new User(null, "newUser", "newUser@example.com", "encodedpassword", Set.of("USER"));
        User savedUser = new User(2L, "newUser", "newUser@example.com", "encodedpassword", Set.of("USER"));
        UserDto savedUserDto = new UserDto("newUser", "newUser@example.com", "encodedpassword", Set.of("USER"));

        when(userMapper.toUser(userCreateRequest)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);

        UserDto result = userService.createUser(userCreateRequest);

        assertNotNull(result);
        assertEquals(savedUserDto, result);

        verify(userMapper, times(1)).toUser(userCreateRequest);
        verify(userRepository, times(1)).save(newUser);
        verify(userMapper, times(1)).toDto(savedUser);
    }

    //Kullanıcı Kaydı: registerUser() metodunun, yeni bir kullanıcı kaydı isteğini doğru bir şekilde işleyip oluşturulan UserDto'yu döndürdüğünü test et.
    @Test
    void registerUser_shouldRegisterAndReturnUserDto() {
        User newUser = new User(null, "newuser", "newuser@example.com", "encodedpassword", Set.of("USER"));
        User savedUser = new User(2L, "newuser", "newuser@example.com", "encodedpassword", Set.of("USER"));
        UserDto savedUserDto = new UserDto("newuser", "newuser@example.com", "encodedpassword", Set.of("USER"));

        when(userMapper.toUser(userRegisterRequest)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);

        UserDto result = userService.registerUser(userRegisterRequest);

        assertNotNull(result);
        assertEquals(savedUserDto, result);

        verify(userMapper, times(1)).toUser(userRegisterRequest);
        verify(userRepository, times(1)).save(newUser);
        verify(userMapper, times(1)).toDto(savedUser);
    }

    //Kullanıcı Girişi (Başarılı): loginUser() metodunun, geçerli kimlik bilgileri verildiğinde User nesnesini döndürdüğünü test et.
    @Test
    void loginUser_shouldReturnUser_whenCredentialsAreValid() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedpassword")).thenReturn(true);

        User result = userService.loginUser("testuser", "password");

        assertNotNull(result);
        assertEquals(user, result);

        verify(userRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, times(1)).matches("password", "encodedpassword");
    }

    //Kullanıcı Girişi (Kullanıcı Bulunamadı): loginUser() metodunun, mevcut olmayan bir kullanıcı adı verildiğinde ResourceNotFoundException fırlattığını test et.
    @Test
    void loginUser_shouldThrowResourceNotFoundException_whenUserDoesNotFound() {
        when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.loginUser("unknownuser", "password"));

        verify(userRepository, times(1)).findByUsername("unknownuser");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    //Kullanıcı Girişi (Yanlış Şifre): loginUser() metodunun, yanlış şifre verildiğinde InvalidCredentialsException fırlattığını test et.
    @Test
    void loginUser_shouldThrowInvalidCredentialsException_whenPasswordIsIncorrect() {
        when(userRepository.findByUsername("passwordIsIncorrect")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedpassword")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> userService.loginUser("passwordIsIncorrect",  "password"));

        verify(userRepository, times(1)).findByUsername("passwordIsIncorrect");
        verify(passwordEncoder, times(1)).matches("password", "encodedpassword");
    }

    //Kullanıcı Güncelleme (Başarılı): updateUserById() metodunun, mevcut bir kullanıcıyı verilen bilgilerle güncelleyip güncellenmiş UserDto'yu döndürdüğünü test et.
    @Test
    void updateUserById_shouldUpdateAndReturnUserDto_whenUserExists() {
        User updatedUser = new User(1L, "updatedUser", "updatedUser@example.com", "encodedpassword", Set.of("ADMIN"));
        UserDto updatedUserDto = new UserDto("updatedUser", "updatedUser@example.com", "encodedpassword", Set.of("ADMIN"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userMapper).updateUserFromDto(userUpdateRequest, user);
        when(userRepository.save(user)).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(updatedUserDto);

        UserDto result = userService.updateUserById(1L, userUpdateRequest);

        assertNotNull(result);
        assertEquals(updatedUserDto, result);

        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).updateUserFromDto(userUpdateRequest, user);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(updatedUser);
    }

    //Kullanıcı Güncelleme (Başarısız): updateUserById() metodunun, mevcut olmayan bir kullanıcıyı güncellemeye çalışırken ResourceNotFoundException fırlattığını test et.
    @Test
    void updateUserById_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUserById(1L, userUpdateRequest));

        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, never()).updateUserFromDto(any(), any());
        verify(userRepository, never()).save(any());
    }

    //Kullanıcı Silme (Başarılı): deleteUserById() metodunun, mevcut bir kullanıcıyı başarıyla sildiğini ve herhangi bir hata döndürmediğini test et.
    @Test
    void deleteUserById_shouldDeleteUser_whenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> userService.deleteUserById(1L));

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    //Kullanıcı Silme (Başarısız): deleteUserById() metodunun, mevcut olmayan bir kullanıcıyı silmeye çalışırken ResourceNotFoundException fırlattığını test et.
    @Test
    void deleteUserById_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUserById(1L));

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, never()).deleteById(anyLong());
    }
}
