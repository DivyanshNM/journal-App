package net.Edigest.journal.App.Repository;
import net.Edigest.journal.App.entity.User;
import net.Edigest.journal.App.entity.journalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUserName(String userName);
    void deleteByUserName(String userName);
}