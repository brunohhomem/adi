package br.com.bh.adi.services;

import br.com.bh.adi.controllers.CreateUserDTO;
import br.com.bh.adi.entities.User;
import br.com.bh.adi.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    //Padrão Triplo A: Arrange - Act - Assert

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

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
            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            var input = new CreateUserDTO(
                    "username",
                    "email@email.com",
                    "password123");

            //Act
            var output = userService.createUser(input);

            //Assert
            assertNotNull(output);

            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(input.username(), userCaptured.getUsername());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());
        }

        @Test
        @DisplayName("Should throw exception when error occurs!")
        void shouldThrowExceptionWhenErrorOccurs(){

            //Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password123",
                    Instant.now(),
                    null);
            doThrow(new RuntimeException()).when(userRepository).save(any());

            var input = new CreateUserDTO(
                    "username",
                    "email@email.com",
                    "password123");

            //Act & Assert
            assertThrows(RuntimeException.class, () -> userService.createUser(input));
        }
    }

    @Nested
    class getUserById{

        @Test
        @DisplayName("Should get user by id with success when optional is present!")
        void shouldGetUserByIdWithSuccessWhenOptionalIsPresent(){
            //Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password123",
                    Instant.now(),
                    null);
            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());
            //Act

            var output = userService.getUserById(user.getUserId().toString());

            //Assert
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }
    }

    @Test
    @DisplayName("Should get user by id with success when optional is empty!")
    void shouldGetUserByIdWithSuccessWhenOptionalIsEmpty(){
        //Arrange
        var userId = UUID.randomUUID();

        doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());
        //Act

        var output = userService.getUserById(userId.toString());

        //Assert
        assertTrue(output.isEmpty());
        assertEquals(userId, uuidArgumentCaptor.getValue());
    }

    @Nested
    class listUsers {
        @Test
        @DisplayName("Should return all users with success")
        void shouldReturnAllUsersWithSuccess() {
            //Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password123",
                    Instant.now(),
                    null);

            var userList = List.of(user);

            doReturn(userList).when(userRepository).findAll();
            //Act
            var output = userService.listUsers();
            //Assert
            assertNotNull(output);
            assertEquals(userList.size(), output.size());
        }
    }

}