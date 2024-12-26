// Kelas Controller untuk Registrasi

/* ======================================================== */

// Meng-Import Packages SQL Java (Built-in)
import java.sql.*;

// Meng-Import Packages JavaFX (Built-in)
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;  
import javafx.scene.control.TextArea;  
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class RegistrationController {  
    @FXML  
    private TextField txtNomorKendaraan;  
    @FXML  
    private TextField txtNamaPemilik;  
    @FXML  
    private ChoiceBox<String> choiceJenisKendaraan;
    @FXML
    private ChoiceBox<String> choiceKategoriPemilik;
    @FXML  
    private Button btnSimpan;  
    @FXML  
    private Button btnTampilkan;  
    @FXML  
    private Button btnHapus;  
    @FXML  
    private Button btnSimulasi;  
    @FXML
    private Button btnLaporanHarian;

    @FXML  
    private TextArea textArea;  
    
    @FXML
    private TableView<Kendaraan> tableView;

    @FXML
    private TableColumn<Kendaraan, Integer> columnId;
    @FXML
    private TableColumn<Kendaraan, String> columnNomorKendaraan;
    @FXML
    private TableColumn<Kendaraan, String> columnJenisKendaraan;
    @FXML
    private TableColumn<Kendaraan, String> columnNamaPemilik;
    @FXML
    private TableColumn<Kendaraan, String> columnKategoriPemilik;

    // ObservableList untuk mengambil data, disimpan di ArrayList
    private ObservableList<Kendaraan> kendaraanList = FXCollections.observableArrayList();

// -------------------------------------------------------------------------------------------//
    @FXML
    private void initialize() {

        // Menyiapkan kolom untuk ambil data dari masing masing variabel:
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNomorKendaraan.setCellValueFactory(new PropertyValueFactory<>("nomorKendaraan"));
        columnJenisKendaraan.setCellValueFactory(new PropertyValueFactory<>("jenisKendaraan"));
        columnNamaPemilik.setCellValueFactory(new PropertyValueFactory<>("namaPemilik"));
        columnKategoriPemilik.setCellValueFactory(new PropertyValueFactory<>("kategoriPemilik"));

        // Menampilkan data yang sebelumnya di simpan di ArrayList
        tableView.setItems(kendaraanList);

        // Menyetel opsi untuk ChoiceBox
        choiceJenisKendaraan.getItems().addAll("Mobil", "Motor");
        choiceKategoriPemilik.getItems().addAll("Mahasiswa", "Dosen", "Tamu");

        // Menyetel action untuk setiap Button
        btnSimpan.setOnAction(event -> saveKendaraan());
        btnTampilkan.setOnAction(event -> displayKendaraan());
        btnHapus.setOnAction(event -> deleteKendaraan());
        btnSimulasi.setOnAction(event -> openSimulasiPage());
        btnLaporanHarian.setOnAction(event -> generateDailyReport());  
    }

    // Mendeklarasikan variabel untuk kebutuhan koneksi DB
    private final String URL = "jdbc:mysql://localhost:3306/data_kendaraan";  
    private final String USER = "root";   
    private final String PASSWORD = ""; 
    
    // Method Void 1
    private void saveKendaraan() {

        // Pengecekan apabila Jenis Kendaraan belum dipilih.
        if (choiceJenisKendaraan.getValue() == null || choiceJenisKendaraan.getValue().isEmpty()) {
            showAlert("Peringatan", "Silakan pilih jenis kendaraan terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }

        // Pengecekan apabila Kategori Pemilik belum dipilih.
        if (choiceKategoriPemilik.getValue() == null || choiceKategoriPemilik.getValue().isEmpty()) {
            showAlert("Peringatan", "Silakan pilih kategori pemilik terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }

        // Pengecekan untuk mencegah duplikasi penyimpanan data
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String checkSql = "SELECT COUNT(*) FROM data_kendaraan WHERE nomor_kendaraan = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
                checkStatement.setString(1, txtNomorKendaraan.getText());

                ResultSet resultSet = checkStatement.executeQuery();
                if (resultSet.next() && resultSet.getInt(1) > 0) {

                    // Jika kendaraan sudah ada
                    showAlert("Informasi", "Kendaraan dengan nomor " + txtNomorKendaraan.getText() + " sudah terdaftar.", Alert.AlertType.WARNING);
                    return;
                }

            // Jika kendaraan belum ada, simpan data
            String sql = "INSERT INTO data_kendaraan (nomor_kendaraan, jenis_kendaraan, nama_pemilik, kategori_pemilik) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, txtNomorKendaraan.getText());
                statement.setString(2, choiceJenisKendaraan.getValue());
                statement.setString(3, txtNamaPemilik.getText());
                statement.setString(4, choiceKategoriPemilik.getValue());

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {

                    // Jika Data berhasil disimpan
                    showAlert("Informasi", "Data kendaraan berhasil disimpan ke database.", Alert.AlertType.INFORMATION);
                    displayKendaraan();

                    // Bersihkan form input setelah berhasil menyimpan
                    txtNomorKendaraan.clear();
                    txtNamaPemilik.clear();
                    choiceJenisKendaraan.setValue("");
                    choiceKategoriPemilik.setValue("");
                    }
                }
            }

        // Alert jika ada kesalahan pada MySQL (seperti belum ON)
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal menyimpan data kendaraan", Alert.AlertType.ERROR);
        }
    }

    //  Methofd Void 2
    private void displayKendaraan() {
        
        // Kosongkan data sebelum menampilkan yang baru
        kendaraanList.clear(); 

        // Koneksi DB
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM data_kendaraan";
            try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
    
                while (resultSet.next()) {
                    // Buat objek Kendaraan dari setiap baris di ResultSet
                    Kendaraan kendaraan = new KendaraanRegistered(

                            // Ambil seluruh data untuk mengisi parameter constructor
                            resultSet.getInt("id"), 
                            resultSet.getString("nomor_kendaraan"),
                            resultSet.getString("jenis_kendaraan"),
                            resultSet.getString("nama_pemilik"),
                            resultSet.getString("kategori_pemilik")
                    );
                    //  Maukkan ke Observe ArrayList untuk tabel tadi
                    kendaraanList.add(kendaraan);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // method Void 3
    private void deleteKendaraan() {  

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {  
            String sql = "DELETE FROM data_kendaraan WHERE nomor_kendaraan = ?";  
            try (PreparedStatement statement = connection.prepareStatement(sql)) {  
                statement.setString(1, txtNomorKendaraan.getText());  

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

        // Menampilkan data setelah dihapus
        displayKendaraan();
    }  

    // Method Void 4:
    private void openSimulasiPage() {  
        try {  
            Parent root = FXMLLoader.load(getClass().getResource("SimulasiPage.fxml"));  
            Stage stage = (Stage) btnSimulasi.getScene().getWindow();  
            stage.setScene(new Scene(root));  
            stage.show();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }

    // Method Void 5:
    private void generateDailyReport() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            // Query untuk menampilkan laporan harian
            String sql = "SELECT 'Mahasiswa' AS kategori_pemilik, " +
                        // Menghitung jumlah kendaraan berdasarkan jenisnya
                        "SUM(CASE WHEN jenis_kendaraan = 'Mobil' THEN 1 ELSE 0 END) AS total_mobil_masuk, " +
                        "SUM(CASE WHEN jenis_kendaraan = 'Motor' THEN 1 ELSE 0 END) AS total_motor_masuk, " +

                        // Menghitung total kendaraan yang terparkir pada hari tersebut
                        "COUNT(*) AS total_terparkir, " +
                        "(SELECT COUNT(*) FROM mahasiswa_kendaraan WHERE DATE(waktu_masuk) = CURDATE()) - COUNT(*) AS total_mobil_keluar, " +
                        "(SELECT COUNT(*) FROM mahasiswa_kendaraan WHERE DATE(waktu_masuk) = CURDATE()) - COUNT(*) AS total_motor_keluar " +
                        "FROM mahasiswa_kendaraan WHERE DATE(waktu_masuk) = CURDATE() " +

                        // Menggabungkan hasil dari tabel Mahasiswa
                        "UNION ALL " +
                        
                        "SELECT 'Dosen', " +
                        // Menghitung jumlah kendaraan berdasarkan jenisnya
                        "SUM(CASE WHEN jenis_kendaraan = 'Mobil' THEN 1 ELSE 0 END), " +
                        "SUM(CASE WHEN jenis_kendaraan = 'Motor' THEN 1 ELSE 0 END), " +

                        // Menghitung total kendaraan yang terparkir pada hari tersebut
                        "COUNT(*), " +
                        "(SELECT COUNT(*) FROM dosen_kendaraan WHERE DATE(waktu_masuk) = CURDATE()) - COUNT(*), " +
                        "(SELECT COUNT(*) FROM dosen_kendaraan WHERE DATE(waktu_masuk) = CURDATE()) - COUNT(*) " +
                        "FROM dosen_kendaraan WHERE DATE(waktu_masuk) = CURDATE() " +

                        // Menggabungkan hasil dari tabel Dosen
                        "UNION ALL " +

                        "SELECT 'Tamu', " +
                        // Menghitung jumlah kendaraan berdasarkan jenisnya
                        "SUM(CASE WHEN jenis_kendaraan = 'Mobil' THEN 1 ELSE 0 END), " +
                        "SUM(CASE WHEN jenis_kendaraan = 'Motor' THEN 1 ELSE 0 END), " +

                        // Menghitung total kendaraan yang terparkir pada hari tersebut
                        "COUNT(*), " +
                        "(SELECT COUNT(*) FROM tamu_kendaraan WHERE DATE(waktu_masuk) = CURDATE()) - COUNT(*), " +
                        "(SELECT COUNT(*) FROM tamu_kendaraan WHERE DATE(waktu_masuk) = CURDATE()) - COUNT(*) " +
                        "FROM tamu_kendaraan WHERE DATE(waktu_masuk) = CURDATE()";


            StringBuilder reportBuilder = new StringBuilder();
            try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

                // Membaca tabel temporary yang terbuat dari Query SQL
                while (resultSet.next()) {
                    String kategori = resultSet.getString("kategori_pemilik");
                    int totalMobilMasuk = resultSet.getInt("total_mobil_masuk");
                    int totalMotorMasuk = resultSet.getInt("total_motor_masuk");
                    int totalMobilKeluar = resultSet.getInt("total_mobil_keluar");
                    int totalMotorKeluar = resultSet.getInt("total_motor_keluar");
                    int totalTerparkir = resultSet.getInt("total_terparkir");
    
                    // Mengubah format datanya menjadi laporan pakai ReportBuilder
                    reportBuilder.append(kategori).append(":\n")
                                .append("- Total Mobil Masuk: ").append(totalMobilMasuk).append("\n")
                                .append("- Total Motor Masuk: ").append(totalMotorMasuk).append("\n")
                                .append("- Total Mobil Keluar: ").append(totalMobilKeluar).append("\n")
                                .append("- Total Motor Keluar: ").append(totalMotorKeluar).append("\n")
                                .append("- Total yang masih Terparkir: ").append(totalTerparkir).append("\n\n");
                }
            }
    
            // Membuka Halaman Laporan Harian
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportPage.fxml"));
            Parent root = loader.load();
    
            // Mengirimkan data yang disususn di ReportBuilder ke Controller
            ReportController controller = loader.getController();
            controller.setReportData(reportBuilder.toString());
    
            Stage stage = new Stage();
            stage.setTitle("Laporan Harian");
            stage.setScene(new Scene(root));
            stage.show();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    // Method Void 6:
    private void showAlert(String title, String message, Alert.AlertType alertType) {  
        Alert alert = new Alert(alertType);  
        alert.setTitle(title);  
        alert.setHeaderText(null);  
        alert.setContentText(message);  
        alert.showAndWait();  
    }  

}    





/*
import javafx.scene.control.TableCell;
import javafx.scene.text.TextAlignment;
/*

 * // Metode untuk mengatur alignment teks dalam sel
private <T> void setCenterAlignment(TableColumn<Kendaraan, T> column) {
    column.setCellFactory(tc -> {
        TableCell<Kendaraan, T> cell = new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        };
        cell.setStyle("-fx-alignment: LEFT;"); // CSS untuk menempatkan teks di tengah
        return cell;
    });
}
 */

/*     
    // Set alignment ke tengah untuk setiap kolom
    setCenterAlignment(columnId);
    setCenterAlignment(columnNomorKendaraan);
    setCenterAlignment(columnJenisKendaraan);
    setCenterAlignment(columnNamaPemilik);
    setCenterAlignment(columnKategoriPemilik);
*/