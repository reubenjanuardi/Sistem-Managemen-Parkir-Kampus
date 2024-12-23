import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LaporanParkirController {
    @FXML
    private TableView<LaporanParkir> tabelLaporan;

    @FXML
    private TableColumn<LaporanParkir, String> kolomKategoriPemilik;

    @FXML
    private TableColumn<LaporanParkir, String> kolomJenisKendaraan;

    @FXML
    private TableColumn<LaporanParkir, Integer> kolomJumlah;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button btnKembali;
    
    private final ObservableList<LaporanParkir> laporanList = FXCollections.observableArrayList();
    
    @FXML
    private void initialize() {
        kolomKategoriPemilik.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        kolomJenisKendaraan.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        kolomJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));

        // Default date to today
        datePicker.setValue(LocalDate.now());
        loadLaporanData(LocalDate.now());

        btnKembali.setOnAction(event -> NavigationUtil.goToAdminMenu(btnKembali));
    }

    private void loadLaporanData(LocalDate date) {
        laporanList.clear();
    
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
    
        System.out.println("Start Timestamp: " + Timestamp.valueOf(startOfDay));
        System.out.println("End Timestamp: " + Timestamp.valueOf(endOfDay));

        String debugQuery = "SELECT kategori, jenis, COUNT(*) AS jumlah " +
                    "FROM log_parkir " +
                    "WHERE waktu_masuk BETWEEN '" + Timestamp.valueOf(startOfDay) + 
                    "' AND '" + Timestamp.valueOf(endOfDay) + "' " +
                    "AND (waktu_keluar IS NULL OR waktu_keluar >= '" + Timestamp.valueOf(startOfDay) + "') " +
                    "GROUP BY kategori, jenis";

        System.out.println("Debug Query: " + debugQuery);

        try (Connection conn = DatabaseUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(debugQuery)) {

            laporanList.clear();
            while (rs.next()) {
                String kategoriPemilik = rs.getString("kategori");
                String jenisKendaraan = rs.getString("jenis");
                int jumlah = rs.getInt("jumlah");

                laporanList.add(new LaporanParkir(kategoriPemilik, jenisKendaraan, jumlah));
            }

            tabelLaporan.setItems(laporanList);
            System.out.println("Total Data Added: " + laporanList.size());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }  

    @FXML
    private void handleFilterData() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            loadLaporanData(selectedDate);
        } else {
            showAlert("Error", "Tanggal Tidak Dipilih", "Silakan pilih tanggal terlebih dahulu.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}