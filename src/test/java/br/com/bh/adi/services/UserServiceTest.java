package br.com.bh.adi.services;

import br.com.bh.adi.controllers.dto.CreateUserDTO;
import br.com.bh.adi.controllers.dto.UpdateUserDTO;
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
import static org.mockito.Mockito.*;

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

    @Nested
    class deleteById{
        @Test
        @DisplayName("Should delete user with success when user exists")
        void shouldDeleteUserWithSuccessWhenUserExists() {
            //Arrange
            doReturn(true).when(userRepository).existsById(uuidArgumentCaptor.capture());
            doNothing().when(userRepository).deleteById(uuidArgumentCaptor.capture());
            var userId = UUID.randomUUID();
            //Act

            userService.deleteById(userId.toString());

            //Assert
            var idList = uuidArgumentCaptor.getAllValues();
            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));

            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).deleteById(idList.get(1));
        }

        @Test
        @DisplayName("Should delete user with success when user not exists")
        void shouldNotDeleteUserWhenUserNotExists() {
            //Arrange
            doReturn(false).when(userRepository).existsById(uuidArgumentCaptor.capture());
            var userId = UUID.randomUUID();
            //Act

            userService.deleteById(userId.toString());

            //Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1)).existsById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).deleteById(any());
        }
    }

    @Nested
    class updateUserById {

        @Test
        @DisplayName("Should update user by id when user exists and username and passwords is filled!")
        void shouldUpdateUserByIdWhenUserExistsAndUsernameAndPasswordIsFilled() {
            //Arrange
            var updateUserDto = new UpdateUserDTO(
                    "newUsername",
                    "newPassword"
            );

            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "password123",
                    Instant.now(),
                    null);
            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());
            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
            //Act

            userService.updateUserById(user.getUserId().toString(), updateUserDto);

            //Assert
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());

            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(updateUserDto.username(), userCaptured.getUsername());
            assertEquals(updateUserDto.password(), userCaptured.getPassword());

            verify(userRepository, times(1))
                    .findById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(1))
                    .save(user);
        }

        @Test
        @DisplayName("Should not update user when user not exists")
        void shouldNotUpdateUserByIdWhenUserNotExists() {
            //Arrange
            var updateUserDto = new UpdateUserDTO(
                    "newUsername",
                    "newPassword"
            );

            var userId = UUID.randomUUID();

            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());
            //Act

            userService.updateUserById(userId.toString(), updateUserDto);

            //Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1))
                    .findById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).save(any());
        }
    }
}