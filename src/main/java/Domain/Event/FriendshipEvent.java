package Domain.Event;

import Domain.User.Friendship;

public class FriendshipEvent extends Event {
    public enum Type { ADDED, REMOVED }
    private final Type type;
    private final Friendship friendship;
    public FriendshipEvent(Type type, Friendship friendship) {
        this.type = type;
        this.friendship = friendship;
    }
    public Type getType() { return type; }
    public Friendship getFriendship() { return friendship; }
}
