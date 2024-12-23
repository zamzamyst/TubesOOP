import javafx.application.Application;  
import javafx.fxml.FXMLLoader;  
import javafx.scene.Parent;  
import javafx.scene.Scene;  
import javafx.stage.Stage;  

public class App extends Application {  
    @Override  
    public void start(Stage primaryStage) throws Exception {  
        Parent root = FXMLLoader.load(getClass().getResource("SimulasiPage.fxml"));  
        primaryStage.setTitle("Kelompok 3 - Tugas Besar OOP");  
        primaryStage.setScene(new Scene(root));  
        primaryStage.show();  
    }  

    public static void main(String[] args) {  
        launch(args);  
    }  
}