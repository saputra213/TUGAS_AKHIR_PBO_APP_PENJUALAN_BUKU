import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Buku {
    private String judul;
    private String pengarang;
    private double harga;
    private int stok;

    public Buku(String judul, String pengarang, double harga, int stok) {
        this.judul = judul;
        this.pengarang = pengarang;
        this.harga = harga;
        this.stok = stok;
    }

    public String getJudul() {
        return judul;
    }

    public String getPengarang() {
        return pengarang;
    }

    public double getHarga() {
        return harga;
    }

    public int getStok() {
        return stok;
    }

    public void kurangiStok(int jumlah) {
        if (jumlah <= stok) {
            stok -= jumlah;
        } else {
            System.out.println("Stok tidak mencukupi.");
        }
    }
}

class Pelanggan {
    private String nama;
    private String alamat;

    public Pelanggan(String nama, String alamat) {
        this.nama = nama;
        this.alamat = alamat;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }
}

class Penjualan {
    private static int nomorTransaksi = 1;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private Buku buku;
    private Pelanggan pelanggan;
    private int jumlah;
    private double totalHarga;
    private String metodePembayaran;
    private Date tanggal;

    public Penjualan(Buku buku, Pelanggan pelanggan, int jumlah, String metodePembayaran) {
        this.buku = buku;
        this.pelanggan = pelanggan;
        this.jumlah = jumlah;
        this.metodePembayaran = metodePembayaran;
        hitungTotalHarga();
        buku.kurangiStok(jumlah);
        tanggal = new Date();
    }

    private void hitungTotalHarga() {
        totalHarga = buku.getHarga() * jumlah;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void tampilkanStruk() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        System.out.println("==============================");
        System.out.println("         STRUK PEMBELIAN       ");
        System.out.println("==============================");
        System.out.println("Nomor Transaksi: " + nomorTransaksi);
        System.out.println("Tanggal         : " + dateFormat.format(tanggal));
        System.out.println("------------------------------");
        System.out.println("Judul Buku      : " + buku.getJudul());
        System.out.println("Pengarang       : " + buku.getPengarang());
        System.out.println("Harga per unit  : Rp" + buku.getHarga());
        System.out.println("Jumlah          : " + jumlah);
        System.out.println("Total Harga     : Rp" + getTotalHarga());
        System.out.println("Pelanggan       : " + pelanggan.getNama());
        System.out.println("Alamat Pelanggan: " + pelanggan.getAlamat());
        System.out.println("Metode Pembayaran: " + metodePembayaran);
        System.out.println("==============================");
        System.out.println("         TERIMA KASIH         ");
        System.out.println("==============================");

        simpanStrukKeFile();
        nomorTransaksi++;
    }

    private void simpanStrukKeFile() {
        try (FileWriter writer = new FileWriter("struk_transaksi.txt", true)) {
            writer.write("==============================\n");
            writer.write("         STRUK PEMBELIAN       \n");
            writer.write("==============================\n");
            writer.write("Nomor Transaksi: " + nomorTransaksi + "\n");
            writer.write("Tanggal         : " + dateFormat.format(tanggal) + "\n");
            writer.write("------------------------------\n");
            writer.write("Judul Buku      : " + buku.getJudul() + "\n");
            writer.write("Pengarang       : " + buku.getPengarang() + "\n");
            writer.write("Harga per unit  : Rp" + buku.getHarga() + "\n");
            writer.write("Jumlah          : " + jumlah + "\n");
            writer.write("Total Harga     : Rp" + getTotalHarga() + "\n");
            writer.write("Pelanggan       : " + pelanggan.getNama() + "\n");
            writer.write("Alamat Pelanggan: " + pelanggan.getAlamat() + "\n");
            writer.write("Metode Pembayaran: " + metodePembayaran + "\n");
            writer.write("==============================\n");
            writer.write("         TERIMA KASIH         \n");
            writer.write("==============================\n\n");
        } catch (IOException e) {
            System.out.println("Kesalahan menulis ke file: " + e.getMessage());
        }
    }

    public String getDetailPenjualanUntukFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        return nomorTransaksi + "," + dateFormat.format(tanggal) + "," +
                buku.getJudul() + "," + buku.getPengarang() + "," + buku.getHarga() + "," +
                jumlah + "," + getTotalHarga() + "," + pelanggan.getNama() + "," +
                pelanggan.getAlamat() + "," + metodePembayaran;
    }
}


public class TokoBuku {
    static ArrayList<Buku> daftarBuku = new ArrayList<>();
    static ArrayList<Penjualan> riwayatPenjualan = new ArrayList<>();
    static Map<String, Double> metodePembayaran = new HashMap<>();

    static {
        daftarBuku.add(new Buku("Pemrograman Java", "John Doe", 25.99, 10));
        daftarBuku.add(new Buku("Dasar-dasar Python", "Jane Smith", 19.99, 15));
        daftarBuku.add(new Buku("Belajar CSS", "Ahmad", 29.99, 5));
        daftarBuku.add(new Buku("OOP dalam PHP", "Budi", 23.99, 12));
        daftarBuku.add(new Buku("Machine Learning untuk Pemula", "Citra", 33.99, 8));
        daftarBuku.add(new Buku("Web Design for Beginners", "Dian", 17.99, 20));
        daftarBuku.add(new Buku("Pengantar Basis Data", "Erik", 21.99, 7));
        daftarBuku.add(new Buku("Arsitektur Perangkat Lunak", "Fina", 27.99, 9));
        daftarBuku.add(new Buku("Algoritma dan Struktur Data", "Gilang", 31.99, 4));
        daftarBuku.add(new Buku("Internet of Things untuk Industri", "Hari", 37.99, 3));
        daftarBuku.add(new Buku("Keamanan Jaringan Komputer", "Ina", 14.99, 6));
        daftarBuku.add(new Buku("Otodidak Framework VueJS", "Joni", 18.99, 11));
        daftarBuku.add(new Buku("Pemrograman JavaScript", "Kurnia", 29.99, 10));
        daftarBuku.add(new Buku("Blockchain untuk Bisnis", "Luna", 36.99, 5));
        daftarBuku.add(new Buku("Pengenalan Keras", "Michael", 26.99, 7));
        daftarBuku.add(new Buku("Otomatisasi dengan Ansible", "Nadia", 19.99, 9));
        daftarBuku.add(new Buku("Mempelajari Bahasa R", "Omar", 33.99, 6));
        daftarBuku.add(new Buku("Pengolahan Data Big Data", "Prabu", 28.99, 12));
        daftarBuku.add(new Buku("RESTful API Development", "Queen", 24.99, 8));
        daftarBuku.add(new Buku("Dasar Pemrograman C++", "Rangga", 17.99, 15));
        daftarBuku.add(new Buku("Machine Learning dengan Scikit-Learn", "Siti", 31.99, 4));
        daftarBuku.add(new Buku("Kecerdasan Buatan dan Deep Learning", "Toni", 37.99, 3));
        daftarBuku.add(new Buku("Analisis Data Kuantitatif", "Umar", 22.99, 11));
        daftarBuku.add(new Buku("Pengantar Jaringan Komputer", "Vina", 16.99, 13));
        // Buku Umum
        daftarBuku.add(new Buku("Ensiklopedia Umum", "Tim Penulis", 79.99, 10));
        daftarBuku.add(new Buku("Kamus Besar Bahasa Indonesia", "Pujangga Tunggal", 89.99, 5));
        daftarBuku.add(new Buku("1001 Fakta Dunia yang Menakjubkan", "Wendy Dunia", 36.99, 12));

// Buku Fisika
        daftarBuku.add(new Buku("Fisika Dasar Edisi Revisi", "Stephen Hawking", 49.99, 8));
        daftarBuku.add(new Buku("Optika dan Fotonika Modern", "Len Albert", 56.99, 6));
        daftarBuku.add(new Buku("Pengantar Fisika Kuantum", "Richard Feynman", 39.99, 9));

// Lain-lain
        daftarBuku.add(new Buku("Kumpulan Puisi Terbaik 2022", "Komunitas Puisi", 29.99, 15));
        daftarBuku.add(new Buku("1001 Lowongan Kerja Tahun 2023", "IndoJobs", 26.99, 7));
        daftarBuku.add(new Buku("Panduan Budidaya Tanaman Hidroponik", "Petani Muda", 23.99, 12));

        // Tambahkan lebih banyak buku sesuai kebutuhan

        metodePembayaran.put("Kartu Kredit", 1.02); // Biaya pemrosesan 2%
        metodePembayaran.put("Tunai", 1.0); // Tidak ada biaya pemrosesan
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Selamat datang di Toko Buku!");
            System.out.println("1. Telusuri Buku");
            System.out.println("2. Lakukan Pembelian");
            System.out.println("3. Lihat Riwayat Penjualan");
            System.out.println("4. Keluar");
            System.out.print("Masukkan pilihan Anda: ");
            int pilihan = scanner.nextInt();

            switch (pilihan) {
                case 1:
                    tampilkanDaftarBuku();
                    break;
                case 2:
                    lakukanPembelian(scanner);
                    break;
                case 3:
                    lihatRiwayatPenjualan();
                    break;
                case 4:
                    simpanRiwayatPenjualanKeFile();
                    System.out.println("Keluar dari program. Selamat tinggal!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        }
    }

    static void tampilkanDaftarBuku() {
        System.out.println("Daftar Buku Tersedia:");
        for (int i = 0; i < daftarBuku.size(); i++) {
            Buku buku = daftarBuku.get(i);
            System.out.println((i + 1) + ". " + buku.getJudul() + " oleh " + buku.getPengarang() +
                    " - Rp" + buku.getHarga() + " (Stok: " + buku.getStok() + ")");
        }
    }

    static void lakukanPembelian(Scanner scanner) {
        tampilkanDaftarBuku();

        System.out.print("Masukkan nomor buku yang ingin dibeli: ");
        int indeksBukuTerpilih = scanner.nextInt() - 1;

        System.out.print("Masukkan nama Anda: ");
        String namaPelanggan = scanner.next();

        System.out.print("Masukkan alamat Anda: ");
        String alamatPelanggan = scanner.next();

        System.out.print("Masukkan jumlah pembelian: ");
        int jumlah = scanner.nextInt();

        scanner.nextLine(); // Konsumsi karakter newline

        System.out.println("Pilih Metode Pembayaran:");
        for (String metode : metodePembayaran.keySet()) {
            System.out.println(metode);
        }

        System.out.print("Masukkan metode pembayaran: ");
        String metodePembayaranPilihan = scanner.nextLine();

        if (metodePembayaran.containsKey(metodePembayaranPilihan)) {
            if (indeksBukuTerpilih >= 0 && indeksBukuTerpilih < daftarBuku.size()) {
                Buku bukuTerpilih = daftarBuku.get(indeksBukuTerpilih);
                Pelanggan pelanggan = new Pelanggan(namaPelanggan, alamatPelanggan);
                Penjualan penjualan = new Penjualan(bukuTerpilih, pelanggan, jumlah, metodePembayaranPilihan);

                riwayatPenjualan.add(penjualan);

                penjualan.tampilkanStruk();
            } else {
                System.out.println("Pilihan buku tidak valid.");
            }
        } else {
            System.out.println("Metode pembayaran tidak valid.");
        }
    }

    private static void lihatRiwayatPenjualan() {
        String namaFile = "struk_transaksi.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(namaFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
        }
    }

    static void simpanRiwayatPenjualanKeFile() {
        try (FileWriter writer = new FileWriter("riwayat_penjualan.txt")) {
            for (Penjualan penjualan : riwayatPenjualan) {
                writer.write(penjualan.getDetailPenjualanUntukFile() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Kesalahan menulis ke file: " + e.getMessage());
        }
    }


}



