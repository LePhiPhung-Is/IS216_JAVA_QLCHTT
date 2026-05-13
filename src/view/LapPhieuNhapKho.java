package src.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * Chức năng: Lập phiếu nhập kho - BEAUTY SHOP
 * Sinh viên thực hiện: ĐOÀN XUÂN CHIẾN - MSSV: 24520217
 */
public class LapPhieuNhapKho extends JPanel {
    private JComboBox<String> cbNCC, cbSanPham;
    private JTextField txtSoLuong, txtDonGia, txtTongTienPhieu;
    private JTable tableDetails;
    private DefaultTableModel modelDetails;
    private JButton btnAddToList, btnComplete;

    // Màu sắc đồng bộ với UI chung của Chiến
    private final Color BRAND_GOLD = new Color(212, 175, 55);
    private final Color MAIN_BG = new Color(244, 247, 246);

    // Cấu hình Database Oracle UIT
    private final String DB_URL = "jdbc:oracle:thin:@localhost:1522/xepdb1";
    private final String USERNAME = "sinhvien02";
    private final String DB_PASS = "123";

    public LapPhieuNhapKho() {
        setLayout(new BorderLayout(20, 20));
        setBackground(MAIN_BG);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        initComponents();
        loadNCCData();
        loadProductData();
    }

    private void initComponents() {
        // --- Header ---
        JLabel title = new JLabel("LẬP PHIẾU NHẬP KHO");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // --- Form nhập liệu ---
        JPanel pnlInput = new JPanel(new GridLayout(2, 4, 15, 15));
        pnlInput.setBackground(MAIN_BG);
        pnlInput.setBorder(new TitledBorder(BorderFactory.createLineBorder(BRAND_GOLD), "Thông tin hàng nhập"));

        pnlInput.add(new JLabel("Nhà Cung Cấp:"));
        cbNCC = new JComboBox<>();
        pnlInput.add(cbNCC);

        pnlInput.add(new JLabel("Sản Phẩm:"));
        cbSanPham = new JComboBox<>();
        pnlInput.add(cbSanPham);

        pnlInput.add(new JLabel("Số Lượng:"));
        txtSoLuong = new JTextField();
        pnlInput.add(txtSoLuong);

        pnlInput.add(new JLabel("Đơn Giá Nhập:"));
        txtDonGia = new JTextField();
        pnlInput.add(txtDonGia);

        // --- Bảng danh sách sản phẩm chờ nhập ---
        String[] columns = {"Mã SP", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        modelDetails = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableDetails = new JTable(modelDetails);
        tableDetails.setRowHeight(30);
        
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 15));
        pnlCenter.setBackground(MAIN_BG);
        pnlCenter.add(pnlInput, BorderLayout.NORTH);
        pnlCenter.add(new JScrollPane(tableDetails), BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // --- Bottom: Tổng tiền & Nút bấm ---
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(MAIN_BG);

        JPanel pnlTotal = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTotal.setBackground(MAIN_BG);
        pnlTotal.add(new JLabel("Tổng tiền phiếu: "));
        txtTongTienPhieu = new JTextField("0", 15);
        txtTongTienPhieu.setEditable(false);
        txtTongTienPhieu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlTotal.add(txtTongTienPhieu);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlButtons.setBackground(MAIN_BG);
        
        btnAddToList = new JButton("Thêm sản phẩm");
        btnComplete = new JButton("Xác nhận nhập kho");
        btnComplete.setBackground(BRAND_GOLD);
        btnComplete.setForeground(Color.BLACK);
        btnComplete.setFont(new Font("Segoe UI", Font.BOLD, 13));

        pnlButtons.add(btnAddToList);
        pnlButtons.add(btnComplete);

        pnlFooter.add(pnlTotal, BorderLayout.WEST);
        pnlFooter.add(pnlButtons, BorderLayout.EAST);
        add(pnlFooter, BorderLayout.SOUTH);

        // --- Sự kiện ---
        btnAddToList.addActionListener(e -> addRowToTable());
        btnComplete.addActionListener(e -> saveToDatabase());
    }

