package net.Divyansh.journal.App.controller;

import net.Divyansh.journal.App.entity.User;
import net.Divyansh.journal.App.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserService userService ;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAll();
    }
    @PostMapping
    public boolean createUser(@RequestBody User myEntry){
        return userService.saveEntry(myEntry);
    }

    @GetMapping("/userName/{name}")
    public ResponseEntity<User> getUserEntryByName(@PathVariable String name){
        return userService.findUserByName(name);
    }

    @DeleteMapping("/deleteUser/{username}")
    public ResponseEntity<Void> deleteByUsername(@PathVariable String username){
        return userService.deleteByUsername(username);
    }
    @PutMapping("/putUser/{name}")
    public User updateById(@PathVariable String name,@RequestBody User newEntry){
        Optional<User> old= Optional.ofNullable(userService.findUserByName(name).getBody());
        if(old.isPresent()){
            old.get().setPassword(!newEntry.getPassword().isEmpty() ?passwordEncoder.encode(newEntry.getPassword()):old.get().getPassword());
            old.get().setUserName(!newEntry.getUserName().isEmpty() ?newEntry.getUserName():old.get().getUserName());
        }
        userService.saveEntry(old.get());
        return old.get();
    }

}
