package br.com.bh.adi.controllers;

import br.com.bh.adi.entities.User;
import br.com.bh.adi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
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
        //
        return null;
    }

}
