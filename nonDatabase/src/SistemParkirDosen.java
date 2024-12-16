import java.util.*;

public class SistemParkirDosen extends SistemParkir {  
    private ArrayList<Kendaraan> parkirDosen;  

    public SistemParkirDosen(int kapasitasKendaraan) {  
        super(kapasitasKendaraan);  
        this.parkirDosen = new ArrayList<>();  
    }  

    @Override  
    public void masukParkir(Kendaraan kendaraan) {   
        parkirDosen.add(kendaraan);  
        addKendaraan(); // Tambah jumlah kendaraan di parkir  
        System.out.println("Kendaraan Dosen " + kendaraan.getNomorKendaraan() + " masuk parkir.");  
    
        }  

    @Override  
    public void keluarParkir(String nomorKendaraan) {  
        for (int i = 0; i < parkirDosen.size(); i++) {  
            if (parkirDosen.get(i).getNomorKendaraan().equals(nomorKendaraan)) {  
                parkirDosen.remove(i);  
                removeKendaraan(); // Mengurangi jumlah kendaraan di parkir  
                System.out.println("Kendaraan Dosen " + nomorKendaraan + " keluar parkir.");  
                return;  
            }  
        }  
    }
}
 