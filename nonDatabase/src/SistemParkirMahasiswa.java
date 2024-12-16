import java.util.ArrayList;  

public class SistemParkirMahasiswa extends SistemParkir {  
    private ArrayList<Kendaraan> parkirMahasiswa;  

    public SistemParkirMahasiswa(int kapasitasKendaraan) {  
        super(kapasitasKendaraan);  
        this.parkirMahasiswa = new ArrayList<>();  
    } 
    
    @Override  
    public void masukParkir(Kendaraan kendaraan) {  
            parkirMahasiswa.add(kendaraan); // Tambahkan kendaraan ke daftar parkir  
            addKendaraan(); // Tambah jumlah kendaraan di parkir  
            System.out.println("Kendaraan Mahasiswa " + kendaraan.getNomorKendaraan() + " masuk parkir.");  
    
    }
    
    @Override  
    public void keluarParkir(String nomorKendaraan) {  
        for (int i = 0; i < parkirMahasiswa.size(); i++) {  
            if (parkirMahasiswa.get(i).getNomorKendaraan().equals(nomorKendaraan)) {  
                parkirMahasiswa.remove(i);  
                removeKendaraan(); // Mengurangi jumlah kendaraan di parkir  
                System.out.println("Kendaraan Mahasiswa " + nomorKendaraan + " keluar parkir.");  
                return;  
            }  
        }
    }
}
    

