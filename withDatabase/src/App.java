import java.sql.*;  
import java.util.Scanner;  

public class App {  

    private static final String URL = "jdbc:mysql://localhost:3306/data_kendaraan"; // Ganti dengan URL MySQL Anda  
    private static final String USER = "root"; // Ganti dengan username MySQL Anda  
    private static final String PASSWORD = ""; // Ganti dengan password MySQL Anda  

    public static void main(String[] args) {  
        Scanner scanner = new Scanner(System.in);  
        while (true) {  
            System.out.println("\nMenu:");  
            System.out.println("1. Tambah Kendaraan");  
            System.out.println("2. Tampilkan Kendaraan");  
            System.out.println("3. Update Kendaraan");  
            System.out.println("4. Hapus Kendaraan");  
            System.out.println("5. Keluar");  
            System.out.print("Pilih opsi (1-5): ");  
            int option = scanner.nextInt();  
            scanner.nextLine(); // membersihkan newline character  

            switch (option) {  
                case 1:  
                    createKendaraan(scanner);  
                    break;  
                case 2:  
                    readKendaraan();  
                    break;  
                case 3:  
                    updateKendaraan(scanner);  
                    break;  
                case 4:  
                    deleteKendaraan(scanner);  
                    break;  
                case 5:  
                    System.out.println("Terima kasih! Program dihentikan.");  
                    return;  
                default:  
                    System.out.println("Opsi tidak valid. Silakan coba lagi.");  
            }  
        }  
    }  

    private static void createKendaraan(Scanner scanner) {  
        System.out.print("Masukkan Nomor Kendaraan: ");  
        String nomorKendaraan = scanner.nextLine();  
        
        System.out.print("Masukkan Jenis Kendaraan: ");  
        String jenisKendaraan = scanner.nextLine();  
        
        System.out.print("Masukkan Nama Pemilik: ");  
        String namaPemilik = scanner.nextLine();  
        
        System.out.print("Masukkan Kategori Pemilik: ");  
        String kategoriPemilik = scanner.nextLine();  

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {  
            String sql = "INSERT INTO data_kendaraan (nomor_kendaraan, jenis_kendaraan, nama_pemilik, kategori_pemilik) VALUES (?, ?, ?, ?)";  
            try (PreparedStatement statement = connection.prepareStatement(sql)) {  
                statement.setString(1, nomorKendaraan);  
                statement.setString(2, jenisKendaraan);  
                statement.setString(3, namaPemilik);  
                statement.setString(4, kategoriPemilik);  
                
                int rowsInserted = statement.executeUpdate();  
                if (rowsInserted > 0) {  
                    System.out.println("Data kendaraan berhasil disimpan!");  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  

    private static void readKendaraan() {  
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {  
            String sql = "SELECT * FROM data_kendaraan";  
            try (Statement statement = connection.createStatement();  
                 ResultSet resultSet = statement.executeQuery(sql)) {  
                System.out.println("\nDaftar Kendaraan:");  
                while (resultSet.next()) {  
                    String nomorKendaraan = resultSet.getString("nomor_kendaraan");  
                    String jenisKendaraan = resultSet.getString("jenis_kendaraan");  
                    String namaPemilik = resultSet.getString("nama_pemilik");  
                    String kategoriPemilik = resultSet.getString("kategori_pemilik");  
                    System.out.println(nomorKendaraan + ", " + jenisKendaraan + ", " + namaPemilik + ", " + kategoriPemilik);  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  

    private static void updateKendaraan(Scanner scanner) {  
        System.out.print("Masukkan Nomor Kendaraan yang ingin diupdate: ");  
        String nomorKendaraan = scanner.nextLine();  

        System.out.print("Masukkan Jenis Kendaraan Baru: ");  
        String jenisKendaraan = scanner.nextLine();  
        
        System.out.print("Masukkan Nama Pemilik Baru: ");  
        String namaPemilik = scanner.nextLine();  
        
        System.out.print("Masukkan Kategori Pemilik Baru: ");  
        String kategoriPemilik = scanner.nextLine();  

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {  
            String sql = "UPDATE data_kendaraan SET jenis_kendaraan = ?, nama_pemilik = ?, kategori_pemilik = ? WHERE nomor_kendaraan = ?";  
            try (PreparedStatement statement = connection.prepareStatement(sql)) {  
                statement.setString(1, jenisKendaraan);  
                statement.setString(2, namaPemilik);  
                statement.setString(3, kategoriPemilik);  
                statement.setString(4, nomorKendaraan);  
                
                int rowsUpdated = statement.executeUpdate();  
                if (rowsUpdated > 0) {  
                    System.out.println("Data kendaraan berhasil diupdate!");  
                } else {  
                    System.out.println("Data kendaraan tidak ditemukan.");  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  

    private static void deleteKendaraan(Scanner scanner) {  
        System.out.print("Masukkan Nomor Kendaraan yang ingin dihapus: ");  
        String nomorKendaraan = scanner.nextLine();  

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {  
            String sql = "DELETE FROM data_kendaraan WHERE nomor_kendaraan = ?";  
            try (PreparedStatement statement = connection.prepareStatement(sql)) {  
                statement.setString(1, nomorKendaraan);  
                
                int rowsDeleted = statement.executeUpdate();  
                if (rowsDeleted > 0) {  
                    System.out.println("Data kendaraan berhasil dihapus!");  
                } else {  
                    System.out.println("Data kendaraan tidak ditemukan.");  
                }  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
}
