public abstract class ParkingArea {  
    protected int kapasitasMaks;  
    protected int jumlahKendaraan;  

    public ParkingArea(int kapasitasMaks) {  
        this.kapasitasMaks = kapasitasMaks;  
        this.jumlahKendaraan = 0; // Mulai dengan jumlah kendaraan 0  
    }  

    protected boolean isSlotAvailable() {  
        return jumlahKendaraan < kapasitasMaks;  
    }  

    protected void addKendaraan() {  
        if (isSlotAvailable()) {  
            jumlahKendaraan++;  
        }  
    }  

    protected void removeKendaraan() {  
        if (jumlahKendaraan > 0) {  
            jumlahKendaraan--;  
        }  
    }  

    protected abstract void saveKendaraanToDatabase(Kendaraan kendaraan);  
    protected abstract void deleteKendaraanFromDatabase(String nomorKendaraan);  
}