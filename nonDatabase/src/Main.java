import java.util.Scanner;  

public class Main {  
    public static void main(String[] args) {  
        Scanner scanner = new Scanner(System.in); // Scanner untuk input dari pengguna  
        AlgoritmaParkir AlgoritmaParkir = new AlgoritmaParkir(); // Instance dari sistem pengelola parkir  
        int pilihan;  

        // Menu utama  
        do {  
            System.out.println("=== Menu Parkir Universitas ===");  
            System.out.println("1. Daftar Kendaraan");  
            System.out.println("2. Masuk Parkir");  
            System.out.println("3. Keluar Parkir");  
            System.out.println("4. Laporan Akhir");  
            System.out.println("0. Keluar");  
            System.out.print("Pilih aktivitas (0-4): ");  
            pilihan = scanner.nextInt(); // Ambil input dari pengguna  
            scanner.nextLine(); // Mengkonsumsi newline  

            switch (pilihan) {  
                case 1: // Pendaftaran kendaraan  
                    System.out.print("Nomor Kendaraan: ");  
                    String nomorKendaraan = scanner.nextLine();  
                    System.out.print("Jenis Kendaraan (mobil/motor): ");  
                    String jenisKendaraan = scanner.nextLine();  
                    System.out.print("Nama Pemilik: ");  
                    String namaPemilik = scanner.nextLine();  
                    System.out.print("Kategori Pemilik (Dosen/Mahasiswa/Tamu): ");  
                    String kategoriPemilik = scanner.nextLine();  
                    AlgoritmaParkir.pendaftaranKendaraan(nomorKendaraan, jenisKendaraan, namaPemilik, kategoriPemilik); // Panggil metode untuk pendaftaran kendaraan  
                    break;  

                case 2: // Masuk parkir  
                    System.out.print("Nomor Kendaraan untuk masuk: ");  
                    String nomorMasuk = scanner.nextLine();  
                    AlgoritmaParkir.kendaraanMasuk(nomorMasuk); // Panggil metode untuk masuk parkir  
                    break;  

                case 3: // Keluar parkir  
                    System.out.print("Nomor Kendaraan untuk keluar: ");  
                    String nomorKeluar = scanner.nextLine();  
                    System.out.print("Kategori Pemilik (Dosen/Mahasiswa/Tamu): ");  
                    String kategoriKeluar = scanner.nextLine();  
                    AlgoritmaParkir.kendaraanKeluar(nomorKeluar, kategoriKeluar); // Panggil metode untuk keluar parkir  
                    break;  

                case 4: // Laporan akhir  
                    System.out.println("Fitur laporan akhir belum diimplementasikan.");  
                    break;  

                case 0: // Keluar  
                    System.out.println("Program selesai."); // Menyelesaikan program  
                    break;  

                default:  
                    System.out.println("Pilihan tidak valid."); // Jika pilihan tidak ada  
            }  
        } while (pilihan != 0); // Terus menjalankan menu hingga pengguna memilih untuk keluar  

        scanner.close(); // Menutup scanner  
    }  
}