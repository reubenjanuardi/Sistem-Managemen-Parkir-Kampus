import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;

public class LogParkirController {

    // FXML Annotations for TableView and Columns
    @FXML
    private TableView<LogKendaraan> logTable;

    @FXML
    private TableColumn<LogKendaraan, String> kolomNomorKendaraan;

    @FXML
    private TableColumn<LogKendaraan, String> kolomKategori;

    @FXML
    private TableColumn<LogKendaraan, String> kolomJenisKendaraan;

    @FXML
    private TableColumn<LogKendaraan, String> kolomWaktuMasuk;

    @FXML
    private TableColumn<LogKendaraan, String> kolomWaktuKeluar;
    
    @FXML
    private Button btnKembali;

    // ObservableList to hold vehicle data
    private ObservableList<LogKendaraan> logParkir;

    // Initialize method to set up the TableView
    @FXML
    public void initialize() {
        // Initialize the vehicle list
        logParkir = FXCollections.observableArrayList();

        // Set up the columns with the appropriate property values
        kolomNomorKendaraan.setCellValueFactory(new PropertyValueFactory<>("nomorKendaraan"));
        kolomKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        kolomJenisKendaraan.setCellValueFactory(new PropertyValueFactory<>("jenisKendaraan"));
        kolomWaktuMasuk.setCellValueFactory(new PropertyValueFactory<>("waktuMasuk"));
        kolomWaktuKeluar.setCellValueFactory(new PropertyValueFactory<>("waktuKeluar"));
        // Load data into the TableView
        loadLogParkir();

        btnKembali.setOnAction(event -> NavigationUtil.goToAdminMenu(btnKembali));
    }

    // Method untuk memuat data log parkir dari database
    private void loadLogParkir() {
        String query = "SELECT * FROM log_parkir";

        try (Connection conn = DatabaseUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String nomorKendaraan = rs.getString("nomor_kendaraan");
                String kategori = rs.getString("kategori");
                String jenisKendaraan = rs.getString("jenis");
                String waktuMasuk = rs.getString("waktu_masuk");
                String waktuKeluar = rs.getString("waktu_keluar");

                // Tambahkan data ke ObservableList
                logParkir.add(new LogKendaraan(nomorKendaraan, kategori, jenisKendaraan, waktuMasuk, waktuKeluar));
            }

            // Set data ke TableView
            logTable.setItems(logParkir);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}