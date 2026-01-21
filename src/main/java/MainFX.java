import Repository.Db.PersonRepository;
import Service.AuthService;
import Service.PersonService;
import UI.LoginController;
import config.Config;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

public class MainFX extends Application {
    private AuthService authService;
    private PersonService personService;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties props = Config.getProperties();
        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        PersonRepository personRepository = new PersonRepository(connection);
        personService = new PersonService(personRepository);
        authService = new AuthService(personService);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        controller.setAuthService(authService,primaryStage);
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
