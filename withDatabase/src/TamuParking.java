import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;
import java.sql.SQLException;  

public class TamuParking extends Kendaraan {  
    private static final int KAPASITAS_MOTOR = 5;  
    private static final int KAPASITAS_MOBIL = 5;   
    private static int jumlahMotor = 0;   
    private static int jumlahMobil = 0;   

    static {  
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_kendaraan", "root", "")) {  
            String sqlMotor = "SELECT COUNT(*) FROM tamu_kendaraan WHERE jenis_kendaraan = 'Motor'";  
            String sqlMobil = "SELECT COUNT(*) FROM tamu_kendaraan WHERE jenis_kendaraan = 'Mobil'";  
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

    public TamuParking(int id, String nomorKendaraan, String jenisKendaraan, String namaPemilik) {  
        super(id, nomorKendaraan, jenisKendaraan, namaPemilik, "Tamu");  
    }  

    public static boolean isMotorSlotAvailable() {  
        return jumlahMotor < KAPASITAS_MOTOR;   
    }  

    public static boolean isMobilSlotAvailable() {  
        return jumlahMobil < KAPASITAS_MOBIL;   
    }  

    @Override  
    public void simpanKeDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_kendaraan", "root", "")) {
            String sql = "INSERT INTO tamu_kendaraan (nomor_kendaraan, nama_pemilik, jenis_kendaraan) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, getNomorKendaraan());
                statement.setString(2, getNamaPemilik());
                statement.setString(3, getJenisKendaraan());
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Kendaraan berhasil masuk parkir.");
                } else {
                    System.out.println("Gagal menyimpan data kendaraan.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void hapusDariDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_kendaraan", "root", "")) {
            // Cek apakah data ada di tabel tamu_kendaraan
            String checkSql = "SELECT COUNT(*) AS total FROM tamu_kendaraan WHERE nomor_kendaraan = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
                checkStatement.setString(1, getNomorKendaraan());
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt("total") == 0) {
                        throw new Exception("Nomor kendaraan tidak ditemukan di database tamu.");
                    }
                }
            }

            // Lanjutkan penghapusan jika data ada
            String deleteSql = "DELETE FROM tamu_kendaraan WHERE nomor_kendaraan = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
                deleteStatement.setString(1, getNomorKendaraan());
                int rowsDeleted = deleteStatement.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Data kendaraan berhasil dihapus.");
                } else {
                    throw new Exception("Kendaraan tidak ditemukan.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}