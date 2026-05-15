package net.Edigest.journal.App.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

@Document(collection = "Users")
@Data
@NoArgsConstructor
public class User {
//    @Id
//    private ObjectId id;
    @Indexed(unique = true)
    @NonNull
    private String userName;
    @NonNull
    private String Password;

    @DBRef
    PriorityQueue<journalEntry> journalEntries=new PriorityQueue<>((a,b)->b.getPriority()-a.getPriority());
//    List<journalEntry> journalEntries=new ArrayList<>();

}
