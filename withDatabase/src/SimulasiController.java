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
        btnKeluar.setOnAction(event -> keluarParkir());  
        btnPengelola.setOnAction(event -> openRegistrationPage());  // Memanggil metode openRegistrationPage  
    }  

    private void openRegistrationPage() {  
        try {  
            Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));  
            Stage stage = (Stage) btnPengelola.getScene().getWindow();  
            stage.setScene(new Scene(root));  
            stage.show();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }

    private void masukParkir() {
        // Ambil nomor kendaraan dari input
        String nomorKendaraan = txtNomorKendaraan.getText();
    
        // Validasi input
        if (nomorKendaraan == null || nomorKendaraan.isEmpty()) {
            showAlert("Input Tidak Valid", "Nomor kendaraan harus diisi!");
            return;
        }
    
        // Cari data kendaraan di tabel parkir untuk memeriksa apakah kendaraan sudah terdaftar
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_kendaraan", "root", "")) {
            String sql = "SELECT * FROM data_kendaraan WHERE nomor_kendaraan = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, nomorKendaraan);
    
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    // Ambil data dari hasil query
                    String jenisKendaraan = resultSet.getString("jenis_kendaraan");
                    String namaPemilik = resultSet.getString("nama_pemilik");
                    String kategoriPemilik = resultSet.getString("kategori_pemilik"); // Ambil kategori pemilik
    
                    // Tentukan tabel yang akan digunakan berdasarkan kategori pemilik
                    String tabelKendaraan = "";
                    if (kategoriPemilik.equals("Mahasiswa")) {
                        tabelKendaraan = "mahasiswa_kendaraan";
                    } else if (kategoriPemilik.equals("Dosen")) {
                        tabelKendaraan = "dosen_kendaraan";
                    } else if (kategoriPemilik.equals("Tamu")) {
                        tabelKendaraan = "tamu_kendaraan";
                    }
    
                    // Memeriksa apakah kendaraan sudah terdaftar di parkir
                    String sqlCheck = "SELECT COUNT(*) FROM " + tabelKendaraan + " WHERE nomor_kendaraan = ?";
                    try (PreparedStatement checkStatement = connection.prepareStatement(sqlCheck)) {
                        checkStatement.setString(1, nomorKendaraan);
    
                        ResultSet checkResultSet = checkStatement.executeQuery();
                        if (checkResultSet.next() && checkResultSet.getInt(1) > 0) {
                            // Jika kendaraan sudah ada di parkir, tampilkan pesan
                            showAlert("Kendaraan Sudah Terdaftar", "Kendaraan dengan nomor " + nomorKendaraan + " sudah terdaftar di parkir.");
                            return;
                        }
    
                        // Lanjutkan jika kendaraan belum ada di parkir
                        Kendaraan kendaraan;
                        boolean slotAvailable = false;
                        switch (kategoriPemilik) {
                            case "Mahasiswa":
                                kendaraan = new MahasiswaParking(resultSet.getInt("id"), nomorKendaraan, jenisKendaraan, namaPemilik);
                                slotAvailable = MahasiswaParking.isMotorSlotAvailable() && jenisKendaraan.equals("Motor")
                                                || MahasiswaParking.isMobilSlotAvailable() && jenisKendaraan.equals("Mobil");
                                break;
                            case "Dosen":
                                kendaraan = new DosenParking(resultSet.getInt("id"), nomorKendaraan, jenisKendaraan, namaPemilik);
                                slotAvailable = DosenParking.isMotorSlotAvailable() && jenisKendaraan.equals("Motor")
                                                || DosenParking.isMobilSlotAvailable() && jenisKendaraan.equals("Mobil");
                                break;
                            case "Tamu":
                                kendaraan = new TamuParking(resultSet.getInt("id"), nomorKendaraan, jenisKendaraan, namaPemilik);
                                slotAvailable = TamuParking.isMotorSlotAvailable() && jenisKendaraan.equals("Motor")
                                                || TamuParking.isMobilSlotAvailable() && jenisKendaraan.equals("Mobil");
                                break;
                            default:
                                showAlert("Error", "Kategori pemilik tidak valid.");
                                return;
                        }
    
                        if (!slotAvailable) {
                            showAlert("Slot Penuh", "Tidak ada slot parkir tersedia untuk kendaraan jenis " + jenisKendaraan + " milik " + kategoriPemilik);
                            return;
                        }
    
                        // Cek slot parkir berdasarkan jenis kendaraan
                        if (kendaraan instanceof MahasiswaParking) {
                            ((MahasiswaParking) kendaraan).cekSlotParkir();
                        } else if (kendaraan instanceof DosenParking) {
                            ((DosenParking) kendaraan).cekSlotParkir();
                        } else if (kendaraan instanceof TamuParking) {
                            ((TamuParking) kendaraan).cekSlotParkir();
                        }
    
                        // Memanggil metode untuk simpan ke database
                        kendaraan.simpanKeDatabase();
                        showAlert("Sukses", "Kendaraan berhasil masuk parkir.");
                    }
                } else {
                    showAlert("Kendaraan Tidak Terdaftar", "Kendaraan dengan nomor " + nomorKendaraan + " tidak terdaftar.");
                }
            }
        } catch (SQLException e) {
            showAlert("Error", "Gagal mengakses database: " + e.getMessage());
        }
    }
    
    
    
    private void keluarParkir() {
        // Ambil nomor kendaraan dari input
        String nomorKendaraan = txtNomorKendaraan.getText();
    
        // Validasi input
        if (nomorKendaraan == null || nomorKendaraan.isEmpty()) {
            showAlert("Input Tidak Valid", "Nomor kendaraan harus diisi!");
            return;
        }
    
        // Cari data kendaraan di tabel parkir berdasarkan nomor kendaraan
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_kendaraan", "root", "")) {
            String sql = "SELECT * FROM data_kendaraan WHERE nomor_kendaraan = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, nomorKendaraan);
    
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    // Ambil data dari hasil query
                    String jenisKendaraan = resultSet.getString("jenis_kendaraan");
                    String namaPemilik = resultSet.getString("nama_pemilik");
                    String kategoriPemilik = resultSet.getString("kategori_pemilik"); // Ambil kategori pemilik
    
                    // Tentukan tabel yang akan digunakan berdasarkan kategori pemilik
                    String tabelKendaraan = "";
                    if (kategoriPemilik.equals("Mahasiswa")) {
                        tabelKendaraan = "mahasiswa_kendaraan";
                    } else if (kategoriPemilik.equals("Dosen")) {
                        tabelKendaraan = "dosen_kendaraan";
                    } else if (kategoriPemilik.equals("Tamu")) {
                        tabelKendaraan = "tamu_kendaraan";
                    }
    
                    // Memeriksa apakah kendaraan ada di tabel parkir yang sesuai
                    String sqlCheck = "SELECT COUNT(*) FROM " + tabelKendaraan + " WHERE nomor_kendaraan = ?";
                    try (PreparedStatement checkStatement = connection.prepareStatement(sqlCheck)) {
                        checkStatement.setString(1, nomorKendaraan);
    
                        ResultSet checkResultSet = checkStatement.executeQuery();
                        if (checkResultSet.next() && checkResultSet.getInt(1) > 0) {
                            // Tentukan kendaraan sesuai kategori pemilik
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
    
                            // Memanggil metode untuk hapus dari database
                            kendaraan.hapusDariDatabase();
                            showAlert("Sukses", "Kendaraan berhasil keluar parkir.");
                        } else {
                            showAlert("Kendaraan Tidak Terdaftar", "Kendaraan dengan nomor " + nomorKendaraan + " tidak terdaftar di parkir.");
                        }
                    }
                } else {
                    showAlert("Kendaraan Tidak Terdaftar", "Kendaraan dengan nomor " + nomorKendaraan + " tidak terdaftar di parkir.");
                }
            }
        } catch (SQLException e) {
            showAlert("Error", "Gagal mengakses database: " + e.getMessage());
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