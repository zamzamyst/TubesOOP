// Kelas Controller untuk Menampilkan Laporan Harian

/* ======================================================== */

// Meng-Import Packages JavaFX (Built-in)
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;

public class ReportController {
    @FXML
    private TextArea reportTextArea;

    @FXML
    private Button btnBack;

    // Method untuk memasukkan data report ke TextArea GUI
    public void setReportData(String reportData) {
        reportTextArea.setText(reportData);
    }

    @FXML
    private void initialize() {

        // Menyetel aksi klik button
        btnBack.setOnAction(event -> backToRegistrationPage());
    }

    // Method Void: Untuk kembali ke Registration Page
    private void backToRegistrationPage() {
        try {  
            Parent root = FXMLLoader.load(getClass().getResource("RegistrationPage.fxml"));  
            Stage stage = (Stage) btnBack.getScene().getWindow();  
            stage.setScene(new Scene(root));  
            stage.show();  

        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
}
