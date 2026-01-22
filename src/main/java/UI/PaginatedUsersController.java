package UI;

import Domain.User.Person;
import Repository.Db.Filter;
import Repository.Db.PersonRepository;
import Service.PersonService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class PaginatedUsersController {

    @FXML
    private TextField searchField;
    @FXML private TableView<Person> usersTable;
    @FXML private TableColumn<Person, Long> idColumn;
    @FXML private TableColumn<Person, String> usernameColumn;
    @FXML private TableColumn<Person, String> emailColumn;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label pageLabel;
    private PersonService personService;
    private PersonRepository personRepository;
    private int currentPage = 0;
    private final int pageSize = 5;
    private String currentFilter = "";

    public void setService(PersonService personService) {
        this.personService = personService;
        this.personRepository = (PersonRepository)personService.getRepo();
        initializeTable();
        loadPage();
    }
    private void initializeTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            currentFilter = newVal.trim();
            currentPage = 0;
            loadPage();
        });
        prevButton.setOnAction(e -> {
            if (currentPage > 0) {
                currentPage--;
                loadPage();
            }
        });
        nextButton.setOnAction(e -> {
            currentPage++;
            loadPage();
        });
    }

    private void loadPage() {
        List<Person> page;
        if (currentFilter.isEmpty()) {
            page = personRepository.getPage(pageSize, currentPage * pageSize);
        } else {
            Filter f = new Filter();
            f.likeIgnoreCase("username", currentFilter);
            page = personRepository.filter(f, pageSize, currentPage * pageSize);
        }
        usersTable.setItems(FXCollections.observableArrayList(page));
        pageLabel.setText("Page: " + (currentPage + 1));
        prevButton.setDisable(currentPage == 0);
        nextButton.setDisable(page.size() < pageSize);
    }
}

