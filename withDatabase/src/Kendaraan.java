import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  

public abstract class Kendaraan {  
    private int id;  
    private String nomorKendaraan;  
    private String jenisKendaraan;  
    private String namaPemilik;  

    // IMPL
    public Kendaraan(int id, String nomorKendaraan, String jenisKendaraan, String namaPemilik) {  
        this.id = id;  
        this.nomorKendaraan = nomorKendaraan;  
        this.jenisKendaraan = jenisKendaraan;  
        this.namaPemilik = namaPemilik;  
    }  
    // SUBCLASS
    public Kendaraan(int id, String nomorKendaraan, String jenisKendaraan, String namaPemilik, String kategoriPemilik) {  
        this.id = id;  
        this.nomorKendaraan = nomorKendaraan;  
        this.jenisKendaraan = jenisKendaraan;  
        this.namaPemilik = namaPemilik; 
    }

    public String getNomorKendaraan() {  
        return nomorKendaraan;  
    }  

    public String getJenisKendaraan() {  
        return jenisKendaraan;  
    }  

    public String getNamaPemilik() {  
        return namaPemilik;  
    }  

    public abstract void simpanKeDatabase();  
    public abstract void hapusDariDatabase();  
    
}