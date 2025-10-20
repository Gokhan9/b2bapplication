package com.gokhancomert.b2bapplication;

import com.gokhancomert.b2bapplication.mapper.UserMapper;
import com.gokhancomert.b2bapplication.repository.UserRepository;
import com.gokhancomert.b2bapplication.service.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;
}
