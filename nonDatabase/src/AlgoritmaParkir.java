// Memnghubungkan semua subclass dengan kelas ini 
// Tipe Data berupa Variabel karena untuk merepresentasikan bukan menyimpan data)

import java.util.ArrayList;  

public class AlgoritmaParkir {  
    private SistemParkirMahasiswa sistemParkirMahasiswa;   
    private SistemParkirDosen sistemParkirDosen;   
    private SistemParkirTamu sistemParkirTamu;   
    private ArrayList<Kendaraan> kendaraanTerdaftar;  

    public AlgoritmaParkir() {  
        sistemParkirMahasiswa = new SistemParkirMahasiswa(2); // Kontrol kapasitas  
        sistemParkirDosen = new SistemParkirDosen(2);  
        sistemParkirTamu = new SistemParkirTamu(2);  
        kendaraanTerdaftar = new ArrayList<>(); // inisialisasi list kendaraan terdaftar  
    }  

    // Metode untuk mendaftarkan kendaraan  
    public void pendaftaranKendaraan(String nomorKendaraan, String jenisKendaraan, String namaPemilik, String kategoriPemilik) {  
        Kendaraan kendaraan = new Kendaraan(nomorKendaraan, jenisKendaraan, namaPemilik, kategoriPemilik);  
        kendaraanTerdaftar.add(kendaraan);  
        System.out.println("Kendaraan " + nomorKendaraan + " berhasil didaftarkan.");  
    }  

    public void kendaraanMasuk(String nomorKendaraan) {  
        for (Kendaraan kendaraan : kendaraanTerdaftar) {  
            // Cek apakah kendaraan terdaftar  
            if (kendaraan.getNomorKendaraan().equalsIgnoreCase(nomorKendaraan)) {
                // Catat masuk ke parkir  
                switch (kendaraan.getKategoriPemilik().toLowerCase()) {  
                    case "mahasiswa":  
                        sistemParkirMahasiswa.masukParkir(kendaraan);  
                        break;  
                    case "dosen":  
                        sistemParkirDosen.masukParkir(kendaraan);  
                        break;  
                    case "tamu":  
                        sistemParkirTamu.masukParkir(kendaraan);  
                        break;  
                }  
                return;  
                
            }  
        }  
        System.out.println("Kendaraan dengan nomor " + nomorKendaraan + " tidak terdaftar.");
    }
    
    // Metode untuk mencatat kendaraan keluar (butuh hasil input dari menu Keluar Parkir pada Main.java)
    public void kendaraanKeluar(String nomorKendaraan, String kategoriPemilik) {  
    switch (kategoriPemilik.toLowerCase()) {  
        case "mahasiswa":  
            sistemParkirMahasiswa.keluarParkir(nomorKendaraan);  
            break;  
        case "dosen":  
            sistemParkirDosen.keluarParkir(nomorKendaraan);  
            break;  
        case "tamu":  
            sistemParkirTamu.keluarParkir(nomorKendaraan);  
            break;  
        default:  
            System.out.println("Kategori pemilik tidak valid.");  
        }  
    }
}
