package net.Divyansh.journal.App.service;

import net.Divyansh.journal.App.Repository.UserRepository;
import net.Divyansh.journal.App.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public boolean saveEntry(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(userRepository.findByUserName(user.getUserName())==null){
            userRepository.save(user);
            return true;
        }else return false;
    }
    public void addEntryInUser(User user){
        userRepository.save(user);
    }
    public List<User>  getAll(){
        return userRepository.findAll();
    }
    public Optional<User> findById(ObjectId id){
        return userRepository.findById(id);
    }
    public ResponseEntity<Void> deleteByUsername(String username){
        Optional<User> user=Optional.ofNullable(userRepository.findByUserName(username));
        if(user.isPresent()){
            userRepository.deleteByUserName(username);
            return new ResponseEntity<>(HttpStatus.OK);
        }else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    public ResponseEntity<User> findUserByName(String userName){
        Optional<User> user=Optional.ofNullable(userRepository.findByUserName(userName));
        if(user.isPresent()) return new ResponseEntity<>(user.get(),HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByUserName(username);
        if(user==null) throw new UsernameNotFoundException("User not found");
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .roles(user.getRoles().toArray(new String[0]))
                .build();
    }
}

