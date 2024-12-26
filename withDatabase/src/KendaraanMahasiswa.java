// SubClass Kendaraan

/* ======================================================== */

// Meng-Import Packages SQL Java (Built-in)
import java.sql.*;

// Meng-Import Packages JavaFX (Built-in)
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;  

/* ===================================================================================== */

// Mendeklarasikan Atributnya
public class KendaraanMahasiswa extends Kendaraan {  
    private static final int KAPASITAS_MOTOR = 2; // kapasitas maksimum motor  
    private static final int KAPASITAS_MOBIL = 2; // kapasitas maksimum mobil  
    private static int jumlahMotor = 0;  // jumlah default motor
    private static int jumlahMobil = 0;  // jumlah default mobil
    
    // Constructors untuk Kendaraan Mahasiswa
    public KendaraanMahasiswa(int id, String nomorKendaraan, String jenisKendaraan, String namaPemilik) {  
        super(id, nomorKendaraan, jenisKendaraan, namaPemilik, "Mahasiswa");  
    }  

/* ========================================================================================================================================================================= */

    // Proses statis untuk Menghubungkan, Pengecekan, dan Menyesuaikan Jumlah Kendaraan dalam DB
    static {  
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_kendaraan", "root", "")) {  // koneksi DB
            String sqlMotor = "SELECT COUNT(*) FROM mahasiswa_kendaraan WHERE jenis_kendaraan = 'Motor'";  // Query SQL untuk hitung jumlah motor yang sudah ada dalam DB 
            String sqlMobil = "SELECT COUNT(*) FROM mahasiswa_kendaraan WHERE jenis_kendaraan = 'Mobil'";  // Query SQL untuk hitung jumlah motor yang sudah ada dalam DB
            
            try (PreparedStatement statementMotor = connection.prepareStatement(sqlMotor);  
                PreparedStatement statementMobil = connection.prepareStatement(sqlMobil);  
                ResultSet resultSetMotor = statementMotor.executeQuery();  
                ResultSet resultSetMobil = statementMobil.executeQuery()) {  

                // Mengecek jumlah motor pada DB, jika ada, maka akan mengganti nilai default menjadi jumlah yang ada pada DB.
                if (resultSetMotor.next()) {  
                    jumlahMotor = resultSetMotor.getInt(1);   
                }  

                // Mengecek jumlah mobil pada DB, jika ada, maka akan mengganti nilai default menjadi jumlah yang ada pada DB.
                if (resultSetMobil.next()) {  
                    jumlahMobil = resultSetMobil.getInt(1);  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  

/* ============================== Method Return: Menetapkan/mengembalikan nilai saat dipanggil di kelas lain ======================================= */

    // Method Return 1: Mengembalikan status terkait slot parkir motor
    public static boolean isMotorSlotAvailable() {  
        return jumlahMotor < KAPASITAS_MOTOR;   // nilai yang ditetapkan/dikembalikan
    }  


    // Method Return 2: Mengembalikan status terkait slot parkir mobil
    public static boolean isMobilSlotAvailable() {  
        return jumlahMobil < KAPASITAS_MOBIL;   // nilai yang ditetapkan/dikembalikan
    }  

/* ========== Method Void: Tidak Menetapkan/mengembalikan nilai saat dipanggil di kelas lain, melainkan hanya melakukan aksi/fungsi  ============== */

    // Method Void 1: Melakukan aksi untuk menyimpan ke DB Parkiran Kendaraan Mahasiswa
    @Override
    public void simpanKeDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_kendaraan", "root", "")) {

            // Melakukan prosesi INSERT ke DB Parkiran Mahasiswa
            String sql = "INSERT INTO mahasiswa_kendaraan (nomor_kendaraan, nama_pemilik, jenis_kendaraan) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, getNomorKendaraan());
                statement.setString(2, getNamaPemilik());
                statement.setString(3, getJenisKendaraan());

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Berhasil menyimpan data kendaraan."); // SCRIPT UNTUK OUTPUT TERMINAL, BUKAN NOTIFIKASI GUI

                    
                    // Cek apakah yang masuk Mobil/Motor?
                    if (getJenisKendaraan().equals("Motor")) {
                        jumlahMotor++;  // Menambah jumlah motor
                        
                    } else if (getJenisKendaraan().equals("Mobil")) {
                        jumlahMobil++;  // Menambah jumlah mobil
                    }
                    
                } else {
                    System.out.println("Gagal menyimpan data kendaraan."); // SCRIPT UNTUK OUTPUT TERMINAL, BUKAN NOTIFIKASI GUI
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Method Void 2: Melakukan aksi untuk menyimpan ke DB Parkiran Kendaraan Mahasiswa
    @Override
    public void hapusDariDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_kendaraan", "root", "")) { // koneksi DB

            // Melakukan prosesi DELETE dari DB Parkiran Mahasiswa 
            String deleteSql = "DELETE FROM mahasiswa_kendaraan WHERE nomor_kendaraan = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
                deleteStatement.setString(1, getNomorKendaraan());
                
                int rowsDeleted = deleteStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Data kendaraan berhasil dihapus."); // SCRIPT UNTUK OUTPUT TERMINAL, BUKAN NOTIFIKASI GUI
                    
                    // Cek apakah Motor/Mobil
                    if (getJenisKendaraan().equals("Motor")) {
                        jumlahMotor--;  // Mengurangi jumlah motor

                    } else if (getJenisKendaraan().equals("Mobil")) {
                        jumlahMobil--;  // Mengurangi jumlah mobil
                    }

                } else {
                    throw new Exception("Kendaraan tidak ditemukan."); // SCRIPT UNTUK OUTPUT TERMINAL, BUKAN NOTIFIKASI GUI
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    // Method Void 3: Melakukan aksi untuk Mengecek Slot Kendaraan di DB Parkiran Mahasiswa
    public void cekSlotParkir() {
        String message = "";
    
        // Prosesi Mengecek Ketersediaan Slot Parkir, Apakah Slot Motor/Mobil?

        // Jika Motor:
        if (getJenisKendaraan().equals("Motor")) {
            
            // Menggunakan/Memanggil Method Return 1 untuk mengecek:
            if (isMotorSlotAvailable()) {
                message = "Kapasitas Motor: " + KAPASITAS_MOTOR + "\nSlot Terisi: " + jumlahMotor + "\nSlot Tersisa: " + (KAPASITAS_MOTOR - jumlahMotor); // Output jika nilai yang dikembalikan sesuai (slot masih ada)
            }

        // Jika Mobil
        } else if (getJenisKendaraan().equals("Mobil")) {
            
            // Menggunakan/Memanggil Method Return 2 untuk mengecek:
            if (isMobilSlotAvailable()) {
                message = "Kapasitas Mobil: " + KAPASITAS_MOBIL + "\nSlot Terisi: " + jumlahMobil + "\nSlot Tersisa: " + (KAPASITAS_MOBIL - jumlahMobil); // Output jika nilai yang dikembalikan sesuai (slot masih ada)
            }
        }
    
        // Menampilkan notifikasi GUI dengan alert terkait informasi slot parkirnya
        showAlert("Informasi", message);
    }
    
    // Method Void 4: Hanya untuk menjalankan aksi/fungsi alert terkait informasi Slot Parkir Mahasiswa
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Slot Parkir Mahasiswa");
        alert.setContentText(message);
        alert.showAndWait();
    }
}




// TIDAK PERLU DIJELASKAN ATAU DIBACA!!! HANYA DRAFT.


/*
 // Melakukan prosesi SELECT ke DB Parkiran Mahasiswa (Mengecek apakah nomor kendaraan ada di parkiran)
            String checkSql = "SELECT COUNT(*) AS total FROM mahasiswa_kendaraan WHERE nomor_kendaraan = ?"; 
            try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
                checkStatement.setString(1, getNomorKendaraan());
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt("total") == 0) {
                        throw new Exception("Nomor kendaraan tidak ditemukan di database dosen.");
                    }
                }
            }
 */