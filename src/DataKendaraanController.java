import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;

public class DataKendaraanController {

    // FXML Annotations for TableView and Columns
    @FXML
    private TableView<Vehicle> vehicleTable;

    @FXML
    private TableColumn<Vehicle, String> nomorKendaraan;

    @FXML
    private TableColumn<Vehicle, String> jenisKendaraan;

    @FXML
    private TableColumn<Vehicle, String> namaPemilik;

    @FXML
    private TableColumn<Vehicle, String> kategoriPemilik;
    
    @FXML
    private Button btnKembali;

    // ObservableList to hold vehicle data
    private ObservableList<Vehicle> listKendaraan;

    // Initialize method to set up the TableView
    @FXML
    public void initialize() {
        // Initialize the vehicle list
        listKendaraan = FXCollections.observableArrayList();

        // Set up the columns with the appropriate property values
        nomorKendaraan.setCellValueFactory(new PropertyValueFactory<>("nomorKendaraan"));
        jenisKendaraan.setCellValueFactory(new PropertyValueFactory<>("jenisKendaraan"));
        namaPemilik.setCellValueFactory(new PropertyValueFactory<>("namaPemilik"));
        kategoriPemilik.setCellValueFactory(new PropertyValueFactory<>("kategoriPemilik"));

        // Load data into the TableView
        loadDataKendaraan();
        btnKembali.setOnAction(event -> NavigationUtil.goToAdminMenu(btnKembali));
    }

    // Method to load vehicle data (this is just an example)
    private void loadDataKendaraan() {
        String query = "SELECT * FROM kendaraan";

        try (Connection conn = DatabaseUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String nomorKendaraan = rs.getString("nomor_kendaraan");
                String jenisKendaraan = rs.getString("jenis_kendaraan");
                String namaPemilik = rs.getString("nama_pemilik");
                String kategoriPemilik = rs.getString("kategori_pemilik");

                // Tambahkan data ke ObservableList
                listKendaraan.add(new Vehicle(nomorKendaraan, jenisKendaraan, namaPemilik, kategoriPemilik));
            }

            // Set data ke TableView
            vehicleTable.setItems(listKendaraan);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}