public class Kendaraan {
    private int id; // Tambahkan ID
    private String nomorKendaraan;
    private String jenisKendaraan;
    private String namaPemilik;
    private String kategoriPemilik;

    // Constructor dengan ID
    public Kendaraan(int id, String nomorKendaraan, String jenisKendaraan, String namaPemilik, String kategoriPemilik) {
        this.id = id;
        this.nomorKendaraan = nomorKendaraan;
        this.jenisKendaraan = jenisKendaraan;
        this.namaPemilik = namaPemilik;
        this.kategoriPemilik = kategoriPemilik;
    }

    // Getter dan Setter untuk ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter dan Setter lainnya
    public String getNomorKendaraan() {
        return nomorKendaraan;
    }

    public void setNomorKendaraan(String nomorKendaraan) {
        this.nomorKendaraan = nomorKendaraan;
    }

    public String getJenisKendaraan() {
        return jenisKendaraan;
    }

    public void setJenisKendaraan(String jenisKendaraan) {
        this.jenisKendaraan = jenisKendaraan;
    }

    public String getNamaPemilik() {
        return namaPemilik;
    }

    public void setNamaPemilik(String namaPemilik) {
        this.namaPemilik = namaPemilik;
    }

    public String getKategoriPemilik() {
        return kategoriPemilik;
    }

    public void setKategoriPemilik(String kategoriPemilik) {
        this.kategoriPemilik = kategoriPemilik;
    }
}
