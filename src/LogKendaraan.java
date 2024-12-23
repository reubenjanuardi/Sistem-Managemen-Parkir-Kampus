import java.sql.*;

public class LogKendaraan {
    private String nomorKendaraan;
    private String kategori;
    private String jenisKendaraan;
    private String waktuMasuk;
    private String waktuKeluar;

    public LogKendaraan(String nomorKendaraan, String kategori, String jenisKendaraan, String waktuMasuk, String waktuKeluar) {
        this.nomorKendaraan = nomorKendaraan;
        this.kategori = kategori;
        this.jenisKendaraan = jenisKendaraan;
        this.waktuMasuk = waktuMasuk;
        this.waktuKeluar = waktuKeluar;
    }

    public String getNomorKendaraan() { return nomorKendaraan; }
    public String getKategori() { return kategori; }
    public String getJenisKendaraan() { return jenisKendaraan; }
    public String getWaktuMasuk() { return waktuMasuk; }
    public String getWaktuKeluar() { return waktuKeluar; }

    // Method untuk menyimpan log_parkir masuk ke tabel log_parkir
    public static void catatKendaraanMasuk(Vehicle vehicle, String kategoriArea, String jenisArea) {
        String insertQuery = "INSERT INTO log_parkir (nomor_kendaraan, kategori, jenis, waktu_masuk, waktu_keluar) " + 
                            "VALUES (?, ?, ?, CURRENT_TIMESTAMP, NULL)";
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setString(1, vehicle.getNomorKendaraan());
            stmt.setString(2, kategoriArea);
            stmt.setString(3, jenisArea);
            stmt.executeUpdate(); 

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method untuk mengupdate log_parkir keluar ke tabel log_parkir
    public static void catatKendaraanKeluar(Vehicle vehicle, String kategoriArea, String jenisArea) {
        String updateQuery = "UPDATE log_parkir SET waktu_keluar = CURRENT_TIMESTAMP WHERE nomor_kendaraan = ? AND waktu_keluar IS NULL";
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setString(1, vehicle.getNomorKendaraan());
            stmt.executeUpdate(); 

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
