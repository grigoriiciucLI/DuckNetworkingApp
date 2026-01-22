package Service;

import Domain.Event.FriendshipEvent;
import Domain.User.Friendship;
import Domain.User.Person;
import Domain.User.User;
import Observer.IObservable;
import Observer.IObserver;
import Repository.Db.Filter;
import Repository.IRepository;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FriendshipService extends EntityService<Integer, Friendship>
implements IObservable<FriendshipEvent> {
    private final PersonService personService;
    private final NotificationService notificationService;
    private final List<IObserver<FriendshipEvent>> observers = new ArrayList<>();
    public FriendshipService(IRepository<Integer, Friendship> repo, PersonService personService, NotificationService notificationService) {
        super(repo);
        this.personService = personService;
        this.notificationService = notificationService;
    }

    public void add(User u1, User u2){
        Friendship friendship = new Friendship(u1,u2);
        super.add(friendship);
        notifyObservers(new FriendshipEvent(FriendshipEvent.Type.ADDED,friendship));
        notificationService.sendNotification(u2,u1.getUsername()+" added you as a friend");
    }

    public CompletableFuture<Void> addAsync(User u1, User u2){
        Friendship friendship = new Friendship(u1, u2);
        return CompletableFuture.runAsync(() -> super.add(friendship), AsyncExecutor.getExecutor())
                .thenRun(() -> {
                    // Notify observers safely on JavaFX thread
                    Platform.runLater(() ->
                            notifyObservers(new FriendshipEvent(FriendshipEvent.Type.ADDED, friendship)));
                    // Just call sendNotification, it's already async
                    notificationService.sendNotification(u2, u1.getUsername() + " added you as a friend");
                });
    }

    public void remove(User u1, User u2) {
        List<long[]> pairs = List.of(
                new long[]{u1.getId(), u2.getId()},
                new long[]{u2.getId(), u1.getId()}
        );
        for (long[] pair : pairs) {
            Filter f = new Filter();
            f.addFilter("user1_id", pair[0]);
            f.addFilter("user2_id", pair[1]);
            getRepo().filter(f).stream()
                    .findFirst()
                    .ifPresent(friendship -> {
                        super.remove(friendship.getId());
                        notifyObservers(new FriendshipEvent(FriendshipEvent.Type.REMOVED, friendship));
                        notificationService.sendNotification(u2, u1.getUsername() + " removed you as a friend");
                    });
        }
    }
    public CompletableFuture<Void> removeAsync(User u1, User u2) {
        List<long[]> pairs = List.of(
                new long[]{u1.getId(), u2.getId()},
                new long[]{u2.getId(), u1.getId()}
        );
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (long[] pair : pairs) {
            Filter f = new Filter();
            f.addFilter("user1_id", pair[0]);
            f.addFilter("user2_id", pair[1]);
            getRepo().filter(f).stream().findFirst().ifPresent(friendship -> {
                CompletableFuture<Void> future = CompletableFuture.runAsync(
                        () -> super.remove(friendship.getId()), AsyncExecutor.getExecutor()
                ).thenRun(() -> {
                    // Notify observers on JavaFX thread
                    Platform.runLater(() ->
                            notifyObservers(new FriendshipEvent(FriendshipEvent.Type.REMOVED, friendship)));
                    // Send notification (already async)
                    notificationService.sendNotification(u2, u1.getUsername() + " removed you as a friend");
                });
                futures.add(future);
            });
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
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

    @Override
    public void addObserver(IObserver<FriendshipEvent> o) {
        observers.add(o);
    }
    @Override
    public void removeObserver(IObserver<FriendshipEvent> o) {
        observers.remove(o);
    }
    @Override
    public void notifyObservers(FriendshipEvent e) {
        Platform.runLater(() -> {
            observers.forEach(o -> o.update(e));
        });
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }
}
