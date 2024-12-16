// === Kendaraan.java ===
import java.sql.Timestamp;

public class Kendaraan {
    private int id;
    private String nomorKendaraan;
    private String jenisKendaraan;
    private String namaPemilik;
    private String kategoriPemilik;
    private Timestamp waktuMasuk;
    private Timestamp waktuKeluar;

    // Constructor, Getter, dan Setter
    public Kendaraan() {}

    public Kendaraan(String nomorKendaraan, String jenisKendaraan, String namaPemilik, String kategoriPemilik) {
        this.nomorKendaraan = nomorKendaraan;
        this.jenisKendaraan = jenisKendaraan;
        this.namaPemilik = namaPemilik;
        this.kategoriPemilik = kategoriPemilik;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomorKendaraan() { return nomorKendaraan; }
    public void setNomorKendaraan(String nomorKendaraan) { this.nomorKendaraan = nomorKendaraan; }
    public String getJenisKendaraan() { return jenisKendaraan; }
    public void setJenisKendaraan(String jenisKendaraan) { this.jenisKendaraan = jenisKendaraan; }
    public String getNamaPemilik() { return namaPemilik; }
    public void setNamaPemilik(String namaPemilik) { this.namaPemilik = namaPemilik; }
    public String getKategoriPemilik() { return kategoriPemilik; }
    public void setKategoriPemilik(String kategoriPemilik) { this.kategoriPemilik = kategoriPemilik; }
    public Timestamp getWaktuMasuk() { return waktuMasuk; }
    public void setWaktuMasuk(Timestamp waktuMasuk) { this.waktuMasuk = waktuMasuk; }
    public Timestamp getWaktuKeluar() { return waktuKeluar; }
    public void setWaktuKeluar(Timestamp waktuKeluar) { this.waktuKeluar = waktuKeluar; }
}

