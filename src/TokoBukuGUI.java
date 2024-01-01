import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TokoBukuGUI {

    private static JFrame frame;
    private static JPanel panel;
    private static JTextArea outputTextArea;
    private static JFrame historyFrame;
    private static JTextArea historyTextArea;
    private static JPanel backgroundPanel;



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }



    private static void createAndShowGUI() {


        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
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

        // ... (existing code)

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
        JButton historyButton = new JButton("Lihat Riwayat Penjualan");
        JButton exitButton = new JButton("Keluar");

        // Set button styles
        styleButton(browseButton);
        styleButton(purchaseButton);
        styleButton(historyButton);
        styleButton(exitButton);

        // Button actions
        browseButton.addActionListener(e -> tampilkanDaftarBuku());
        purchaseButton.addActionListener(e -> lakukanPembelian());
        historyButton.addActionListener(e -> lihatRiwayatPenjualan());
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
        buttonPanel.add(exitButton);

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
}
