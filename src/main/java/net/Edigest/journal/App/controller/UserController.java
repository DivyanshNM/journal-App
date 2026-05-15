package net.Edigest.journal.App.controller;

import net.Edigest.journal.App.Repository.UserRepository;
import net.Edigest.journal.App.entity.User;
import net.Edigest.journal.App.entity.journalEntry;
import net.Edigest.journal.App.service.UserService;
import net.Edigest.journal.App.service.journalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")

public class UserController {

    @Autowired
    private UserService userService ;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAll();
    }
    @PostMapping
    public boolean createUser(@RequestBody User myEntry){
        if(userService.findUserByName(myEntry.getUserName())==null){
            userService.saveEntry(myEntry);
            return true;
        }else return false;
    }
//    @GetMapping("/id/{myid}")
//    public ResponseEntity<User> getUserEntryById(@PathVariable ObjectId myid){
//        Optional<User> user=userService.findById(myid);
//        if(user.isPresent()){
//            return new ResponseEntity<>(user.get(), HttpStatus.OK);
//        }else{
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
    @GetMapping("/userName/{name}")
    public ResponseEntity<User> getUserEntryByName(@PathVariable String name){
        User userInDb=userService.findUserByName(name);
        if(userInDb!=null){
            return new ResponseEntity<>(userInDb,HttpStatus.OK);
        }else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
//    @DeleteMapping("id/{myid}")
//    public ResponseEntity<User>  deleteUserById(@PathVariable ObjectId myid){
//        if(userService.findById(myid).isPresent()){
//            userService.deleteById(myid);
//            return new ResponseEntity<>(HttpStatus.OK);
//        }else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
    @DeleteMapping("{username}")
    public ResponseEntity<User>  deleteUserByName(@PathVariable String username){
        if(userService.findUserByName(username)!=null){
            User userInDb=userService.findUserByName(username);
            userService.deleteByName(userInDb.getUserName());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("name/{name}")
    public User updateById(@PathVariable String name,@RequestBody User newEntry){
        Optional<User> old= Optional.ofNullable(userService.findUserByName(name));
        if(old.isPresent()){
            old.get().setPassword(newEntry.getPassword()!=null && !newEntry.getPassword().equals("")?newEntry.getPassword():old.get().getPassword());
            old.get().setUserName(newEntry.getUserName()!=null && !newEntry.getUserName().equals("")?newEntry.getUserName():old.get().getUserName());
        }
        userService.saveEntry(old.get());
        return old.get();
    }

}
