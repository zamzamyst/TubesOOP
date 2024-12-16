// import library java.util secara keseluruhan (semua fungsi)
import java.util.*;  

public class SistemParkirTamu extends SistemParkir {  
    private ArrayList<Kendaraan> parkirTamu;  

    public SistemParkirTamu(int kapasitasKendaraan) {  
        super(kapasitasKendaraan);  
        this.parkirTamu = new ArrayList<>();  
    }  


    @Override  
    public void masukParkir(Kendaraan kendaraan) {  
        if (slotTersedia()) {
            parkirTamu.add(kendaraan);  
            addKendaraan(); // Tambah jumlah kendaraan di parkir  
            System.out.println("Kendaraan Tamu " + kendaraan.getNomorKendaraan() + " masuk parkir.");  
        } else {  
            System.out.println("Maaf, parkir tamu sudah penuh.");  
        }  
    }  

    @Override  
    public void keluarParkir(String nomorKendaraan) {  
        for (int i = 0; i < parkirTamu.size(); i++) {  
            if (parkirTamu.get(i).getNomorKendaraan().equals(nomorKendaraan)) {  
                parkirTamu.remove(i);  
                removeKendaraan(); // Mengurangi jumlah kendaraan di parkir  
                System.out.println("Kendaraan Tamu " + nomorKendaraan + " keluar parkir.");  
                return;  
            }  
        }  
        System.out.println("Kendaraan tidak ditemukan.");  
    }  
}