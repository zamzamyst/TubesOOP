// === Main.java ===
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AlgoritmaParkir algoritmaParkir = new AlgoritmaParkir();
        int pilihan;

        do {
            System.out.println("=== Menu Parkir Universitas ===");
            System.out.println("1. Daftar Kendaraan");
            System.out.println("2. Masuk Parkir");
            System.out.println("3. Keluar Parkir");
            System.out.println("4. Laporan Akhir");
            System.out.println("0. Keluar");
            System.out.print("Pilih aktivitas (0-4): ");
            pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {
                case 1:
                    System.out.print("Nomor Kendaraan: ");
                    String nomorKendaraan = scanner.nextLine();
                    System.out.print("Jenis Kendaraan (mobil/motor): ");
                    String jenisKendaraan = scanner.nextLine();
                    System.out.print("Nama Pemilik: ");
                    String namaPemilik = scanner.nextLine();
                    System.out.print("Kategori Pemilik (Dosen/Mahasiswa/Tamu): ");
                    String kategoriPemilik = scanner.nextLine();
                    algoritmaParkir.pendaftaranKendaraan(nomorKendaraan, jenisKendaraan, namaPemilik, kategoriPemilik);
                    break;
                case 2:
                    System.out.print("Nomor Kendaraan untuk masuk: ");
                    String nomorMasuk = scanner.nextLine();
                    algoritmaParkir.kendaraanMasuk(nomorMasuk);
                    break;
                case 3:
                    System.out.print("Nomor Kendaraan untuk keluar: ");
                    String nomorKeluar = scanner.nextLine();
                    System.out.print("Kategori Pemilik (Dosen/Mahasiswa/Tamu): ");
                    String kategoriKeluar = scanner.nextLine();
                    algoritmaParkir.kendaraanKeluar(nomorKeluar, kategoriKeluar);
                    break;
                case 4:
                    algoritmaParkir.laporanAkhir();
                    break;
                case 0:
                    System.out.println("Program selesai.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        } while (pilihan != 0);

        scanner.close();
    }
}
