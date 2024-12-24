
import java.io.IOException;

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

    // Set data laporan ke TextArea
    public void setReportData(String reportData) {
        reportTextArea.setText(reportData);
    }

    // Inisialisasi tombol kembali
    @FXML
    private void initialize() {
        btnBack.setOnAction(event -> goBackToMainPage());
    }

    // Navigasi kembali ke halaman utama
    private void goBackToMainPage() {
        try {  
            Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));  
            Stage stage = (Stage) btnBack.getScene().getWindow();  
            stage.setScene(new Scene(root));  
            stage.show();  

        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
}
