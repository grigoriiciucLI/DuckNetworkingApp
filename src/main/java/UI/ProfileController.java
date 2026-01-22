package UI;

import Domain.Event.FriendshipEvent;
import Domain.Event.Notification;
import Domain.User.Person;
import Domain.User.Friendship;
import Observer.IObserver;
import Repository.Db.FriendshipRepository;
import Service.AuthService;
import Service.FriendshipService;
import Service.NotificationService;
import Service.PersonService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class ProfileController implements IObserver<FriendshipEvent>{
    private AuthService authService;
    private FriendshipService friendshipService;
    private PersonService personService;
    private Stage stage;
    private Person profilePerson;
    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label friendsCountLabel;
    @FXML private Button addButton;
    @FXML private Button removeButton;
    @FXML
    private void initialize() {
        addButton.setVisible(false);
        removeButton.setVisible(false);
    }
    public void setServices(AuthService authService, Stage stage) {
        this.authService = authService;
        this.stage = stage;
        this.personService = authService.getPersonService();
        this.friendshipService = authService.getFriendshipService();
        friendshipService.addObserver(this);
    }
    public void setProfilePerson(Person person) {
        this.profilePerson = person;
        showProfile();
    }
    private void showProfile() {
        Person currentUser = authService.getCurrentPerson();
        nameLabel.setText(profilePerson.getName() + " " + profilePerson.getSurname());
        usernameLabel.setText(profilePerson.getUsername());
        updateFriendshipButtons(currentUser);
        updateFriendsCount();
    }

    private void updateFriendshipButtons(Person currentUser) {
        boolean isFriend = friendshipService.exists(currentUser, profilePerson);
        addButton.setVisible(!isFriend && !currentUser.equals(profilePerson));
        removeButton.setVisible(isFriend);
    }
    private void updateFriendsCount(){
        friendsCountLabel.setText("Friends: " + friendshipService.countFriends(profilePerson));
    }
    @FXML
    private void handleAdd() {
        Person currentUser = authService.getCurrentPerson();
        friendshipService.add(currentUser, profilePerson);
        updateFriendshipButtons(currentUser);
    }
    @FXML
    private void handleRemove() {
        Person currentUser = authService.getCurrentPerson();
        friendshipService.remove(currentUser, profilePerson);
        updateFriendshipButtons(currentUser);
    }
    @FXML
    private void handleBack(){
        dispose();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UsersView.fxml"));
            Parent root = loader.load();
            UsersController controller = loader.getController();
            controller.setServices(authService, stage);
            stage.setScene(new Scene(root));
            stage.setTitle("Users");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(FriendshipEvent event) {
        Person currentUser = authService.getCurrentPerson();
        if (event.getFriendship().getU1().equals(profilePerson) ||
                event.getFriendship().getU2().equals(profilePerson) ||
                event.getFriendship().getU1().equals(currentUser) ||
                event.getFriendship().getU2().equals(currentUser)) {
            updateFriendshipButtons(currentUser);
            updateFriendsCount();
        }
    }
    public void dispose() {
        friendshipService.removeObserver(this);
    }
}
