package net.Divyansh.journal.App.controller;

import net.Divyansh.journal.App.entity.User;
import net.Divyansh.journal.App.entity.journalEntry;
import net.Divyansh.journal.App.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserService userService ;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers(){
        return userService.getAll();
    }

    @GetMapping("/getUser/{userName}")
    public ResponseEntity<User> getUser(@PathVariable String userName){
        Optional<User> user=Optional.ofNullable(userService.findUserByName(userName).getBody());
        if(user.isPresent()){
            Collections.sort(user.get().getJournalEntries(),(a,b)->b.getPriority()-a.getPriority());
            return new ResponseEntity<>(user.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/createUser")
    public boolean createUser(@RequestBody User myEntry){
        return userService.saveEntry(myEntry);
    }

    @GetMapping("/userName/{name}")
    public ResponseEntity<List<journalEntry>> getUserEntryByName(@PathVariable String name){
        User user= userService.findUserByName(name).getBody();
        assert user != null;
        List<journalEntry> list=user.getJournalEntries();
        Collections.sort(list,(a,b)->b.getPriority()-a.getPriority());
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser/{username}")
    public ResponseEntity<Void> deleteByUsername(@PathVariable String username){
        return userService.deleteByUsername(username);
    }
    @PutMapping("/updateUser/{name}")
    public ResponseEntity<User> updateById(@PathVariable String name,@RequestBody User newEntry){
        Optional<User> old= Optional.ofNullable(userService.findUserByName(name).getBody());
        if(old.isPresent()){
            old.get().setPassword(!newEntry.getPassword().isEmpty() ?passwordEncoder.encode(newEntry.getPassword()):old.get().getPassword());
            old.get().setUserName(!newEntry.getUserName().isEmpty() ?newEntry.getUserName():old.get().getUserName());
            old.get().setRoles(!newEntry.getRoles().isEmpty() ?newEntry.getRoles():old.get().getRoles());
        }
        old.ifPresent(user -> userService.saveEntry(user));
        if(old.isPresent()){
            return new ResponseEntity<>(old.get(),HttpStatus.OK);
        }else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
