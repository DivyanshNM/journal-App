package net.Divyansh.journal.App.controller;
import net.Divyansh.journal.App.JournalApplication;
import net.Divyansh.journal.App.Repository.journalEntryRepository;
import net.Divyansh.journal.App.entity.User;
import net.Divyansh.journal.App.entity.journalEntry;
import net.Divyansh.journal.App.service.UserService;
import net.Divyansh.journal.App.service.journalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class journalEntryController {

    @Autowired
    private journalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/getNextTask/{userName}")
    public ResponseEntity<journalEntry> getNextTask(@PathVariable String userName){
        Optional<journalEntry> opt=journalEntryService.getNextEntryOfUser(userName);
        if(opt.isPresent()) return new ResponseEntity<>(opt.get(),HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/postJournal/{userName}")
    public ResponseEntity<journalEntry> createEntry(@RequestBody journalEntry myEntry,@PathVariable String userName){
        try{
            boolean done=journalEntryService.saveEntry(myEntry,userName);
            if(done) return new ResponseEntity<>(myEntry,HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("deleteById/{username}/{myid}")
    public boolean  deleteJournalById(@PathVariable ObjectId myid,@PathVariable String username){
        journalEntryService.deleteById(myid,username);
        return true;
    }
    @PutMapping("updateById/{id}")
    public journalEntry updateById(@PathVariable ObjectId id,@RequestBody journalEntry newEntry){
        journalEntry old= journalEntryService.findById(id).orElse(null);
        if(old!=null){
            old.setTitle(!newEntry.getTitle().isEmpty() ?newEntry.getTitle(): old.getTitle());
            old.setContent(newEntry.getContent()!=null && !newEntry.getContent().isEmpty() ?newEntry.getContent(): old.getContent());
        }
        return old;
    }
}
