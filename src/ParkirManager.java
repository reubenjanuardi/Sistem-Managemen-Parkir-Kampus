import java.sql.*;
import java.util.ArrayList;

public class ParkirManager {
    // Method untuk mengecek apakah slot masih tersedia
    public static boolean isSlotAvailable(String kategoriArea, String jenisArea) {
    String query = "SELECT total_slot, slot_terisi FROM area_parkir WHERE kategori_area = ? AND jenis_area = ?";
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, kategoriArea);
            stmt.setString(2, jenisArea);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int totalSlot = rs.getInt("total_slot");
                int slotTerisi = rs.getInt("slot_terisi");

                // Tambahkan log untuk debug
                System.out.println("Kategori Area: " + kategoriArea + ", Jenis Area: " + jenisArea);
                System.out.println("Total Slot: " + totalSlot + ", Slot Terisi: " + slotTerisi);

                return slotTerisi < totalSlot;
            } else {
                System.out.println("Data area parkir tidak ditemukan!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method untuk menambahkan kendaraan ke dalam slot parkir
    public static boolean parkVehicle(Vehicle vehicle, String kategoriArea, String jenisArea) {
        // Langkah 1: Cek apakah slot tersedia
        if (!isSlotAvailable(vehicle.getKategoriPemilik(), vehicle.getJenisKendaraan())) {
            System.out.println("Slot penuh untuk kategori: " + vehicle.getKategoriPemilik() + 
                            ", jenis: " + vehicle.getJenisKendaraan());
            return false; // Slot penuh
        }

        // Langkah 2: Cek apakah kendaraan sudah parkir
        if (isVehicleAlreadyParked(vehicle.getNomorKendaraan())) {
            System.out.println("Kendaraan dengan nomor " + vehicle.getNomorKendaraan() + " sudah parkir!");
            return false; // Kendaraan sudah ada
        }

        // Langkah 3: Update slot parkir dan tambahkan kendaraan
        String updateQuery = "UPDATE area_parkir SET slot_terisi = slot_terisi + 1 " + "WHERE kategori_area = ? AND jenis_area = ?";
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

            // Update slot_terisi
            updateStmt.setString(1, vehicle.getKategoriPemilik());
            updateStmt.setString(2, vehicle.getJenisKendaraan());
            updateStmt.executeUpdate();

            // Simpan data kendaraan ke tabel kendaraan_terparkir
            saveKendaraanTerparkir(vehicle, kategoriArea, jenisArea);
            LogKendaraan.catatKendaraanMasuk(vehicle, kategoriArea, jenisArea);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isVehicleAlreadyParked(String nomorKendaraan) {
        String query = "SELECT COUNT(*) AS count FROM kendaraan_terparkir WHERE nomor_kendaraan = ?";
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nomorKendaraan);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0; // Jika ditemukan, berarti kendaraan sudah parkir
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean unloadVehicle(Vehicle vehicle, String kategoriArea, String jenisArea) {
        // Query untuk mengambil detail kendaraan yang keluar
        String selectQuery = "SELECT kategori_area, jenis_area FROM kendaraan_terparkir WHERE nomor_kendaraan = ?";
        String deleteQuery = "DELETE FROM kendaraan_terparkir WHERE nomor_kendaraan = ?";
        String updateSlotQuery = "UPDATE area_parkir SET slot_terisi = slot_terisi - 1 WHERE kategori_area = ? AND jenis_area = ?";
    
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateSlotQuery)) {
    
            // Ambil informasi kategori_area dan jenis_area berdasarkan nomor kendaraan
            selectStmt.setString(1, vehicle.getNomorKendaraan());
            ResultSet rs = selectStmt.executeQuery();
    
            if (rs.next()) {
                // Hapus kendaraan dari tabel kendaraan_terparkir
                deleteStmt.setString(1, vehicle.getNomorKendaraan());
                deleteStmt.executeUpdate();
    
                // Kurangi slot terisi di tabel area_parkir
                updateStmt.setString(1, kategoriArea);
                updateStmt.setString(2, jenisArea);
                updateStmt.executeUpdate();
                
                // Menambahkan log parkir keluar
                System.out.println("Kendaraan dengan nomor " + vehicle.getNomorKendaraan() + " berhasil keluar.");
                return true;
            } else {
                System.out.println("Kendaraan dengan nomor " + vehicle.getNomorKendaraan() + " tidak ditemukan di parkiran.");
                return false;
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Method untuk mengambil semua kendaraan yang sedang parkir
    public static ArrayList<Vehicle> getParkedVehicles() {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT nomor_kendaraan, jenis_kendaraan, kategori_pemilik, waktu_masuk, kategori_area, jenis_area FROM kendaraan_terparkir";

        try (Connection conn = DatabaseUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String nomorKendaraan = rs.getString("nomor_kendaraan");
                String jenisKendaraan = rs.getString("jenis_kendaraan");
                String namaPemilik = rs.getString("nama_pemilik");
                String kategoriPemilik = rs.getString("kategori_pemilik");

                // Sesuaikan konstruktor Vehicle agar menerima parameter tambahan, jika diperlukan
                vehicles.add(new Vehicle(nomorKendaraan, jenisKendaraan, namaPemilik, kategoriPemilik));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    // Method untuk menyimpan data kendaraan ke tabel kendaraan_terparkir
    private static void saveKendaraanTerparkir(Vehicle vehicle, String kategoriArea, String jenisArea) {
        String insertQuery = "INSERT INTO kendaraan_terparkir (nomor_kendaraan, jenis_kendaraan, nama_pemilik, kategori_pemilik, waktu_masuk, kategori_area, jenis_area) " + 
                            "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setString(1, vehicle.getNomorKendaraan());
            stmt.setString(2, vehicle.getJenisKendaraan());
            stmt.setString(3, vehicle.getNamaPemilik());
            stmt.setString(4, vehicle.getKategoriPemilik());
            stmt.setString(5, kategoriArea);
            stmt.setString(6, jenisArea);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
}
