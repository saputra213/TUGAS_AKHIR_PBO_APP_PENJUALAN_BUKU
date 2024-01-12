import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class TokoBukuGUI {
    private static JTextField usernameField;
    private static JPasswordField passwordField;
    private static JFrame frameLogin;
    private static JFrame frame;
    private static JPanel panel;
    private static JTextArea outputTextArea;
    private static JFrame historyFrame;
    private static JTextArea historyTextArea;
    private static JPanel backgroundPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            tampilkanLogin();
        });
    }


    private static void tampilkanLogin() {
        frameLogin = new JFrame("Login");
        frameLogin.setSize(300, 150);
        frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameLogin.setLocationRelativeTo(null);

        JPanel panelLogin = new JPanel();
        panelLogin.setLayout(new GridLayout(3, 2));

        JLabel labelUsername = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel labelPassword = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton tombolLogin = new JButton("Login");

        tombolLogin.addActionListener(e -> {
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);

            if (validasiLogin(username, password)) {
                frameLogin.dispose(); // Menutup dialog login
                // Membuka jendela aplikasi utama atau melanjutkan ke menu berikutnya
                bukaAplikasiUtama();
            } else {
                JOptionPane.showMessageDialog(null, "Username atau password tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton tombolRegistrasi = new JButton("Registrasi");
        tombolRegistrasi.addActionListener(e -> tampilkanRegistrasi());

        panelLogin.add(labelUsername);
        panelLogin.add(usernameField);
        panelLogin.add(labelPassword);
        panelLogin.add(passwordField);
        panelLogin.add(tombolLogin);
        panelLogin.add(tombolRegistrasi);

        frameLogin.add(panelLogin);
        frameLogin.setVisible(true);
    }

    private static void tampilkanRegistrasi() {
        JFrame frameRegistrasi = new JFrame("Registrasi");
        frameRegistrasi.setSize(300, 150);
        frameRegistrasi.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameRegistrasi.setLocationRelativeTo(null);

        JPanel panelRegistrasi = new JPanel();
        panelRegistrasi.setLayout(new GridLayout(3, 2));

        JLabel labelUsername = new JLabel("Username Baru:");
        JTextField fieldUsernameBaru = new JTextField();

        JLabel labelPassword = new JLabel("Password Baru:");
        JPasswordField fieldPasswordBaru = new JPasswordField();

        JButton tombolRegistrasi = new JButton("Registrasi");

        tombolRegistrasi.addActionListener(e -> {
            String usernameBaru = fieldUsernameBaru.getText();
            char[] passwordBaruChars = fieldPasswordBaru.getPassword();
            String passwordBaru = new String(passwordBaruChars);

            if (registrasiUser(usernameBaru, passwordBaru)) {
                JOptionPane.showMessageDialog(null, "Registrasi berhasil. Anda sekarang dapat login.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                frameRegistrasi.dispose(); // Menutup dialog registrasi
            } else {
                JOptionPane.showMessageDialog(null, "Error saat mendaftarkan pengguna.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panelRegistrasi.add(labelUsername);
        panelRegistrasi.add(fieldUsernameBaru);
        panelRegistrasi.add(labelPassword);
        panelRegistrasi.add(fieldPasswordBaru);
        panelRegistrasi.add(tombolRegistrasi);

        frameRegistrasi.add(panelRegistrasi);
        frameRegistrasi.setVisible(true);
    }

    // Metode-metode yang sudah ada...

    private static boolean validasiLogin(String username, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app_bukuku", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean registrasiUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app_bukuku", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void bukaAplikasiUtama() {
        JOptionPane.showMessageDialog(null, "Login berhasil! Buka jendela aplikasi utama.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        frame = new JFrame("Toko Buku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        JLabel titleLabel = new JLabel("TokoBukuku by AJIPS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(titleLabel, BorderLayout.NORTH);

        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load your background image (change the path accordingly)
                Image backgroundImage = new ImageIcon("src/bukuku_bg.jpg").getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(new BorderLayout());

        frame.add(backgroundPanel);
        frame.setVisible(true);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        outputTextArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton browseButton = new JButton("Telusuri Buku");
        JButton purchaseButton = new JButton("Lakukan Pembelian");
        JButton historyButton = new JButton("Lihat Struk Belanja");
        JButton databuyButton = new JButton("Lihat Data Belanja");
        JButton exitButton = new JButton("Keluar");

        // Set button styles
        styleButton(browseButton);
        styleButton(purchaseButton);
        styleButton(historyButton);
        styleButton(databuyButton);
        styleButton(exitButton);

        // Button actions
        browseButton.addActionListener(e -> tampilkanDaftarBuku());
        purchaseButton.addActionListener(e -> lakukanPembelian());
        historyButton.addActionListener(e -> lihatRiwayatPenjualan());
        databuyButton.addActionListener(e -> PembelianGUI.databeli());
        exitButton.addActionListener(e -> {
            simpanRiwayatPenjualanKeFile();
            System.out.println("Keluar dari program. Selamat tinggal!");
            System.exit(0);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.add(browseButton);
        buttonPanel.add(purchaseButton);
        buttonPanel.add(historyButton);
        buttonPanel.add(databuyButton);
        buttonPanel.add(exitButton);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(50, 150, 255));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }


    static void tampilkanDaftarBuku() {
        outputTextArea.setText(""); // Clear the text area
        outputTextArea.append("Daftar Buku Tersedia:\n");

        for (int i = 0; i < TokoBuku.daftarBuku.size(); i++) {
            Buku buku = TokoBuku.daftarBuku.get(i);
            outputTextArea.append((i + 1) + ". " + buku.getJudul() + " oleh " + buku.getPengarang() +
                    " - Rp" + buku.getHarga() + " (Stok: " + buku.getStok() + ")\n");
        }
    }

    static void lakukanPembelian() {
        JFrame purchaseFrame = new JFrame("Lakukan Pembelian");
        purchaseFrame.setSize(300, 200);
        purchaseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel purchasePanel = new JPanel();
        purchasePanel.setLayout(new GridLayout(5, 2));

        JLabel bookLabel = new JLabel("Nomor Buku:");
        JTextField bookField = new JTextField();

        JLabel nameLabel = new JLabel("Nama Pelanggan:");
        JTextField nameField = new JTextField();

        JLabel addressLabel = new JLabel("Alamat Pelanggan:");
        JTextField addressField = new JTextField();

        JLabel quantityLabel = new JLabel("Jumlah Pembelian:");
        JTextField quantityField = new JTextField();

        JLabel paymentLabel = new JLabel("Metode Pembayaran:");
        JComboBox<String> paymentComboBox = new JComboBox<>(TokoBuku.metodePembayaran.keySet().toArray(new String[0]));

        JButton confirmButton = new JButton("Konfirmasi Pembelian");

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedBookIndex = Integer.parseInt(bookField.getText()) - 1;
                String customerName = nameField.getText();
                String customerAddress = addressField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                String selectedPaymentMethod = (String) paymentComboBox.getSelectedItem();

                if (TokoBuku.metodePembayaran.containsKey(selectedPaymentMethod)) {
                    if (selectedBookIndex >= 0 && selectedBookIndex < TokoBuku.daftarBuku.size()) {
                        Buku selectedBook = TokoBuku.daftarBuku.get(selectedBookIndex);
                        Pelanggan customer = new Pelanggan(customerName, customerAddress);
                        Penjualan purchase = new Penjualan(selectedBook, customer, quantity, selectedPaymentMethod);

                        TokoBuku.riwayatPenjualan.add(purchase);

                        TokoBuku.DatabaseManager.simpanPembelian(purchase);

                        purchase.tampilkanStruk();
                        purchaseFrame.dispose(); // Close the purchase dialog
                    } else {
                        JOptionPane.showMessageDialog(null, "Nomor buku tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Metode pembayaran tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        purchasePanel.add(bookLabel);
        purchasePanel.add(bookField);
        purchasePanel.add(nameLabel);
        purchasePanel.add(nameField);
        purchasePanel.add(addressLabel);
        purchasePanel.add(addressField);
        purchasePanel.add(quantityLabel);
        purchasePanel.add(quantityField);
        purchasePanel.add(paymentLabel);
        purchasePanel.add(paymentComboBox);

        purchaseFrame.add(purchasePanel, BorderLayout.CENTER);
        purchaseFrame.add(confirmButton, BorderLayout.SOUTH);
        purchaseFrame.setVisible(true);
    }


    private static void lihatRiwayatPenjualan() {
        if (historyFrame == null) {
            historyFrame = new JFrame("Riwayat Penjualan");
            historyFrame.setSize(400, 300);
            historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel historyPanel = new JPanel();
            historyPanel.setLayout(new BorderLayout());

            historyTextArea = new JTextArea();
            historyTextArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(historyTextArea);
            historyPanel.add(scrollPane, BorderLayout.CENTER);

            JButton closeButton = new JButton("Tutup");
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    historyFrame.dispose(); // Close the history dialog
                }
            });

            historyPanel.add(closeButton, BorderLayout.SOUTH);

            historyFrame.add(historyPanel);
        }

        historyTextArea.setText(""); // Clear the text area

        // Read transaction history from the file
        String historyFileName = "struk_transaksi.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(historyFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                historyTextArea.append(line + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Kesalahan membaca file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        historyFrame.setVisible(true);
    }

    static void simpanRiwayatPenjualanKeFile() {
        // Implement saving history to file logic for GUI
    }

    public static class PembelianGUI extends JFrame {
        private JTable table;
        private DefaultTableModel tableModel;

        public PembelianGUI() {
            initializeUI();
            tampilkanSemuaPembelian();
        }

        private void initializeUI() {
            setTitle("Data Pembelian");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setPreferredSize(new Dimension(800, 600));
            setIconImage(new ImageIcon("src/database.jpg").getImage()); // Ganti dengan path ke ikon yang sesuai

            // Buat panel utama dengan layout border dan batas
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Panel bagian atas untuk judul
            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(new Color(102, 178, 255)); // Warna latar belakang biru
            JLabel titleLabel = new JLabel("Data Pembelian");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setForeground(Color.WHITE); // Warna teks putih
            titlePanel.add(titleLabel);

            // Tambahkan judul ke bagian atas
            mainPanel.add(titlePanel, BorderLayout.NORTH);

            // Buat tabel dengan model
            tableModel = new DefaultTableModel();
            table = new JTable(tableModel);
            table.getTableHeader().setBackground(new Color(51, 153, 255)); // Warna latar belakang header tabel
            table.getTableHeader().setForeground(Color.black); // Warna teks header tabel

            // Tambahkan tabel ke dalam scroll pane
            JScrollPane scrollPane = new JScrollPane(table);

            // Tambahkan scroll pane ke bagian tengah
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            // Tambahkan panel utama ke frame
            add(mainPanel);

            // Rapihkan antarmuka pengguna
            pack();

            // Pusatkan frame di tengah layar
            setLocationRelativeTo(null);
        }



        private void tampilkanSemuaPembelian() {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/app_bukuku", "root", "")) {
                String query = "SELECT * FROM pembelian";
                try (Statement statement = connection.createStatement()) {
                    ResultSet resultSet = statement.executeQuery(query);

                    // Mengisi model tabel dengan data dari database
                    tableModel.addColumn("ID");
                    tableModel.addColumn("Judul Buku");
                    tableModel.addColumn("Nama Pelanggan");
                    tableModel.addColumn("Alamat Pelanggan");
                    tableModel.addColumn("Jumlah");
                    tableModel.addColumn("Metode Pembayaran");

                    while (resultSet.next()) {
                        Object[] row = new Object[6];
                        row[0] = resultSet.getInt("id");
                        row[1] = resultSet.getString("judul_buku");
                        row[2] = resultSet.getString("nama_pelanggan");
                        row[3] = resultSet.getString("alamat_pelanggan");
                        row[4] = resultSet.getInt("jumlah");
                        row[5] = resultSet.getString("metode_pembayaran");
                        tableModel.addRow(row);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static void databeli() {
            SwingUtilities.invokeLater(() -> {
                PembelianGUI pembelianGUI = new PembelianGUI();
                pembelianGUI.setVisible(true);
            });
        }
    }
}

