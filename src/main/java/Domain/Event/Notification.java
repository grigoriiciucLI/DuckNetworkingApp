package Domain.Event;

import Domain.Entity;
import Domain.User.User;

import java.time.LocalDateTime;

public class Notification extends Event {
    User receiver;
    String content;
    LocalDateTime generatedOn;
    LocalDateTime seenOn = null;

    public Notification(User receiver, String content) {
        super();
        this.receiver = receiver;
        this.content = content;
        generatedOn = LocalDateTime.now();
    }

    public Notification(Integer id, User receiver, String content, LocalDateTime generatedOn, LocalDateTime seenOn) {
        super(id);
        this.receiver = receiver;
        this.content = content;
        this.generatedOn = generatedOn;
        this.seenOn = seenOn;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getGeneratedOn() {
        return generatedOn;
    }

    public LocalDateTime getSeenOn() {
        return seenOn;
    }

    public void setSeenOn(LocalDateTime seenOn) {
        this.seenOn = seenOn;
    }
}
