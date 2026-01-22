package UI;
import Domain.Event.Notification;
import Domain.User.Person;
import Observer.IObserver;
import Service.AuthService;
import Service.NotificationService;
import Service.PersonService;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UsersController implements IObserver<Notification> {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<Person> usersTable;
    @FXML
    private TableColumn<Person, Long> idColumn;
    @FXML
    private TableColumn<Person, String> usernameColumn;
    @FXML
    private TableColumn<Person, String> emailColumn;
    @FXML private ListView<Notification> notificationListView;
    private AuthService authService;
    private PersonService personService;
    private NotificationService notificationService;
    private Stage stage;
    private final ObservableList<Person> usersList = FXCollections.observableArrayList();
    public void setServices(AuthService authService, Stage stage) {
        this.authService = authService;
        this.stage = stage;
        this.personService = authService.getPersonService();
        loadUsers();
        this.notificationService = authService.getFriendshipService().getNotificationService();
        loadNotifications();
        notificationService.addObserver(this);
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        usersTable.setItems(usersList);
        usersTable.setRowFactory(tv -> {
            TableRow<Person> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    openProfile(row.getItem());
                }
            });
            return row;
        });
        notificationListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Notification n, boolean empty) {
                super.updateItem(n, empty);
                if (empty || n == null) {
                    setText(null);
                } else {
                    setText(n.getContent() + " (" + n.getGeneratedOn().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ")");
                    if (n.getSeenOn() != null) {
                        setTextFill(Color.GRAY);
                    } else {
                        setTextFill(Color.BLACK);
                    }
                }
            }
        });
    }

    private void loadUsers() {
        usersList.setAll(personService.getAll());
    }
    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();
        usersList.setAll(
                query.isEmpty()
                        ? personService.getAll()
                        : personService.searchByUsername(query)
        );
    }
    private void openProfile(Person person) {
        dispose();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProfileView.fxml"));
            Parent root = loader.load();
            ProfileController controller = loader.getController();
            controller.setServices(authService, stage);
            controller.setProfilePerson(person);
            stage.setScene(new Scene(root));
            stage.setTitle("Profile - " + person.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleLogout() {
        dispose();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            ProfileController controller = loader.getController();
            authService.logout();
            controller.setServices(authService, stage);
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(Notification notification) {
        if (notification.getReceiver().equals(authService.getCurrentPerson())) {
            Platform.runLater(() -> {
                showPopup(notification.getContent());
                loadNotifications();
            });
        }
    }
    private void showPopup(String message) {
        Platform.runLater(() -> {
            Popup popup = new Popup();
            Label label = new Label(message);
            label.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-text-fill: white; -fx-padding: 8; -fx-border-radius: 5; -fx-background-radius: 5;");
            popup.getContent().add(label);
            popup.show(stage, stage.getX() + stage.getWidth() - 250, stage.getY() + 50);
            PauseTransition pt = new PauseTransition(Duration.seconds(3));
            pt.setOnFinished(e -> popup.hide());
            pt.play();
        });
    }

    public void dispose() {
        notificationService.removeObserver(this);
    }
    private void loadNotifications() {
        if (authService.getCurrentPerson() == null) return;
        List<Notification> notifications = notificationService.getNotificationsFor(authService.getCurrentPerson());
        Platform.runLater(() -> {
            notificationListView.setItems(FXCollections.observableArrayList(notifications));
            if (!notifications.isEmpty())
                notificationListView.scrollTo(notifications.size() - 1);
        });
    }

    @FXML
    private void markAllAsRead() {
        notificationService.markAllAsSeen(authService.getCurrentPerson());
        loadNotifications();
    }
}

