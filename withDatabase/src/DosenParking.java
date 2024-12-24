import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;  

public class DosenParking extends Kendaraan {  
    private static final int KAPASITAS_MOTOR = 5;   
    private static final int KAPASITAS_MOBIL = 5;   
    private static int jumlahMotor = 0;   
    private static int jumlahMobil = 0;   

    // Set DB
    static {  
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_kendaraan", "root", "")) {  
            String sqlMotor = "SELECT COUNT(*) FROM dosen_kendaraan WHERE jenis_kendaraan = 'Motor'";  
            String sqlMobil = "SELECT COUNT(*) FROM dosen_kendaraan WHERE jenis_kendaraan = 'Mobil'";  
            try (PreparedStatement statementMotor = connection.prepareStatement(sqlMotor);  
                 PreparedStatement statementMobil = connection.prepareStatement(sqlMobil);  
                 ResultSet resultSetMotor = statementMotor.executeQuery();  
                 ResultSet resultSetMobil = statementMobil.executeQuery()) {  
                if (resultSetMotor.next()) {  
                    jumlahMotor = resultSetMotor.getInt(1);   
                }  
                if (resultSetMobil.next()) {  
                    jumlahMobil = resultSetMobil.getInt(1);  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  

    // Constructor
    public DosenParking(int id, String nomorKendaraan, String jenisKendaraan, String namaPemilik) {  
        super(id, nomorKendaraan, jenisKendaraan, namaPemilik, "Dosen");  
    }  

    public static boolean isMotorSlotAvailable() {  
        return jumlahMotor < KAPASITAS_MOTOR;   
    }  

    public static boolean isMobilSlotAvailable() {  
        return jumlahMobil < KAPASITAS_MOBIL;   
    }  

    // metode simpan
    @Override
    public void simpanKeDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_kendaraan", "root", "")) {
            String sql = "INSERT INTO dosen_kendaraan (nomor_kendaraan, nama_pemilik, jenis_kendaraan) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, getNomorKendaraan());
                statement.setString(2, getNamaPemilik());
                statement.setString(3, getJenisKendaraan());
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Kendaraan berhasil masuk parkir.");
                    
                    // Update jumlah kendaraan setelah masuk parkir
                    if (getJenisKendaraan().equals("Motor")) {
                        jumlahMotor++;  // Menambah jumlah motor
                    } else if (getJenisKendaraan().equals("Mobil")) {
                        jumlahMobil++;  // Menambah jumlah mobil
                    }
                } else {
                    System.out.println("Gagal menyimpan data kendaraan.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void hapusDariDatabase() {
    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_kendaraan", "root", "")) {
        // Cek apakah data ada di tabel dosen_kendaraan
        String checkSql = "SELECT COUNT(*) AS total FROM dosen_kendaraan WHERE nomor_kendaraan = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
            checkStatement.setString(1, getNomorKendaraan());
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt("total") == 0) {
                    throw new Exception("Nomor kendaraan tidak ditemukan di database dosen.");
                }
            }
        }

        // Lanjutkan penghapusan jika data ada
        String deleteSql = "DELETE FROM dosen_kendaraan WHERE nomor_kendaraan = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setString(1, getNomorKendaraan());
            int rowsDeleted = deleteStatement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Data kendaraan berhasil dihapus.");
                
                // Update jumlah kendaraan setelah kendaraan keluar parkir
                if (getJenisKendaraan().equals("Motor")) {
                    jumlahMotor--;  // Mengurangi jumlah motor
                } else if (getJenisKendaraan().equals("Mobil")) {
                    jumlahMobil--;  // Mengurangi jumlah mobil
                }
            } else {
                throw new Exception("Kendaraan tidak ditemukan.");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e.getMessage());
    }
}

    public void cekSlotParkir() {
        String message = "";
    
        if (getJenisKendaraan().equals("Motor")) {
            // Mengecek ketersediaan slot untuk motor
            if (isMotorSlotAvailable()) {
                message = "Kapasitas Motor: " + KAPASITAS_MOTOR + "\nSlot Parkir Terisi: " + jumlahMotor + "\nSlot Parkir Sisa: " + (KAPASITAS_MOTOR - jumlahMotor);
            } else {
                message = "Kapasitas Motor telah penuh.";
            }
        } else if (getJenisKendaraan().equals("Mobil")) {
            // Mengecek ketersediaan slot untuk mobil
            if (isMobilSlotAvailable()) {
                message = "Kapasitas Mobil: " + KAPASITAS_MOBIL + "\nSlot Parkir Terisi: " + jumlahMobil + "\nSlot Parkir Sisa: " + (KAPASITAS_MOBIL - jumlahMobil);
            } else {
                message = "Kapasitas Mobil telah penuh.";
            }
        }
    
        // Menampilkan pesan dengan alert
        showAlert("Informasi Parkir", message);
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);  // Opsional, jika ingin header kosong
        alert.setContentText(message);
        alert.showAndWait();
    }

}
