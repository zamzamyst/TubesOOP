import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.SQLException;  

public class KendaraanImpl extends Kendaraan {  
    private String kategoriPemilik;  

    public KendaraanImpl(int id, String nomorKendaraan, String jenisKendaraan, String namaPemilik, String kategoriPemilik) {  
        super(id, nomorKendaraan, jenisKendaraan, namaPemilik);  
        this.kategoriPemilik = kategoriPemilik;  
    }  

    public String getKategoriPemilik() {  
        return kategoriPemilik;  
    }  

    @Override  
    public void simpanKeDatabase() {  
        // Menyimpan data kendaraan ke database  
        String sql = "INSERT INTO data_kendaraan (nomor_kendaraan, jenis_kendaraan, nama_pemilik, kategori_pemilik) VALUES (?, ?, ?, ?)";  

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_kendaraan", "root", "")) {  
            try (PreparedStatement statement = connection.prepareStatement(sql)) {  
                statement.setString(1, getNomorKendaraan());  
                statement.setString(2, getJenisKendaraan());  
                statement.setString(3, getNamaPemilik());  
                statement.setString(4, kategoriPemilik); // Menggunakan kategori pemilik yang di-inisialisasi  

                int rowsInserted = statement.executeUpdate();  
                if (rowsInserted > 0) {  
                    System.out.println("Data kendaraan berhasil disimpan.");  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
            System.err.println("Gagal menyimpan data kendaraan.");  
        }  
    }  

    @Override  
    public void hapusDariDatabase() {  
        // Menghapus data kendaraan berdasarkan nomor kendaraan  
        String sql = "DELETE FROM data_kendaraan WHERE nomor_kendaraan = ?";  

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_kendaraan", "root", "")) {  
            try (PreparedStatement statement = connection.prepareStatement(sql)) {  
                statement.setString(1, getNomorKendaraan());  

                int rowsDeleted = statement.executeUpdate();  
                if (rowsDeleted > 0) {  
                    System.out.println("Data kendaraan berhasil dihapus.");  
                } else {  
                    System.out.println("Data kendaraan tidak ditemukan.");  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
            System.err.println("Gagal menghapus data kendaraan.");  
        }  
    }  
}