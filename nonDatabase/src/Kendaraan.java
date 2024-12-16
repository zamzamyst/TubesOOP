// Kelas Kendaraan untuk menyimpan informasi kendaraan  
public class Kendaraan {  
    private String nomorKendaraan; // Nomor kendaraan  
    private String jenisKendaraan;  // Jenis kendaraan (mobil/motor)  
    private String namaPemilik;      // Nama pemilik kendaraan  
    private String kategoriPemilik;  // Kategori pemilik (Dosen/Mahasiswa/Tamu)  

    // Konstruktor untuk menginisialisasi atribut Kendaraan  
    public Kendaraan(String nomorKendaraan, String jenisKendaraan, String namaPemilik, String kategoriPemilik) {  
        this.nomorKendaraan = nomorKendaraan;  
        this.jenisKendaraan = jenisKendaraan;  
        this.namaPemilik = namaPemilik;  
        this.kategoriPemilik = kategoriPemilik;  
    }  

    // Getter untuk nomorKendaraan  
    public String getNomorKendaraan() {  
        return nomorKendaraan;  
    }  

    // Getter untuk jenisKendaraan  
    public String getJenisKendaraan() {  
        return jenisKendaraan;  
    }  

    // Getter untuk namaPemilik  
    public String getNamaPemilik() {  
        return namaPemilik;  
    }  

    // Getter untuk kategoriPemilik  
    public String getKategoriPemilik() {  
        return kategoriPemilik;  
    }  
}