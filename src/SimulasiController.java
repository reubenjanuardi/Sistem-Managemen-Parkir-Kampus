import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import java.util.ArrayList;

public class SimulasiController {

    @FXML
    private ComboBox<String> cmbJenisKendaraan;

    @FXML
    private TextField txtPlatNomor;
    
    @FXML
    private Button btnKembali;

    // Inisialisasi ComboBox dan lainnya
    @FXML
    private void initialize() {
        ObservableList<String> jenisKendaraan = FXCollections.observableArrayList("Mobil", "Motor");
        cmbJenisKendaraan.setItems(jenisKendaraan);
        // Action untuk tombol kembali ke menu utama
        btnKembali.setOnAction(event -> NavigationUtil.goToMainMenu(btnKembali));
    }

    // Ambil data registrasi kendaraan dari RegistrationController
    ArrayList<Vehicle> registeredVehicles = RegistrationController.getVehiclesFromDatabase();

    // Method mencari kendaraan ter-registrasi
    private Vehicle findRegisteredVehicle(String nomorKendaraan) {
        for (Vehicle v : registeredVehicles) {
            if (v.getNomorKendaraan().equalsIgnoreCase(nomorKendaraan)) {
                return v;
            }
        }
        return null;
    }

    // Handle tombol Parkir Masuk
    @FXML
    private void handleParkirMasuk() {
        String nomorKendaraan = txtPlatNomor.getText();

        // Check valiadasi input field
        if (nomorKendaraan == null || nomorKendaraan.isEmpty()) {
            showAlert("Error", "Lengkapi data!", "Nomor kendaraan harus diisi!");
            return;
        }

        // Mengecek apakah kendaraan sudah diterdaftar
        Vehicle vehicle = findRegisteredVehicle(nomorKendaraan);
        if (vehicle == null) {
            showAlert("Error", "Data tidak ditemukan!", "Kendaraan belum terdaftar! Silakan registrasi terlebih dahulu.");
            return;
        }

        // // Mengecek apakah kendaraan sudah terparkir
        // String category = vehicle.getKategoriPemilik();
        // ArrayList<Vehicle> parkedVehicles = ParkirManager.getParkedVehicles();
        // for (Vehicle v : parkedVehicles) {
        //     if (v.getNomorKendaraan().equalsIgnoreCase(nomorKendaraan)) {
        //         showAlert("Error", "Kendaraan Sudah Parkir!", "Kendaraan ini sudah parkir di sistem.");
        //         return;
        //     }
        // }

        // Mengecek apakah kendaraan sudah terparkir
        String category = vehicle.getKategoriPemilik();
        String type = vehicle.getJenisKendaraan();
        // ArrayList<Vehicle> parkedVehicles = ParkirManager.getParkedVehicles();

        // System.out.println("Mengecek kendaraan dengan nomor: " + nomorKendaraan); // Debugging
        // System.out.println("Kendaraan terparkir saat ini: " + parkedVehicles.size()); // Debugging

        // for (Vehicle v : parkedVehicles) {
        //     //System.out.println("Membandingkan dengan kendaraan: " + v.getNomorKendaraan()); // Debugging
        //     if (v.getNomorKendaraan().equalsIgnoreCase(nomorKendaraan)) {
        //         showAlert("Error", "Kendaraan Sudah Parkir!", "Kendaraan ini sudah parkir di sistem.");
        //         return;
        //     }
        // }

        // Proses parkir
        if (ParkirManager.parkVehicle(vehicle, category, type)) {
            clearFields();
            showAlert("Sukses", "Parkir Masuk Berhasil", "Parkir masuk dicatat untuk kendaraan: " + nomorKendaraan);
        } else {
            showAlert("Error", "Gagal Parkir!", "Terjadi kesalahan saat mencatat parkir.");
        }
    }

    // Handle tombol Parkir Keluar
    @FXML
    private void handleParkirKeluar() {
        String nomorKendaraan = txtPlatNomor.getText();
        // Check valiadasi input field
        if (nomorKendaraan == null || nomorKendaraan.isEmpty()) {
            showAlert("Error", "Lengkapi data!", "Nomor kendaraan harus diisi!");
            return;
        }

        // Mengecek apakah kendaraan sudah diterdaftar
        Vehicle vehicle = findRegisteredVehicle(nomorKendaraan);
        if (vehicle == null) {
            showAlert("Error", "Data tidak ditemukan!", "Kendaraan belum terdaftar! Silakan registrasi terlebih dahulu.");
            return;
        }

        if(ParkirManager.unloadVehicle(vehicle, vehicle.getKategoriPemilik(), vehicle.getJenisKendaraan())) {
            LogKendaraan.catatKendaraanKeluar(vehicle, vehicle.getKategoriPemilik(), vehicle.getJenisKendaraan());
            clearFields();
            showAlert("Sukses", "Parkir Keluar Berhasil", "Parkir keluar dicatat untuk kendaraan: " + nomorKendaraan);
        } else {
            showAlert("Gagal", "Kendaraan tidak ditemukan!", "Kendaraan belum masuk atau sudah keluar.");
        }
    }

    // Method untuk menampilkan alert
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Membersihkan input
    private void clearFields() {
        txtPlatNomor.clear();
        cmbJenisKendaraan.setValue(null);
    }
}