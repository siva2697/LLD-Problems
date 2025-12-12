package splitwise.entities;

import java.util.ArrayList;
import java.util.List;

public class Group {

    String id;
    String name;
    String description;
    List<String> participants;

    public Group(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.participants = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void addParticipant(String userId) {
        this.participants.add(userId);
    }
}
