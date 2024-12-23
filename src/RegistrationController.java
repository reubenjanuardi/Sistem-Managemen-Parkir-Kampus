import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import java.sql.*;
import java.util.ArrayList;

public class RegistrationController {

    @FXML
    private TextField txtNomorKendaraan;

    @FXML
    private ComboBox<String> cbJenisKendaraan;

    @FXML
    private TextField txtNamaPemilik;

    @FXML
    private ComboBox<String> cbKategoriPemilik;

    @FXML
    private Button btnDaftar;

    @FXML
    private Button btnKembali;

    @FXML
    public void initialize() {
        // Isi ComboBox
        cbJenisKendaraan.setItems(FXCollections.observableArrayList("Mobil", "Motor"));
        cbKategoriPemilik.setItems(FXCollections.observableArrayList("Mahasiswa", "Dosen", "Tamu"));

        // Action untuk tombol daftar
        btnDaftar.setOnAction(event -> registerVehicle());

        // Action untuk tombol kembali ke menu utama
        btnKembali.setOnAction(event -> NavigationUtil.goToMainMenu(btnKembali));
    }

    private void registerVehicle() {
        String nomorKendaraan = txtNomorKendaraan.getText();
        String jenisKendaraan = cbJenisKendaraan.getValue();
        String namaPemilik = txtNamaPemilik.getText();
        String kategoriPemilik = cbKategoriPemilik.getValue();

        // Validasi input
        if (nomorKendaraan == null || nomorKendaraan.isEmpty() ||
            jenisKendaraan == null || jenisKendaraan.isEmpty() ||
            namaPemilik == null || namaPemilik.isEmpty() ||
            kategoriPemilik == null || kategoriPemilik.isEmpty()) {

            showAlert("Error", "Semua field harus diisi!");
            return;
        }

        // Cek duplikasi berdasarkan nomor kendaraan di database
        boolean isDuplicate = checkDuplicateVehicle(nomorKendaraan);
        if (isDuplicate) {
            showAlert("Error", "Kendaraan dengan nomor tersebut sudah terdaftar!");
        } else {
            // Simpan kendaraan baru ke database
            saveVehicleToDatabase(nomorKendaraan, jenisKendaraan, namaPemilik, kategoriPemilik);
            showAlert("Sukses", "Kendaraan berhasil terdaftar!");
            clearFields();
        }
    }

    private boolean checkDuplicateVehicle(String nomorKendaraan) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM kendaraan WHERE nomor_kendaraan = ?")) {
            statement.setString(1, nomorKendaraan);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Terjadi kesalahan saat memeriksa duplikasi!");
            return false;
        }
    }
    
    private void saveVehicleToDatabase(String nomorKendaraan, String jenisKendaraan, String namaPemilik, String kategoriPemilik) {
        String query = "INSERT INTO kendaraan (nomor_kendaraan, jenis_kendaraan, nama_pemilik, kategori_pemilik) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, nomorKendaraan);
            statement.setString(2, jenisKendaraan);
            statement.setString(3, namaPemilik);
            statement.setString(4, kategoriPemilik);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Terjadi kesalahan saat menyimpan kendaraan!");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        txtNomorKendaraan.clear();
        cbJenisKendaraan.setValue(null);
        txtNamaPemilik.clear();
        cbKategoriPemilik.setValue(null);
    }

    // Method untuk mengambil kendaraan dari database
    public static ArrayList<Vehicle> getVehiclesFromDatabase() {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM kendaraan";

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String nomorKendaraan = resultSet.getString("nomor_kendaraan");
                String jenisKendaraan = resultSet.getString("jenis_kendaraan");
                String namaPemilik = resultSet.getString("nama_pemilik");
                String kategoriPemilik = resultSet.getString("kategori_pemilik");

                vehicles.add(new Vehicle(nomorKendaraan, jenisKendaraan, namaPemilik, kategoriPemilik));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    // Method untuk memperbarui data kendaraan
    public void updateVehicleInDatabase(int id, String nomorKendaraan, String jenisKendaraan, String namaPemilik, String kategoriPemilik) {
        String query = "UPDATE kendaraan SET nomor_kendaraan = ?, jenis_kendaraan = ?, nama_pemilik = ?, kategori_pemilik = ? WHERE id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, nomorKendaraan);
            statement.setString(2, jenisKendaraan);
            statement.setString(3, namaPemilik);
            statement.setString(4, kategoriPemilik);
            statement.setInt(5, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method untuk menghapus kendaraan dari database
    public void deleteVehicleFromDatabase(int id) {
        String query = "DELETE FROM kendaraan WHERE id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}