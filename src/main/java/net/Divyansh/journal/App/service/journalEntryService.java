package net.Divyansh.journal.App.service;

import net.Divyansh.journal.App.Repository.journalEntryRepository;
import net.Divyansh.journal.App.entity.User;
import net.Divyansh.journal.App.entity.journalEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class journalEntryService {
    @Autowired
    private journalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(journalEntry journalEntry, String userName) {
        User user = userService.findUserByName(userName).getBody();
        journalEntry.setDate(LocalDateTime.now());
        journalEntry saved = journalEntryRepository.save(journalEntry);
        user.getJournalEntries().add(saved);
        userService.saveEntry(user);
    }

    public List<journalEntry> getAllEntriesOfUser(String userName) {
        Optional<User> user = Optional.ofNullable(userService.findUserByName(userName).getBody());
        if(user.isPresent())return user.get().getJournalEntries();
        else return Collections.emptyList();
    }

    public Optional<journalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public void deleteById(ObjectId id, String userName) {
        User user = userService.findUserByName(userName).getBody();
        assert user != null;
        user.getJournalEntries().removeIf(x -> x.getId() == id);
        userService.saveEntry(user);
        journalEntryRepository.deleteById(id);
    }
}