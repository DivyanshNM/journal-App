package net.Edigest.journal.App.service;

import net.Edigest.journal.App.Repository.UserRepository;
import net.Edigest.journal.App.Repository.journalEntryRepository;
import net.Edigest.journal.App.entity.User;
import net.Edigest.journal.App.entity.journalEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void saveEntry(User user){
        userRepository.save(user);
    }

    public List<User>  getAll(){
        return userRepository.findAll();
    }
    public Optional<User> findById(ObjectId id){
        return userRepository.findById(id);
    }
    public void deleteByName(String username){
        userRepository.deleteByUserName(username);
    }
    public User findUserByName(String UserName){
        return userRepository.findByUserName(UserName);
    }
}
