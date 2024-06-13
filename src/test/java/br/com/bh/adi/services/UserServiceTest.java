package br.com.bh.adi.services;

import br.com.bh.adi.controllers.CreateUserDTO;
import br.com.bh.adi.entities.User;
import br.com.bh.adi.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    //Padrão Triplo A: Arrange - Act - Assert

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Nested
    class createUser{
        @Test
        @DisplayName("Should create a user with success!")
        void shouldCreateUserWithSuccess(){

            //Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password123",
                    Instant.now(),
                    null);
            doReturn(user).when(userRepository).save(any());

            var input = new CreateUserDTO(
                    "username",
                    "email@email.com",
                    "password123");

            //Act
            var output = userService.createUser(input);

            //Assert
            assertNotNull(output);

        }
    }
}