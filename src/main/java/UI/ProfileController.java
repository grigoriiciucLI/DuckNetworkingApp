package UI;

import Domain.User.Person;
import Domain.User.Friendship;
import Repository.Db.FriendshipRepository;
import Service.AuthService;
import Service.FriendshipService;
import Service.PersonService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Optional;

public class ProfileController {

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
    }
    public void setProfilePerson(Person person) {
        this.profilePerson = person;
        showProfile();
    }
    private void showProfile() {
        Person currentUser = authService.getCurrentPerson();
        nameLabel.setText(profilePerson.getName() + " " + profilePerson.getSurname());
        usernameLabel.setText(profilePerson.getUsername());
        friendsCountLabel.setText("Friends: " + friendshipService.countFriends(currentUser));
        updateFriendshipButtons(currentUser);
    }

    private void updateFriendshipButtons(Person currentUser) {
        boolean isFriend = friendshipService.exists(currentUser, profilePerson);
        addButton.setVisible(!isFriend && !currentUser.equals(profilePerson));
        removeButton.setVisible(isFriend);
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
}
