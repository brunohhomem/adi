package br.com.bh.adi.services;

import br.com.bh.adi.controllers.CreateUserDTO;
import br.com.bh.adi.entities.User;
import br.com.bh.adi.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UUID createUser(CreateUserDTO createUserDTO){

        //DTO -> Entity
        var entity = new User(
                null,
                createUserDTO.username(),
                createUserDTO.email(),
                createUserDTO.password(),
                Instant.now(),
                null);

        var userSaved = userRepository.save(entity);
        return userSaved.getUserId();
    }

    public Optional<User> getUserById(String userId){
       return userRepository.findById(UUID.fromString(userId));
    }

    public List<User> listUsers() {
        return  userRepository.findAll();
    }
}
