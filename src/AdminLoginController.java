import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class AdminLoginController{

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnKembali;
    
    @FXML
    public void initialize() {
        btnKembali.setOnAction(event -> NavigationUtil.goToMainMenu(btnKembali));
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("admin") && password.equals("password")) {
            try {
                // Load halaman inventory.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminMenu.fxml"));
                Parent inventoryPage = loader.load();

                // Ganti scene di stage saat ini
                Stage stage = (Stage) usernameField.getScene().getWindow();
                Scene scene = new Scene(inventoryPage);
                stage.setScene(scene);
                stage.setTitle("Menu Admin");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Tampilkan alert jika login gagal
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Username atau password salah!");
            alert.show();
        }
    }
}