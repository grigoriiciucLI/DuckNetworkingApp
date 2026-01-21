package UI;
import Domain.User.User;
import Service.PersonService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;
import java.util.List;
public class UsersController {
    @FXML private TextField searchField;
    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, Long> idColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    private final PersonService personService;
    private final ObservableList<User> usersList = FXCollections.observableArrayList();
    public UsersController(PersonService personService) {
        this.personService = personService;
    }
    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        List<User> allUsers = new ArrayList<>(personService.getAll());
        usersList.setAll(allUsers);
        usersTable.setItems(usersList);
    }
    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            usersList.setAll(personService.getAll());
        } else {
            usersList.setAll(personService.searchByUsername(query));
        }
    }
}