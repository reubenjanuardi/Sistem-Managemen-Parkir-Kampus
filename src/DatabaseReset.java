import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseReset {

    private static final String DATABASE_URL = "jdbc:sqlite:parking.db";

    public static void setupDatabase() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             Statement statement = connection.createStatement()) {

            // Drop all existing tables
            statement.executeUpdate("DROP TABLE IF EXISTS log_parkir;");
            statement.executeUpdate("DROP TABLE IF EXISTS kendaraan;");
            statement.executeUpdate("DROP TABLE IF EXISTS area_parkir;");
            statement.executeUpdate("DROP TABLE IF EXISTS kendaraan_terparkir;");

            // Create kendaraan table
            String createKendaraanTable = """
                CREATE TABLE kendaraan (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nomor_kendaraan TEXT NOT NULL UNIQUE,
                    jenis_kendaraan TEXT NOT NULL,
                    nama_pemilik TEXT NOT NULL,
                    kategori_pemilik TEXT NOT NULL
                );
            """;
            statement.executeUpdate(createKendaraanTable);

            // Create area_parkir table
            String createAreaParkirTable = """
                CREATE TABLE area_parkir (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    kategori_area TEXT NOT NULL,
                    jenis_area TEXT NOT NULL,
                    total_slot INTEGER NOT NULL,
                    slot_terisi INTEGER NOT NULL DEFAULT 0
                );
            """;
            statement.executeUpdate(createAreaParkirTable);

            // INSERT INTO area_parkir
            String insertAreaParkir = """
                INSERT INTO area_parkir (kategori_area, jenis_area, total_slot, slot_terisi)
                VALUES ('Mahasiswa', 'Mobil', 15, 0),('Mahasiswa', 'Motor', 35, 0);

                INSERT INTO area_parkir (kategori_area, jenis_area, total_slot, slot_terisi)
                VALUES ('Dosen', 'Mobil', 15, 0), ('Dosen', 'Motor', 25, 0);

                INSERT INTO area_parkir (kategori_area, jenis_area, total_slot, slot_terisi)
                VALUES ('Tamu', 'Mobil', 10, 0), ('Tamu', 'Motor', 20, 0);
                """;
            statement.executeUpdate(insertAreaParkir);

            // Create log_parkir table
            String createLogParkirTable = """
                CREATE TABLE log_parkir (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nomor_kendaraan TEXT NOT NULL,
                    kategori TEXT NOT NULL,
                    jenis TEXT NOT NULL,
                    waktu_masuk TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    waktu_keluar TIMESTAMP,
                    FOREIGN KEY (nomor_kendaraan) REFERENCES kendaraan(nomor_kendaraan),
                    FOREIGN KEY (kategori) REFERENCES area_parkir(kategori_area),
                    FOREIGN KEY (jenis) REFERENCES area_parkir(jenis_area)
                );
            """;
            statement.executeUpdate(createLogParkirTable);

            // Create kendaraan_terparkir
            String createKendaraanTerparkir = """
                CREATE TABLE kendaraan_terparkir (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nomor_kendaraan TEXT NOT NULL,
                    jenis_kendaraan TEXT NOT NULL,
                    nama_pemilik TEXT NOT NULL,
                    kategori_pemilik TEXT NOT NULL,
                    waktu_masuk TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    kategori_area TEXT NOT NULL,
                    jenis_area TEXT NOT NULL
                );
            """;
            statement.executeUpdate(createKendaraanTerparkir);

            System.out.println("Database setup completed successfully!");

        } catch (Exception e) {
            System.err.println("Database setup failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        setupDatabase();
    }
}