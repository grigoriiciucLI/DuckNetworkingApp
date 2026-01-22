package Service;
import Domain.Event.FriendshipEvent;
import Domain.Event.Notification;
import Domain.User.Person;
import Domain.User.User;
import Observer.IObservable;
import Observer.IObserver;
import Repository.Db.Filter;
import Repository.Db.NotificationRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationService implements IObservable<Notification> {
    private final NotificationRepository repo;
    private final List<IObserver<Notification>> observers = new ArrayList<>();

    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    public void sendNotification(User receiver, String content) {
        Notification n = new Notification(receiver, content);
        repo.add(n);
        notifyObservers(n);
    }

    public List<Notification> getNotificationsFor(User user) {
        Filter f = new Filter();
        f.addFilter("receiver_id", user.getId());
        return repo.filter(f);
    }

    public void markAsSeen(Notification n) {
        n.setSeenOn(LocalDateTime.now());
        repo.update(n);
    }

    public void markAllAsSeen(Person p) {
        List<Notification> notifications = getNotificationsFor(p).stream()
                .filter(n -> n.getSeenOn() == null)
                .toList();
        for (Notification n : notifications) {
            markAsSeen(n);
        }
    }


    @Override
    public void addObserver(IObserver<Notification> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(IObserver<Notification> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(Notification e) {
        observers.forEach(o->o.update(e));
    }
}

