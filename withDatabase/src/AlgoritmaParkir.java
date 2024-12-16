// === AlgoritmaParkir.java ===
public class AlgoritmaParkir {
    private final KendaraanDAO dao = new KendaraanDAO();

    public void pendaftaranKendaraan(String nomorKendaraan, String jenisKendaraan, String namaPemilik, String kategoriPemilik) {
        Kendaraan kendaraan = new Kendaraan(nomorKendaraan, jenisKendaraan, namaPemilik, kategoriPemilik);
        dao.pendaftaranKendaraan(kendaraan);
    }

    public void kendaraanMasuk(String nomorKendaraan) {
        dao.kendaraanMasuk(nomorKendaraan);
    }

    public void kendaraanKeluar(String nomorKendaraan, String kategoriPemilik) {
        dao.kendaraanKeluar(nomorKendaraan, kategoriPemilik);
    }

    public void laporanAkhir() {
        dao.laporanAkhir();
    }
}
