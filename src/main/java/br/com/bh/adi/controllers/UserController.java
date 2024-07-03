package br.com.bh.adi.controllers;

import br.com.bh.adi.controllers.dto.CreateAccountDTO;
import br.com.bh.adi.controllers.dto.CreateUserDTO;
import br.com.bh.adi.controllers.dto.UpdateUserDTO;
import br.com.bh.adi.entities.User;
import br.com.bh.adi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDTO createUserDTO) {
        var userId = userService.createUser(createUserDTO);

        return ResponseEntity.created(URI.create("/api/users/" + userId.toString())).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        var user = userService.getUserById(userId);

        if(user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers(){
        var users = userService.listUsers();

        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUserById(@PathVariable String userId, @RequestBody UpdateUserDTO updateUserDTO){
        userService.updateUserById(userId, updateUserDTO);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable String userId){
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/accounts")
    public ResponseEntity<Void> deleteById(@PathVariable String userId, @RequestBody CreateAccountDTO createAccountDTO){
        userService.createAccount(userId, createAccountDTO);
        return ResponseEntity.ok().build();
    }


}
