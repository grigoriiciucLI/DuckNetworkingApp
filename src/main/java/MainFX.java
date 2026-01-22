import Repository.Db.FriendshipRepository;
import Repository.Db.PersonRepository;
import Service.AuthService;
import Service.FriendshipService;
import Service.PersonService;
import UI.LoginController;
import config.Config;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
public class MainFX extends Application {
    private AuthService authService;
    @Override
    public void start(Stage primaryStage) {
        AuthService authService = createAuthService();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
            Parent root = loader.load();
            LoginController controller = loader.getController();
            controller.setServices(authService, primaryStage);
            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private AuthService createAuthService() {
        try {
            Properties props = Config.getProperties();
            Connection connection = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.username"),
                    props.getProperty("db.password")
            );
            PersonRepository personRepository = new PersonRepository(connection);
            FriendshipRepository friendshipRepository = new FriendshipRepository(connection,personRepository);
            PersonService personService = new PersonService(personRepository);
            FriendshipService friendshipService = new FriendshipService(friendshipRepository,personService);
            return new AuthService(personService,friendshipService);
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed", e);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
