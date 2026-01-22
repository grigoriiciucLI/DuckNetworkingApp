package UI;
import Domain.Event.Notification;
import Observer.IObserver;
import Service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    private AuthService authService;
    private Stage stage;

    public void setServices(AuthService authService, Stage stage) {
        this.authService = authService;
        this.stage = stage;
    }

    @FXML
    private void handleLogin() {
        try {
            authService.login(usernameField.getText(), passwordField.getText());
            openUsersView();
        } catch (RuntimeException e) {
            errorLabel.setText(e.getMessage());
        }
    }
    private void openUsersView() {
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

}


