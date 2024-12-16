// === KendaraanDAO.java ===
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KendaraanDAO {
    public void pendaftaranKendaraan(Kendaraan kendaraan) {
        String query = "INSERT INTO kendaraan (nomor_kendaraan, jenis_kendaraan, nama_pemilik, kategori_pemilik) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, kendaraan.getNomorKendaraan());
            stmt.setString(2, kendaraan.getJenisKendaraan());
            stmt.setString(3, kendaraan.getNamaPemilik());
            stmt.setString(4, kendaraan.getKategoriPemilik());
            stmt.executeUpdate();
            System.out.println("Pendaftaran kendaraan berhasil.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void kendaraanMasuk(String nomorKendaraan) {
        String query = "UPDATE kendaraan SET waktu_masuk = CURRENT_TIMESTAMP WHERE nomor_kendaraan = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nomorKendaraan);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Kendaraan berhasil masuk parkir.");
            } else {
                System.out.println("Kendaraan tidak ditemukan.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void kendaraanKeluar(String nomorKendaraan, String kategoriPemilik) {
        String query = "UPDATE kendaraan SET waktu_keluar = CURRENT_TIMESTAMP WHERE nomor_kendaraan = ? AND kategori_pemilik = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nomorKendaraan);
            stmt.setString(2, kategoriPemilik);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Kendaraan berhasil keluar parkir.");
            } else {
                System.out.println("Kendaraan tidak ditemukan atau kategori salah.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void laporanAkhir() {
        String query = "SELECT * FROM kendaraan";
        try (Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            List<Kendaraan> laporan = new ArrayList<>();
            while (rs.next()) {
                Kendaraan kendaraan = new Kendaraan();
                kendaraan.setId(rs.getInt("id"));
                kendaraan.setNomorKendaraan(rs.getString("nomor_kendaraan"));
                kendaraan.setJenisKendaraan(rs.getString("jenis_kendaraan"));
                kendaraan.setNamaPemilik(rs.getString("nama_pemilik"));
                kendaraan.setKategoriPemilik(rs.getString("kategori_pemilik"));
                kendaraan.setWaktuMasuk(rs.getTimestamp("waktu_masuk"));
                kendaraan.setWaktuKeluar(rs.getTimestamp("waktu_keluar"));
                laporan.add(kendaraan);
            }

            System.out.println("=== Laporan Akhir ===");
            for (Kendaraan k : laporan) {
                System.out.println("Nomor: " + k.getNomorKendaraan() + ", Pemilik: " + k.getNamaPemilik() + ", Masuk: " + k.getWaktuMasuk() + ", Keluar: " + k.getWaktuKeluar());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
