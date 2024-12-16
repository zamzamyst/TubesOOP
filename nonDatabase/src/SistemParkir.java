public abstract class SistemParkir {  
    protected int kapasitasKendaraan;  
    protected int jumlahKendaraan;  

    public SistemParkir(int kapasitasKendaraan) {  
        this.kapasitasKendaraan = kapasitasKendaraan;  
        this.jumlahKendaraan = 0;  
    }  

    public void addKendaraan() {  
            jumlahKendaraan++; // Menambah jumlah kendaraan  
        }  
    

    public void removeKendaraan() {  
        if (jumlahKendaraan > 0) {  
            jumlahKendaraan--; // Mengurangi jumlah kendaraan  
        }  
    }  

    public abstract void masukParkir(Kendaraan kendaraan); // Metode untuk kendaraan masuk  
    
    public abstract void keluarParkir(String nomorKendaraan); // Metode untuk kendaraan keluar  
}