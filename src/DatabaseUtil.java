import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    // Menggunakan path ke file SQLite
    private static final String DATABASE_URL = "jdbc:sqlite:parking.db";  // Ganti dengan path file SQLite Anda

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
}