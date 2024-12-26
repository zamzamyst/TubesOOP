// SubClass Kendaraan

/* ======================================================== */

public abstract class Kendaraan {  
    private int id;  
    private String nomorKendaraan;  
    private String jenisKendaraan;  
    private String namaPemilik;  

    // Constructors 1: Untuk dipakai di SubClass KendaraanRegistered
    public Kendaraan(int id, String nomorKendaraan, String jenisKendaraan, String namaPemilik) {  
        this.id = id;  
        this.nomorKendaraan = nomorKendaraan;  
        this.jenisKendaraan = jenisKendaraan;  
        this.namaPemilik = namaPemilik;  
    }  

    // Constructors 2: Untuk dipakai di SubClass KendaraanMahasiswa, Dosen, dan Tamu
    public Kendaraan(int id, String nomorKendaraan, String jenisKendaraan, String namaPemilik, String kategoriPemilik) {  
        this.id = id;  
        this.nomorKendaraan = nomorKendaraan;  
        this.jenisKendaraan = jenisKendaraan;  
        this.namaPemilik = namaPemilik; 
    }

    // Getter untuk mengembalikan nilai Nomor Kendaraandi kelas lain
    public String getNomorKendaraan() {  
        return nomorKendaraan;  
    }  

    // Getter untuk mengembalikan nilai Jenis Kendaraan kelas lain
    public String getJenisKendaraan() {  
        return jenisKendaraan;  
    }  

    // Getter untuk mengembalikan nilai Nomor Kendaraan kelas lain
    public String getNamaPemilik() {  
        return namaPemilik;  
    }  

    // Method Abstract 1: Untuk menyimpan ke dalam DB berdasarkan cara dari masing-masing SubClass
    public abstract void simpanKeDatabase();  
    
    // Method Abstract 2: Untuk menyimpan ke dalam DB berdasarkan cara dari masing-masing SubClass
    public abstract void hapusDariDatabase();  
    
}