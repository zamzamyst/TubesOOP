// Kelas Controller untuk Simulasi Parkir

/* ======================================================== */

// Meng-Import Packages SQL Java (Built-in)
import java.sql.*;

// Meng-Import Packages JavaFX (Built-in)
import javafx.fxml.FXML;  
import javafx.fxml.FXMLLoader;  
import javafx.stage.Stage;  
import javafx.scene.Parent;  
import javafx.scene.Scene;  
import javafx.scene.control.Button;  
import javafx.scene.control.TextField;  
import javafx.scene.control.Alert;  

public class SimulationController {  

    @FXML  
    private TextField txtNomorKendaraan;  

    @FXML  
    private Button btnMasuk;  

    @FXML  
    private Button btnKeluar;  
    
    @FXML  
    private Button btnPengelola;  

    @FXML  
    public void initialize() {  

        // Menyetel action tiap button
        btnMasuk.setOnAction(event -> masukParkir());  
        btnKeluar.setOnAction(event -> keluarParkir());  
        btnPengelola.setOnAction(event -> openRegistrationPage());   
    }  

    // Method Void: Membuka Halaman Registrasi
    private void openRegistrationPage() {  
        try {  
            Parent root = FXMLLoader.load(getClass().getResource("RegistrationPage.fxml"));  
            Stage stage = (Stage) btnPengelola.getScene().getWindow();  
            stage.setScene(new Scene(root));  
            stage.show();  
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }

    // Method Void: Untuk aktivitas Masuk Parkir
    private void masukParkir() {

        // Ambil nomor kendaraan dari input GUI
        String nomorKendaraan = txtNomorKendaraan.getText();
    
        // Cek apakah nomor kendaraan sudah diisi?
        if (nomorKendaraan == null || nomorKendaraan.isEmpty()) {
            // Jika belum, maka Alert
            showAlert("Peringatan", "Nomor kendaraan harus diisi!");
            return;
        }
    
        // Cari data kendaraan di tabel Kendaraan Terdaftar untuk memeriksa apakah kendaraan sudah terdaftar
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_kendaraan", "root", "")) {
            String sql = "SELECT * FROM data_kendaraan WHERE nomor_kendaraan = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, nomorKendaraan);
    
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {

                    // Mengambil data dari hasil query
                    String jenisKendaraan = resultSet.getString("jenis_kendaraan");
                    String namaPemilik = resultSet.getString("nama_pemilik");
                    String kategoriPemilik = resultSet.getString("kategori_pemilik"); 
    
                    // Tentukan tabel yang akan digunakan berdasarkan kategori pemilik
                    String tabelKendaraan = "";

                    if (kategoriPemilik.equals("Mahasiswa")) {
                        tabelKendaraan = "mahasiswa_kendaraan";

                    } else if (kategoriPemilik.equals("Dosen")) {
                        tabelKendaraan = "dosen_kendaraan";

                    } else if (kategoriPemilik.equals("Tamu")) {
                        tabelKendaraan = "tamu_kendaraan";
                    }
    
                    // Memeriksa apakah kendaraan sudah masuk ke area parkirnya?
                    String sqlCheck = "SELECT COUNT(*) FROM " + tabelKendaraan + " WHERE nomor_kendaraan = ?";
                    try (PreparedStatement checkStatement = connection.prepareStatement(sqlCheck)) {
                        checkStatement.setString(1, nomorKendaraan);
                        ResultSet checkResultSet = checkStatement.executeQuery();
                        if (checkResultSet.next() && checkResultSet.getInt(1) > 0) {

                            // Jika kendaraan sudah ada di parkir, maka Alert
                            showAlert("Informasi", "Kendaraan dengan nomor " + nomorKendaraan + " sudah terdaftar di parkir.");
                            return;
                        }
    
                        // jika kendaraan belum ada di parkir
                        Kendaraan kendaraan;
                        boolean slotAvailable = false;

                        switch (kategoriPemilik) {
                            case "Mahasiswa":
                                kendaraan = new KendaraanMahasiswa(resultSet.getInt("id"), nomorKendaraan, jenisKendaraan, namaPemilik);

                                // Mengganti status slotAvailable jika pengembalian nilainya sesuai
                                slotAvailable = KendaraanMahasiswa.isMotorSlotAvailable() && jenisKendaraan.equals("Motor") // status 
                                                || KendaraanMahasiswa.isMobilSlotAvailable() && jenisKendaraan.equals("Mobil");
                                break;

                            case "Dosen":
                                kendaraan = new KendaraanDosen(resultSet.getInt("id"), nomorKendaraan, jenisKendaraan, namaPemilik);

                                // Mengganti status slotAvailable jika pengembalian nilainya sesuai
                                slotAvailable = KendaraanDosen.isMotorSlotAvailable() && jenisKendaraan.equals("Motor")
                                                || KendaraanDosen.isMobilSlotAvailable() && jenisKendaraan.equals("Mobil");
                                break;
                                
                            case "Tamu":
                                kendaraan = new KendaraanTamu(resultSet.getInt("id"), nomorKendaraan, jenisKendaraan, namaPemilik);

                                // Mengganti status slotAvailable jika pengembalian nilainya sesuai
                                slotAvailable = KendaraanTamu.isMotorSlotAvailable() && jenisKendaraan.equals("Motor")
                                                || KendaraanTamu.isMobilSlotAvailable() && jenisKendaraan.equals("Mobil");
                                break;

                            // Antisipasi apabila kategori nya tidak terdeteksi
                            default:
                                showAlert("Error", "Kategori pemilik tidak valid.");
                                return;
                        }
                        
                        // Jika status SlotAvailable nya masih false, maka Alert
                        if (!slotAvailable) {
                            showAlert("Peringatan", "Slot parkir untuk " + jenisKendaraan + " " + kategoriPemilik + " sudah penuh!");
                            return;
                        }
    
                        // Rangkaian untuk menampilkan notifikasi ketersediaan Slot Parkir (berdasarkan pemilik)
                        if (kendaraan instanceof KendaraanMahasiswa) {
                            ((KendaraanMahasiswa) kendaraan).cekSlotParkir();

                        } else if (kendaraan instanceof KendaraanDosen) {
                            ((KendaraanDosen) kendaraan).cekSlotParkir();

                        } else if (kendaraan instanceof KendaraanTamu) {
                            ((KendaraanTamu) kendaraan).cekSlotParkir();
                        }
    
                        // Menyimpan ke database
                        kendaraan.simpanKeDatabase();
                        showAlert("Informasi", "Kendaraan berhasil masuk parkir.");
                    }
                
                // Jika belum terdaftar kendaraannya
                } else {
                    showAlert("Peringatan", "Kendaraan anda belum terdaftar!");
                }
            }
        } catch (SQLException e) {
            showAlert("Error", "Gagal mengakses database: " + e.getMessage());
        }
    }
    
    // Method Void: untuk aktivitas keluar parkir 
    private void keluarParkir() {

        // Ambil nomor kendaraan dari input GUI
        String nomorKendaraan = txtNomorKendaraan.getText();
    
        // Cek apakah nomor kendaraan sudah diisi?
        if (nomorKendaraan == null || nomorKendaraan.isEmpty()) {
            showAlert("Input Tidak Valid", "Nomor kendaraan harus diisi!");
            return;
        }
    
        // Cari data kendaraan di tabel Kendaraan Terdaftar berdasarkan nomor kendaraan
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_kendaraan", "root", "")) {
            String sql = "SELECT * FROM data_kendaraan WHERE nomor_kendaraan = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, nomorKendaraan);
    
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {

                    // Ambil data dari hasil query
                    String jenisKendaraan = resultSet.getString("jenis_kendaraan");
                    String namaPemilik = resultSet.getString("nama_pemilik");
                    String kategoriPemilik = resultSet.getString("kategori_pemilik");
    
                    // Tentukan tabel yang akan digunakan berdasarkan kategori pemilik
                    String tabelKendaraan = "";

                    if (kategoriPemilik.equals("Mahasiswa")) {
                        tabelKendaraan = "mahasiswa_kendaraan";

                    } else if (kategoriPemilik.equals("Dosen")) {
                        tabelKendaraan = "dosen_kendaraan";

                    } else if (kategoriPemilik.equals("Tamu")) {
                        tabelKendaraan = "tamu_kendaraan";
                    }
    
                    // Memeriksa apakah kendaraan ada di tabel parkir (sesuai Pemilik)
                    String sqlCheck = "SELECT COUNT(*) FROM " + tabelKendaraan + " WHERE nomor_kendaraan = ?";
                    try (PreparedStatement checkStatement = connection.prepareStatement(sqlCheck)) {
                        checkStatement.setString(1, nomorKendaraan);
    
                        ResultSet checkResultSet = checkStatement.executeQuery();
                        if (checkResultSet.next() && checkResultSet.getInt(1) > 0) {

                            // Tentukan kendaraan mana yang akan dihapus (berdasarkan pemilik)
                            Kendaraan kendaraan;

                            switch (kategoriPemilik) {
                                case "Mahasiswa":
                                    kendaraan = new KendaraanMahasiswa(resultSet.getInt("id"), nomorKendaraan, jenisKendaraan, namaPemilik);
                                    break;

                                case "Dosen":
                                    kendaraan = new KendaraanDosen(resultSet.getInt("id"), nomorKendaraan, jenisKendaraan, namaPemilik);
                                    break;

                                case "Tamu":
                                    kendaraan = new KendaraanTamu(resultSet.getInt("id"), nomorKendaraan, jenisKendaraan, namaPemilik);
                                    break;

                                default:
                                    showAlert("Error", "Kategori pemilik tidak valid.");
                                    return;
                            }
    
                            // Menghapus kendaraan dari database
                            kendaraan.hapusDariDatabase();
                            showAlert("Informasi", "Kendaraan telah keluar parkir.");
                        
                        // Jika belum ada di parkiran, tapi mau keluar
                        } else {
                            showAlert("Peringatan", "Kendaraan dengan nomor " + nomorKendaraan + " belum masuk parkiran.");
                        }
                    }

                // Jika mau keluar, tapi belum daftar
                } else {
                    showAlert("Peringatan", "Kendaraan anda belum terdaftar!.");
                }
            }
        } catch (SQLException e) {
            showAlert("Error", "Gagal mengakses database: " + e.getMessage());
        }
    }
    
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}

/* 
import javafx.scene.control.Alert.AlertType;   
import javafx.scene.Node;
import java.io.IOException;
import javafx.scene.control.ChoiceBox;  
*/