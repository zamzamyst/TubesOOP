import java.sql.*;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input data kendaraan
        System.out.print("Masukkan Nomor Kendaraan: ");
        String nomorKendaraan = scanner.nextLine();
        
        System.out.print("Masukkan Jenis Kendaraan: ");
        String jenisKendaraan = scanner.nextLine();
        
        System.out.print("Masukkan Nama Pemilik: ");
        String namaPemilik = scanner.nextLine();
        
        System.out.print("Masukkan Kategori Pemilik: ");
        String kategoriPemilik = scanner.nextLine();

        // Koneksi ke MySQL Database
        String url = "jdbc:mysql://localhost:3306/data_kendaraan"; // Ganti dengan URL MySQL kamu
        String user = "root"; // Ganti dengan username MySQL kamu
        String password = ""; // Ganti dengan password MySQL kamu
        
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Mencoba koneksi ke database
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Koneksi ke database berhasil!");

            // Query untuk insert data
            String sql = "INSERT INTO data_kendaraan (nomor_kendaraan, jenis_kendaraan, nama_pemilik, kategori_pemilik) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            
            // Mengatur parameter query
            statement.setString(1, nomorKendaraan);
            statement.setString(2, jenisKendaraan);
            statement.setString(3, namaPemilik);
            statement.setString(4, kategoriPemilik);

            // Menjalankan query
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data kendaraan berhasil disimpan!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Menutup koneksi dan statement
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}