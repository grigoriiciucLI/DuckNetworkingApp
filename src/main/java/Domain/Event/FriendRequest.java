package Domain.Event;
import Domain.User.User;

public class FriendRequest extends Event{
    User requestFrom;
    User requestTo;
    Status status = Status.PENDING;

    public FriendRequest(Integer id, User requestFrom, User requestTo, Status status) {
        super(id);
        this.requestFrom = requestFrom;
        this.requestTo = requestTo;
        this.status = status;
    }

    public FriendRequest(User requestFrom, User requestTo) {
        super();
        this.requestFrom = requestFrom;
        this.requestTo = requestTo;
    }

    public User getRequestFrom() {
        return requestFrom;
    }

    public User getRequestTo() {
        return requestTo;
    }

    public Status getStatus() {
        return status;
    }
}
