import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;  
import javafx.fxml.FXMLLoader;  
import javafx.scene.control.Button;  
import javafx.scene.control.ChoiceBox;  
import javafx.scene.control.TextField;  
import javafx.stage.Stage;  
import javafx.scene.Parent;  
import javafx.scene.Scene;  
import javafx.scene.control.Alert;  
import javafx.scene.control.Alert.AlertType;   
import javafx.scene.Node;  

public class SimulasiController {  

    @FXML  
    private TextField txtNomorKendaraan;  

    @FXML  
    private TextField txtNamaPemilik;  

    @FXML  
    private ChoiceBox<String> choiceJenisKendaraan;  

    @FXML  
    private ChoiceBox<String> choiceKategoriPemilik;  

    @FXML  
    private Button btnMasuk;  

    @FXML  
    private Button btnKeluar;  
    
    @FXML  
    private Button btnPengelola;  

    @FXML  
    public void initialize() {  
        choiceJenisKendaraan.getItems().addAll("Motor", "Mobil");  
        choiceKategoriPemilik.getItems().addAll("Mahasiswa", "Dosen", "Tamu");  

        btnMasuk.setOnAction(event -> masukParkir());  
        btnKeluar.setOnAction(event -> hapusData());  
        btnPengelola.setOnAction(event -> openRegistrationPage());  // Memanggil metode openRegistrationPage  
    }  

    private void openRegistrationPage() {  
        try {  
            // Menggunakan FXMLLoader untuk memuat halaman PageRegistrasi.fxml  
            Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));  
            Stage stage = (Stage) txtNomorKendaraan.getScene().getWindow(); // Mendapatkan stage menggunakan salah satu TextField  
            stage.setScene(new Scene(root)); // Mengatur scene baru  
            stage.show(); // Menampilkan stage  
        } catch (IOException e) {  
            e.printStackTrace(); // Mencetak kesalahan jika terjadi IOException  
        }  
    }  

    private void masukParkir() {
        String nomorKendaraan = txtNomorKendaraan.getText();
        String kategoriPemilik = choiceKategoriPemilik.getValue();
    
        if (nomorKendaraan == null || nomorKendaraan.isEmpty() || kategoriPemilik == null || kategoriPemilik.isEmpty()) {
            showAlert("Input Tidak Valid", "Nomor kendaraan dan kategori pemilik harus diisi!");
            return;
        }
    
        // Cari data kendaraan di tabel pusat berdasarkan nomor kendaraan dan kategori pemilik
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_kendaraan", "root", "")) {
            String sql = "SELECT * FROM data_kendaraan WHERE nomor_kendaraan = ? AND kategori_pemilik = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, nomorKendaraan);
                statement.setString(2, kategoriPemilik);
    
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String jenisKendaraan = resultSet.getString("jenis_kendaraan");
                    String namaPemilik = resultSet.getString("nama_pemilik");
    
                    Kendaraan kendaraan;
                    switch (kategoriPemilik) {
                        case "Mahasiswa":
                            kendaraan = new MahasiswaParking(resultSet.getInt("id"), nomorKendaraan, jenisKendaraan, namaPemilik);
                            break;
                        case "Dosen":
                            kendaraan = new DosenParking(resultSet.getInt("id"), nomorKendaraan, jenisKendaraan, namaPemilik);
                            break;
                        case "Tamu":
                            kendaraan = new TamuParking(resultSet.getInt("id"), nomorKendaraan, jenisKendaraan, namaPemilik);
                            break;
                        default:
                            showAlert("Error", "Kategori pemilik tidak valid.");
                            return;
                    }
    
                    // Memanggil metode untuk simpan ke database
                    kendaraan.simpanKeDatabase();
                    showAlert("Sukses", "Kendaraan berhasil masuk parkir.");
                } else {
                    showAlert("Kendaraan Tidak Terdaftar", "Kendaraan dengan nomor " + nomorKendaraan + " tidak terdaftar.");
                }
            }
        } catch (SQLException e) {
            showAlert("Error", "Gagal mengakses database: " + e.getMessage());
        }
    }
    

    private void hapusData() {
        String nomorKendaraan = txtNomorKendaraan.getText();
        String kategoriPemilik = choiceKategoriPemilik.getValue();
    
        if (nomorKendaraan.isEmpty() || kategoriPemilik == null) {
            showAlert("Error", "Harap isi nomor kendaraan dan pilih kategori pemilik.");
            return;
        }
    
        Kendaraan kendaraan;
        switch (kategoriPemilik) {
            case "Mahasiswa":
                kendaraan = new MahasiswaParking(0, nomorKendaraan, "Motor", ""); // Sesuaikan jenis kendaraan dan pemilik
                break;
            case "Dosen":
                kendaraan = new DosenParking(0, nomorKendaraan, "Mobil", ""); // Sesuaikan jenis kendaraan dan pemilik
                break;
            case "Tamu":
                kendaraan = new TamuParking(0, nomorKendaraan, "Motor", ""); // Sesuaikan jenis kendaraan dan pemilik
                break;
            default:
                showAlert("Error", "Kategori pemilik tidak valid.");
                return;
        }
    
        try {
            kendaraan.hapusDariDatabase();  // Pemanggilan metode yang melemparkan exception jika ada masalah
            showAlert("Sukses", "Data kendaraan berhasil dihapus.");
        } catch (Exception e) {
            showAlert("Error", e.getMessage());  // Menampilkan pesan error yang dilempar oleh exception
        }
    }
    
    

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}