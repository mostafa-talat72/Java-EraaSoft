package com.tasklec11.controller;

import com.tasklec11.dto.UserDTO;
import com.tasklec11.dto.UserSimpleDTO;
import com.tasklec11.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserSimpleDTO> createUser(@Valid @RequestBody UserSimpleDTO userSimpleDTO){
        userSimpleDTO = userService.createUser(userSimpleDTO);
        return ResponseEntity.created(URI.create("/users/" + userSimpleDTO.getId())).body(userSimpleDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSimpleDTO> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserSimpleDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public  ResponseEntity<UserSimpleDTO> updateUser(@PathVariable Long id,@Valid @RequestBody UserSimpleDTO userSimpleDTO){
        return ResponseEntity.ok(userService.updateUser(id, userSimpleDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usersWithPost")
    public ResponseEntity<List<UserDTO>> getAllUsersWithPost(){
        return ResponseEntity.ok(userService.getAllUsersWithPost());
    }

    @GetMapping("/usersWithPost/{id}")
    public ResponseEntity<UserDTO> getUserWithPostById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserWithPostById(id));
    }
}
