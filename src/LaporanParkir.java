public class LaporanParkir {

    private final String kategori;
    private final String jenis;
    private final int jumlah;

    public LaporanParkir(String kategori, String jenis, int jumlah) {
        this.kategori = kategori;
        this.jenis = jenis;
        this.jumlah = jumlah;
    }

    public String getKategori() {
        return kategori;
    }

    public String getJenis() {
        return jenis;
    }

    public int getJumlah() {
        return jumlah;
    }
}