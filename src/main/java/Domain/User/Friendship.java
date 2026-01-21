package Domain.User;

import Domain.Entity;

public class Friendship extends Entity<Integer> {
    private User u1;
    private User u2;

    public Friendship(Integer integer, User u1, User u2) {
        super(integer);
        this.u1 = u1;
        this.u2 = u2;
    }

    public User getU1() {
        return u1;
    }

    public User getU2() {
        return u2;
    }
}
