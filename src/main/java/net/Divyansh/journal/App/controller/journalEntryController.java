package net.Divyansh.journal.App.controller;
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

@RestController
@RequestMapping("/journal")
public class journalEntryController {

    @Autowired
    private journalEntryService journalEntryService;

    @Autowired
    private UserService userService;
    @GetMapping("/getAll/{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName){
        User user=userService.findUserByName(userName);
        if(user==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<journalEntry> all=user.getJournalEntries();
        if(all!=null && !all.isEmpty()) {
            all.sort((a,b)->b.getPriority()-a.getPriority());
            return new ResponseEntity<>(all,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/postJournal/{userName}")
    public ResponseEntity<journalEntry> createEntry(@RequestBody journalEntry myEntry,@PathVariable String userName){
        try{
            journalEntryService.saveEntry(myEntry,userName);
            return new ResponseEntity<>(myEntry,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
//    @GetMapping("/id/{myid}")
//    public ResponseEntity<journalEntry> getJournalEntryById(@PathVariable ObjectId myid){
//        Optional<journalEntry> journalEntry=journalEntryService.findById(myid);
//        if(journalEntry.isPresent()){
//            return new ResponseEntity(journalEntry.get(), HttpStatus.OK);
//        }else{
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
    @DeleteMapping("deleteById/{username}/{myid}")
    public boolean  deleteJournalById(@PathVariable ObjectId myid,@PathVariable String userName){
        journalEntryService.deleteById(myid,userName);
        return true;
    }
    @PutMapping("updateById/{id}")
    public journalEntry updateById(@PathVariable ObjectId id,@RequestBody journalEntry newEntry){
        journalEntry old= journalEntryService.findById(id).orElse(null);
        if(old!=null){
            old.setTitle(!newEntry.getTitle().isEmpty() ?newEntry.getTitle(): old.getTitle());
            old.setContent(newEntry.getContent()!=null && !newEntry.getContent().isEmpty() ?newEntry.getContent(): old.getContent());
        }
//        journalEntryService.saveEntry(old);
        return old;
    }

}
