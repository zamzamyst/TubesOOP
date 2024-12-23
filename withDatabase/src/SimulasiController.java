import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;  
import javafx.fxml.FXMLLoader;  
import javafx.scene.Parent;  
import javafx.scene.Scene;  
import javafx.scene.control.Alert;  
import javafx.scene.control.Button;  
import javafx.scene.control.TextField;  
import javafx.scene.control.ChoiceBox;  
import javafx.stage.Stage;  

import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  

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
    private ObservableList<Kendaraan> kendaraanList = FXCollections.observableArrayList();  


    private static final String URL = "jdbc:mysql://localhost:3306/data_kendaraan"; // Ganti dengan nama database Anda  
    private static final String USER = "root"; // Ganti dengan username database Anda  
    private static final String PASSWORD = ""; // Ganti dengan password database Anda  

    @FXML  
    private void initialize() {  
        // Menambahkan pilihan di ChoiceBox  
        choiceJenisKendaraan.getItems().addAll("mobil", "motor");  
        choiceKategoriPemilik.getItems().addAll("mahasiswa", "dosen", "tamu");  

        // Menetapkan event handler pada tombol  
        btnMasuk.setOnAction(event -> masukkanKendaraan());  
        btnKeluar.setOnAction(event -> keluarkanKendaraan());  
        btnPengelola.setOnAction(event -> openMainPage());   

        initializeParkingSlots(); 
    }  

    private void initializeParkingSlots() {  
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {  
            // Menambahkan slot parkir jika belum ada
            String sql = "INSERT INTO slot_parkir (area, jenis_kendaraan, kapasitas, terisi) " +  
                         "SELECT 'mahasiswa', 'motor', 10, 0 " +  
                         "WHERE NOT EXISTS (SELECT 1 FROM slot_parkir WHERE area = 'mahasiswa' AND jenis_kendaraan = 'motor') " +  
                         "UNION SELECT 'mahasiswa', 'mobil', 15, 0 " +  
                         "WHERE NOT EXISTS (SELECT 1 FROM slot_parkir WHERE area = 'mahasiswa' AND jenis_kendaraan = 'mobil') " +  
                         "UNION SELECT 'dosen', 'motor', 5, 0 " +  
                         "WHERE NOT EXISTS (SELECT 1 FROM slot_parkir WHERE area = 'dosen' AND jenis_kendaraan = 'motor') " +  
                         "UNION SELECT 'dosen', 'mobil', 8, 0 " +  
                         "WHERE NOT EXISTS (SELECT 1 FROM slot_parkir WHERE area = 'dosen' AND jenis_kendaraan = 'mobil') " +  
                         "UNION SELECT 'tamu', 'motor', 5, 0 " +  
                         "WHERE NOT EXISTS (SELECT 1 FROM slot_parkir WHERE area = 'tamu' AND jenis_kendaraan = 'motor') " +  
                         "UNION SELECT 'tamu', 'mobil', 7, 0 " +  
                         "WHERE NOT EXISTS (SELECT 1 FROM slot_parkir WHERE area = 'tamu' AND jenis_kendaraan = 'mobil');";  
    
            try (PreparedStatement statement = connection.prepareStatement(sql)) {  
                statement.executeUpdate();  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  

    private void openMainPage() {  
        try {  
            Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));  
            Stage stage = (Stage) btnPengelola.getScene().getWindow();  
            stage.setScene(new Scene(root));  
            stage.show();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  

    private void masukkanKendaraan() {  
        // Validasi input  
        if (txtNomorKendaraan.getText().isEmpty() || txtNamaPemilik.getText().isEmpty() ||  
            choiceJenisKendaraan.getValue() == null || choiceKategoriPemilik.getValue() == null) {  
            showAlert("Input Tidak Valid", "Silakan lengkapi semua input!", Alert.AlertType.WARNING);  
            return;  
        }  

        // Cek ketersediaan slot  
        String jenisKendaraan = choiceJenisKendaraan.getValue();  
        String kategoriPemilik = choiceKategoriPemilik.getValue();  

        if (!isSlotAvailable(jenisKendaraan, kategoriPemilik)) {  
            showAlert("Peringatan", "Tidak ada slot tersedia untuk jenis kendaraan dan kategori pemilik ini!", Alert.AlertType.WARNING);  
            return;  
        }  

        // Tambahkan kendaraan ke list  
        Kendaraan kendaraanBaru = new Kendaraan(  
            kendaraanList.size() + 1, // ID otomatis  
            txtNomorKendaraan.getText(),  
            jenisKendaraan,  
            txtNamaPemilik.getText(),  
            kategoriPemilik  
        );  

        kendaraanList.add(kendaraanBaru);  
        saveKendaraanToDatabase(kendaraanBaru);  
        updateParkingSlot(kategoriPemilik, jenisKendaraan);  

        // Menampilkan konfirmasi  
        showAlert("Berhasil", "Kendaraan berhasil dimasukkan!", Alert.AlertType.INFORMATION);  

        // Kosongkan form setelah menambah kendaraan  
        txtNomorKendaraan.clear();  
        txtNamaPemilik.clear();  
        choiceJenisKendaraan.setValue(null);  
        choiceKategoriPemilik.setValue(null);  
    }  

    private void keluarkanKendaraan() {  
        // Logika untuk mengeluarkan kendaraan  
        String nomorKendaraanToRemove = txtNomorKendaraan.getText();  

        if (nomorKendaraanToRemove.isEmpty()) {  
            showAlert("Input Tidak Valid", "Silakan masukkan nomor kendaraan untuk dikeluarkan!", Alert.AlertType.WARNING);  
            return;  
        }  

        // Temukan kendaraan dalam list  
        boolean ditemukan = kendaraanList.removeIf(kendaraan -> kendaraan.getNomorKendaraan().equals(nomorKendaraanToRemove));  

        if (ditemukan) {  
            String jenisKendaraan = getJenisKendaraan(nomorKendaraanToRemove);  
            String kategoriPemilik = getKategoriPemilik(nomorKendaraanToRemove);  
            releaseParkingSlot(kategoriPemilik, jenisKendaraan);  
            deleteKendaraanFromDatabase(nomorKendaraanToRemove);  
            showAlert("Berhasil", "Kendaraan berhasil dikeluarkan!", Alert.AlertType.INFORMATION);  
        } else {  
            showAlert("Tidak Ditemukan", "Kendaraan tidak ditemukan untuk nomor: " + nomorKendaraanToRemove, Alert.AlertType.WARNING);  
        }  
    }  

    private void saveKendaraanToDatabase(Kendaraan kendaraan) {  
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {  
            String sql = "INSERT INTO kendaraan (nomor_kendaraan, jenis_kendaraan, nama_pemilik, kategori_pemilik, waktu_masuk) VALUES (?, ?, ?, ?, NOW())";  
            try (PreparedStatement statement = connection.prepareStatement(sql)) {  
                statement.setString(1, kendaraan.getNomorKendaraan());  
                statement.setString(2, kendaraan.getJenisKendaraan());  
                statement.setString(3, kendaraan.getNamaPemilik());  
                statement.setString(4, kendaraan.getKategoriPemilik());  
                statement.executeUpdate();  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  

    private void deleteKendaraanFromDatabase(String nomorKendaraan) {  
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {  
            String sql = "DELETE FROM kendaraan WHERE nomor_kendaraan = ?";  
            try (PreparedStatement statement = connection.prepareStatement(sql)) {  
                statement.setString(1, nomorKendaraan);  
                statement.executeUpdate();  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  

    private boolean isSlotAvailable(String jenisKendaraan, String kategoriPemilik) {  
        String area = getAreaByCategory(kategoriPemilik);  
        String sql = "SELECT kapasitas, terisi FROM slot_parkir WHERE area = ? AND jenis_kendaraan = ?";  
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);  
             PreparedStatement statement = connection.prepareStatement(sql)) {  
            statement.setString(1, area);  
            statement.setString(2, jenisKendaraan);  
            try (ResultSet resultSet = statement.executeQuery()) {  
                if (resultSet.next()) {  
                    int kapasitas = resultSet.getInt("kapasitas");  
                    int terisi = resultSet.getInt("terisi");  
                    return terisi < kapasitas;  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return false;  
    }  

    private String getAreaByCategory(String kategoriPemilik) {  
        switch (kategoriPemilik) {  
            case "mahasiswa":  
                return "mahasiswa";  
            case "dosen":  
                return "dosen";  
            case "tamu":  
                return "tamu";  
            default:  
                return "";  
        }  
    }  

    private void updateParkingSlot(String kategoriPemilik, String jenisKendaraan) {  
        String area = getAreaByCategory(kategoriPemilik);  
        String sql = "UPDATE slot_parkir SET terisi = terisi + 1 WHERE area = ? AND jenis_kendaraan = ?";  
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);  
             PreparedStatement statement = connection.prepareStatement(sql)) {  
            statement.setString(1, area);  
            statement.setString(2, jenisKendaraan);  
            statement.executeUpdate();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  

    private void releaseParkingSlot(String kategoriPemilik, String jenisKendaraan) {  
        String area = getAreaByCategory(kategoriPemilik);  
        String sql = "UPDATE slot_parkir SET terisi = terisi - 1 WHERE area = ? AND jenis_kendaraan = ?";  
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);  
             PreparedStatement statement = connection.prepareStatement(sql)) {  
            statement.setString(1, area);  
            statement.setString(2, jenisKendaraan);  
            statement.executeUpdate();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  

    private String getJenisKendaraan(String nomorKendaraan) {  
        String sql = "SELECT jenis_kendaraan FROM kendaraan WHERE nomor_kendaraan = ?";  
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);  
             PreparedStatement statement = connection.prepareStatement(sql)) {  
            statement.setString(1, nomorKendaraan);  
            try (ResultSet resultSet = statement.executeQuery()) {  
                if (resultSet.next()) {  
                    return resultSet.getString("jenis_kendaraan");  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  

    private String getKategoriPemilik(String nomorKendaraan) {  
        String sql = "SELECT kategori_pemilik FROM kendaraan WHERE nomor_kendaraan = ?";  
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);  
             PreparedStatement statement = connection.prepareStatement(sql)) {  
            statement.setString(1, nomorKendaraan);  
            try (ResultSet resultSet = statement.executeQuery()) {  
                if (resultSet.next()) {  
                    return resultSet.getString("kategori_pemilik");  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  

    private void showAlert(String title, String message, Alert.AlertType alertType) {  
        Alert alert = new Alert(alertType);  
        alert.setTitle(title);  
        alert.setHeaderText(null);  
        alert.setContentText(message);  
        alert.showAndWait();  
    }  
}