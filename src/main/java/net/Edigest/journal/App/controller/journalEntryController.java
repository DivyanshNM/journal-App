package net.Edigest.journal.App.controller;
import net.Edigest.journal.App.entity.User;
import net.Edigest.journal.App.entity.journalEntry;
import net.Edigest.journal.App.service.UserService;
import net.Edigest.journal.App.service.journalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class journalEntryController {

    @Autowired
    private journalEntryService journalEntryService;

    @Autowired
    private UserService userService;
    @GetMapping("{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName){
        User user=userService.findUserByName(userName);
        List<journalEntry> all=user.getJournalEntries();
        if(all!=null && !all.isEmpty()) {
            return new ResponseEntity<>(all,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("{userName}")
    public ResponseEntity<journalEntry> createEntry(@RequestBody journalEntry myEntry,@PathVariable String userName){
        try{
            journalEntryService.saveEntry(myEntry,userName);
            return new ResponseEntity<>(myEntry,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/id/{myid}")
    public ResponseEntity<journalEntry> getJournalEntryById(@PathVariable ObjectId myid){
        Optional<journalEntry> journalEntry=journalEntryService.findById(myid);
        if(journalEntry.isPresent()){
            return new ResponseEntity(journalEntry.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("id/{username}/{myid}")
    public boolean  deleteJournalById(@PathVariable ObjectId myid,@PathVariable String userName){
        journalEntryService.deleteById(myid,userName);
        return true;
    }
    @PutMapping("id/{id}")
    public journalEntry updateById(@PathVariable ObjectId id,@RequestBody journalEntry newEntry){
        journalEntry old= journalEntryService.findById(id).orElse(null);
        if(old!=null){
            old.setTitle(newEntry.getTitle()!=null && !newEntry.getTitle().equals("")?newEntry.getTitle(): old.getTitle());
            old.setContent(newEntry.getContent()!=null && !newEntry.getContent().equals ("")?newEntry.getContent(): old.getContent());
        }
//        journalEntryService.saveEntry(old);
        return old;
    }

}
