import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

public class AdminMenuController {

    @FXML
    private Button btnLaporan;

    @FXML
    private Button btnLog;

    @FXML
    private Button btnKendaraan;

    @FXML
    private Button btnKembali;

    // Method untuk inisialisasi
    @FXML
    public void initialize() {
        btnLaporan.setOnAction(event -> openScene("LaporanParkir.fxml", "Laporan Parkir"));
        btnLog.setOnAction(event -> openScene("LogParkir.fxml", "Log Parkir"));
        btnKendaraan.setOnAction(event -> openScene("DataKendaraan.fxml", "Data Kendaraan"));
        btnKembali.setOnAction(event -> NavigationUtil.goToMainMenu(btnKembali));
    }

    private void openScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent nextpaPage = loader.load();

            // Ganti scene di stage saat ini
            Stage stage = (Stage) btnLaporan.getScene().getWindow();
            Scene scene = new Scene(nextpaPage);
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