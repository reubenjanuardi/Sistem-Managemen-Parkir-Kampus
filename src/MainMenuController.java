import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

public class MainMenuController {

    @FXML
    private Button btnRegistrasi;

    @FXML
    private Button btnSimulasi;

    @FXML
    private Button btnAdmin;

    // Method untuk inisialisasi
    @FXML
    public void initialize() {
        btnRegistrasi.setOnAction(event -> openScene("Registration.fxml", "Registrasi Kendaraan"));
        btnSimulasi.setOnAction(event -> openScene("Simulasi.fxml", "Simulasi Parkir"));
        btnAdmin.setOnAction(event -> openScene("AdminLogin.fxml", "Login Admin"));
    }

    private void openScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent nextPage = loader.load();

            // Ganti scene di stage saat ini
            Stage stage = (Stage) btnAdmin.getScene().getWindow();
            Scene scene = new Scene(nextPage);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Tidak dapat membuka halaman: " + title);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}