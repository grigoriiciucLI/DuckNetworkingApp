package UI;
import Domain.User.Person;
import Domain.User.User;
import Service.AuthService;
import Service.PersonService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class UsersController {

    @FXML private TextField searchField;
    @FXML private TableView<Person> usersTable;
    @FXML private TableColumn<Person, Long> idColumn;
    @FXML private TableColumn<Person, String> usernameColumn;
    @FXML private TableColumn<Person, String> emailColumn;

    private AuthService authService;
    private PersonService personService;
    private Stage stage;

    private final ObservableList<Person> usersList = FXCollections.observableArrayList();

    public void setServices(AuthService authService, Stage stage) {
        this.authService = authService;
        this.stage = stage;
        this.personService = authService.getPersonService();
        loadUsers();
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
}
