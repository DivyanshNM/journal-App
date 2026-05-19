package net.Divyansh.journal.App.Repository;
import net.Divyansh.journal.App.entity.journalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface journalEntryRepository extends MongoRepository<journalEntry, ObjectId> {

}