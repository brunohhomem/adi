package br.com.bh.adi.services;

import br.com.bh.adi.controllers.dto.CreateAccountDTO;
import br.com.bh.adi.controllers.dto.CreateUserDTO;
import br.com.bh.adi.controllers.dto.UpdateUserDTO;
import br.com.bh.adi.entities.Account;
import br.com.bh.adi.entities.BillingAddress;
import br.com.bh.adi.entities.User;
import br.com.bh.adi.repositories.AccountRepository;
import br.com.bh.adi.repositories.BillingAddressRepository;
import br.com.bh.adi.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BillingAddressRepository billingAddressRepository;

    public UUID createUser(CreateUserDTO createUserDTO){

        // DTO -> ENTITY
        var entity = new User(
                UUID.randomUUID(),
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

    public void updateUserById(String userId, UpdateUserDTO updateUserDTO){
        var id = UUID.fromString(userId);

        var userEntity = userRepository.findById(id);

        if(userEntity.isPresent()){
            var user = userEntity.get();

            //ao fazer essas verificações ele não altera campos que eu não atualizei (null)
            if(updateUserDTO.username() != null){
                user.setUsername(updateUserDTO.username());
            }

            if(updateUserDTO.password() != null){
                user.setPassword(updateUserDTO.password());
            }

            userRepository.save(user);
        }
    }

    public void deleteById(String userId) {
        var id = UUID.fromString(userId);

        var userExists = userRepository.existsById(id);
        if(userExists) {
            userRepository.deleteById(id);
        }

    }

    public void createAccount(String userId, CreateAccountDTO createAccountDTO) {

        //Verificar se o usuario existe
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND));

        // DTO -> Entity
        var account = new Account(
            UUID.randomUUID(),
            createAccountDTO.description(),
            user,
            null,
            new ArrayList<>()
        );

        var accountCreated = accountRepository.save(account);

        var billingAddress = new BillingAddress(
                accountCreated.getAccountId(),
                account,
                createAccountDTO.street(),
                createAccountDTO.number()
        );

        billingAddressRepository.save(billingAddress);
    }
}
