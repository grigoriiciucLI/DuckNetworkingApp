package Service;

import Domain.User.Friendship;
import Domain.User.Person;
import Domain.User.User;
import Repository.Db.Filter;
import Repository.IRepository;

public class FriendshipService extends EntityService<Integer, Friendship>{
    private final PersonService personService;
    public FriendshipService(IRepository<Integer, Friendship> repo, PersonService personService) {
        super(repo);
        this.personService = personService;
    }

    public void add(User u1, User u2){
        Friendship friendship = new Friendship(u1,u2);
        super.add(friendship);
    }

    public void remove(User u1, User u2) {
        Filter f1 = new Filter();
        f1.addFilter("user1_id", u1.getId());
        f1.addFilter("user2_id", u2.getId());
        Filter f2 = new Filter();
        f2.addFilter("user1_id", u2.getId());
        f2.addFilter("user2_id", u1.getId());
        getRepo().filter(f1).stream()
                .findFirst()
                .ifPresent(f -> super.remove(f.getId()));
        getRepo().filter(f2).stream()
                .findFirst()
                .ifPresent(f -> super.remove(f.getId()));
    }

    public boolean exists(Person u1, Person u2) {
        Filter f1 = new Filter();
        f1.addFilter("user1_id", u1.getId());
        f1.addFilter("user2_id", u2.getId());
        if (!getRepo().filter(f1).isEmpty()) {
            return true;
        }
        Filter f2 = new Filter();
        f2.addFilter("user1_id", u2.getId());
        f2.addFilter("user2_id", u1.getId());
        return !getRepo().filter(f2).isEmpty();
    }

    public int countFriends(Person p) {
        Filter f1 = new Filter();
        f1.addFilter("user1_id", p.getId());
        Filter f2 = new Filter();
        f2.addFilter("user2_id", p.getId());
        int count1 =repo.filter(f1).size();
        int count2 = repo.filter(f2).size();
        return count1 + count2;
    }


}
