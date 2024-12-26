// SubClass Kendaraan

/* ======================================================== */

// Meng-Import Packages SQL Java (Built-in)
import java.sql.*;

public class KendaraanRegistered extends Kendaraan {  
    private String kategoriPemilik;  

    // Constructors untuk Kendaraan yang Terdaftar
    public KendaraanRegistered(int id, String nomorKendaraan, String jenisKendaraan, String namaPemilik, String kategoriPemilik) {  
        super(id, nomorKendaraan, jenisKendaraan, namaPemilik);  
        this.kategoriPemilik = kategoriPemilik;  
    }  

    // Getter untuk mengembalikan nilai Kategori Pemilik
    public String getKategoriPemilik() {  
        return kategoriPemilik;  
    }  

/* ========== Method Void: Tidak Menetapkan/mengembalikan nilai saat dipanggil di kelas lain, melainkan hanya melakukan aksi/fungsi  ============== */

    // Method Void 1: Melakukan aksi untuk menyimpan ke DB Kendaraan Terdaftara    
    @Override  
    public void simpanKeDatabase() {  
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_kendaraan", "root", "")) { 
            
            // Melakukan prosesi INSERT ke DB Kendaraan Terdaftar
            String sql = "INSERT INTO data_kendaraan (nomor_kendaraan, jenis_kendaraan, nama_pemilik, kategori_pemilik) VALUES (?, ?, ?, ?)";              
            try (PreparedStatement statement = connection.prepareStatement(sql)) {  
                statement.setString(1, getNomorKendaraan());  
                statement.setString(2, getJenisKendaraan());  
                statement.setString(3, getNamaPemilik());  
                statement.setString(4, kategoriPemilik);

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


    // Method Void 2: Melakukan aksi untuk menyimpan ke DB Kendaraan Terdaftar
    @Override  
    public void hapusDariDatabase() {  
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_kendaraan", "root", "")) {  
            
            // Melakukan prosesi DELETE dari DB Kendaraan Terdaftar
            String sql = "DELETE FROM data_kendaraan WHERE nomor_kendaraan = ?";  
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