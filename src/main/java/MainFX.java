import Repository.Db.FriendshipRepository;
import Repository.Db.NotificationRepository;
import Repository.Db.PersonRepository;
import Service.AuthService;
import Service.FriendshipService;
import Service.NotificationService;
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
    private PersonService sharedPersonService;
    private FriendshipService sharedFriendshipService;
    private NotificationService sharedNotificationService;
    @Override
    public void start(Stage primaryStage) {
        initializeSharedServices();
        openLoginWindow();
        openLoginWindow();
    }
    private void initializeSharedServices() {
        try {
            Properties props = Config.getProperties();
            Connection connection = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.username"),
                    props.getProperty("db.password")
            );
            PersonRepository personRepository = new PersonRepository(connection);
            FriendshipRepository friendshipRepository = new FriendshipRepository(connection, personRepository);
            NotificationRepository notificationRepository = new NotificationRepository(connection,personRepository);
            sharedPersonService = new PersonService(personRepository);
            sharedNotificationService = new NotificationService(notificationRepository);
            sharedFriendshipService = new FriendshipService(friendshipRepository, sharedPersonService,sharedNotificationService);
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed", e);
        }
    }
    public void openLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            AuthService windowAuth = new AuthService(sharedPersonService, sharedFriendshipService);
            LoginController controller = loader.getController();
            controller.setServices(windowAuth, loginStage);
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("Login");
            loginStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
