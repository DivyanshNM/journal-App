package net.Edigest.journal.App.service;

import net.Edigest.journal.App.Repository.journalEntryRepository;
import net.Edigest.journal.App.entity.User;
import net.Edigest.journal.App.entity.journalEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class journalEntryService {
    @Autowired
    private journalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

    public void saveEntry(journalEntry journalEntry,String userName){
        User user=userService.findUserByName(userName);
        journalEntry.setDate(LocalDateTime.now());
        journalEntry saved = journalEntryRepository.save(journalEntry);
        user.getJournalEntries().add(saved);
        userService.saveEntry(user);
    }

    public List<journalEntry>  getAll(){
        return journalEntryRepository.findAll();
    }
    public Optional<journalEntry> findById(ObjectId id){
        return journalEntryRepository.findById(id);
    }
    public void deleteById(ObjectId id){
        journalEntryRepository.deleteById(id);
    }
}