    private void loadNCCData() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MANCC || ' - ' || TENNCC as NCC FROM NHACUNGCAP")) {
            cbNCC.removeAllItems();
            while (rs.next()) cbNCC.addItem(rs.getString("NCC"));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadProductData() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MASP || ' - ' || TENSP as SP FROM SANPHAM")) {
            cbSanPham.removeAllItems();
            while (rs.next()) cbSanPham.addItem(rs.getString("SP"));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void addRowToTable() {
        try {
            if (txtSoLuong.getText().isEmpty() || txtDonGia.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Số lượng và Đơn giá!");
                return;
            }
            String spInfo = cbSanPham.getSelectedItem().toString();
            String maSP = spInfo.split(" - ")[0];
            String tenSP = spInfo.split(" - ")[1];
            int sl = Integer.parseInt(txtSoLuong.getText());
            double gia = Double.parseDouble(txtDonGia.getText());
            double thanhTien = sl * gia;

            modelDetails.addRow(new Object[]{maSP, tenSP, sl, gia, thanhTien});
            updateTotal();
            
            txtSoLuong.setText("");
            txtDonGia.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng và Đơn giá phải là số!");
        }
    }

    private void updateTotal() {
        double total = 0;
        for (int i = 0; i < modelDetails.getRowCount(); i++) {
            total += Double.parseDouble(modelDetails.getValueAt(i, 4).toString());
        }
        txtTongTienPhieu.setText(String.format("%.0f", total));
    }

    private void saveToDatabase() {
        if (modelDetails.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Danh sách chờ nhập kho đang trống!");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, DB_PASS)) {
            conn.setAutoCommit(false);
            
            // 1. Lưu Phiếu Nhập
            String maNCC = cbNCC.getSelectedItem().toString().split(" - ")[0];
            String sqlPN = "INSERT INTO PHIEUNHAP (MAPN, MANCC, NGAYNHAP, TONGTIEN) VALUES (SEQ_PN.NEXTVAL, ?, CURRENT_DATE, ?)";
            PreparedStatement psPN = conn.prepareStatement(sqlPN, new String[]{"MAPN"});
            psPN.setString(1, maNCC);
            psPN.setDouble(2, Double.parseDouble(txtTongTienPhieu.getText()));
            psPN.executeUpdate();

            ResultSet rs = psPN.getGeneratedKeys();
            if (rs.next()) {
                String maPN = rs.getString(1);

                // 2. Lưu Chi Tiết & Cập nhật kho (Batch)
                String sqlCT = "INSERT INTO CHITIETPN (MAPN, MASP, SOLUONG, DONGIA) VALUES (?, ?, ?, ?)";
                String sqlUpdateStock = "UPDATE SANPHAM SET SOLUONG = SOLUONG + ? WHERE MASP = ?";
                
                PreparedStatement psCT = conn.prepareStatement(sqlCT);
                PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock);
                
                for (int i = 0; i < modelDetails.getRowCount(); i++) {
                    String maSP = modelDetails.getValueAt(i, 0).toString();
                    int sl = Integer.parseInt(modelDetails.getValueAt(i, 2).toString());
                    double gia = Double.parseDouble(modelDetails.getValueAt(i, 3).toString());

                    psCT.setString(1, maPN);
                    psCT.setString(2, maSP);
                    psCT.setInt(3, sl);
                    psCT.setDouble(4, gia);
                    psCT.addBatch();

                    psStock.setInt(1, sl);
                    psStock.setString(2, maSP);
                    psStock.addBatch();
                }
                psCT.executeBatch();
                psStock.executeBatch();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Đã hoàn tất nhập kho và cập nhật số lượng tồn!");
            modelDetails.setRowCount(0);
            txtTongTienPhieu.setText("0");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiếu: " + e.getMessage());
        }
    }
}