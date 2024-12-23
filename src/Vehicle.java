import java.sql.*;

public class Vehicle {
    private int id; // ID yang dihasilkan dari database (auto-increment)
    private String nomorKendaraan;
    private String jenisKendaraan;
    private String namaPemilik;
    private String kategoriPemilik;
    

    // Konstruktor tanpa ID (karena ID dihasilkan secara otomatis)
    public Vehicle(String nomorKendaraan, String jenisKendaraan, String namaPemilik, String kategoriPemilik) {
        this.nomorKendaraan = nomorKendaraan;
        this.jenisKendaraan = jenisKendaraan;
        this.namaPemilik = namaPemilik;
        this.kategoriPemilik = kategoriPemilik;
    }

    // Getters dan Setters
    public int getId() {
        return id;
    }

    public String getNomorKendaraan() {
        return nomorKendaraan;
    }

    public String getJenisKendaraan() {
        return jenisKendaraan;
    }

    public String getNamaPemilik() {
        return namaPemilik;
    }

    public String getKategoriPemilik() {
        return kategoriPemilik;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNomorKendaraan(String nomorKendaraan) {
        this.nomorKendaraan = nomorKendaraan;
    }

    public void setJenisKendaraan(String jenisKendaraan) {
        this.jenisKendaraan = jenisKendaraan;
    }

    public void setNamaPemilik(String namaPemilik) {
        this.namaPemilik = namaPemilik;
    }

    public void setKategoriPemilik(String kategoriPemilik) {
        this.kategoriPemilik = kategoriPemilik;
    }

    // Method untuk menyimpan data kendaraan ke database
    public boolean saveToDatabase() {
        String dbUrl = "jdbc:sqlite:parking.db";
        String sql = "INSERT INTO vehicle (nomor_kendaraan, jenis_kendaraan, nama_pemilik ,kategori_pemilik) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl);
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Set parameter untuk query SQL
            pstmt.setString(1, this.nomorKendaraan);
            pstmt.setString(2, this.jenisKendaraan);
            pstmt.setString(3, this.namaPemilik);
            pstmt.setString(4, this.kategoriPemilik);

            // Eksekusi query INSERT
            int affectedRows = pstmt.executeUpdate();

            // Ambil ID yang dihasilkan
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.id = generatedKeys.getInt(1); // Set ID ke objek Vehicle
                        return true; // Berhasil disimpan
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saving vehicle to database: " + e.getMessage());
        }

        return false; // Gagal menyimpan
    }
}