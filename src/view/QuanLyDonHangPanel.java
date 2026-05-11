package src.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class QuanLyDonHangPanel extends JPanel {

    // ===== MÀU GIAO DIỆN =====
    private final Color MAIN_BG = new Color(244, 247, 246);
    private final Color BRAND_GOLD = new Color(212, 175, 55);
    private final Color SIDEBAR_BG = new Color(5, 5, 5);
    private final Color ROW_WHITE = Color.WHITE;

    // ===== COMPONENT =====
    private DefaultTableModel cartTableModel;
    private JTable cartTable;

    private JTextField txtMaSP, txtSoLuong;
    private JTextField txtKhuyenMai, txtSdtKhachHang;

    private JLabel lblTongTien, lblGiamGia, lblThanhTien;

    // ===== DỮ LIỆU =====
    private double tongTien = 0;
    private double tienGiamGia = 0;

    // ===== FORMAT TIỀN VNĐ =====
    private final NumberFormat currencyVN =
            NumberFormat.getCurrencyInstance(
                    Locale.of("vi", "VN")
            );

    public QuanLyDonHangPanel() {

        setLayout(new BorderLayout(20, 20));

        setBackground(MAIN_BG);

        setBorder(new EmptyBorder(20, 20, 20, 20));

        // =====================================================
        // TITLE
        // =====================================================
        JLabel title = new JLabel("LẬP ĐƠN HÀNG TẠI QUẦY");

        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        title.setForeground(SIDEBAR_BG);

        add(title, BorderLayout.NORTH);

        // =====================================================
        // CENTER PANEL
        // =====================================================
        JPanel centerPanel = new JPanel(
                new BorderLayout(20, 0)
        );

        centerPanel.setBackground(MAIN_BG);

        centerPanel.add(
                buildProductSelectionPanel(),
                BorderLayout.WEST
        );

        centerPanel.add(
                buildCartAndCheckoutPanel(),
                BorderLayout.CENTER
        );

        add(centerPanel, BorderLayout.CENTER);
    }

    // =========================================================
    // PANEL CHỌN SẢN PHẨM
    // =========================================================
    private JPanel buildProductSelectionPanel() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBackground(ROW_WHITE);

        panel.setPreferredSize(new Dimension(350, 0));

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                Color.LIGHT_GRAY
                        ),
                        "🔍 Nhập Sản Phẩm",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14),
                        SIDEBAR_BG
                )
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1.0;

        // =====================================================
        // MÃ SẢN PHẨM
        // =====================================================
        gbc.gridx = 0;
        gbc.gridy = 0;

        panel.add(new JLabel("Mã Sản Phẩm:"), gbc);

        txtMaSP = new JTextField();

        gbc.gridy = 1;

        panel.add(txtMaSP, gbc);

        // =====================================================
        // SỐ LƯỢNG
        // =====================================================
        gbc.gridy = 2;

        panel.add(new JLabel("Số Lượng:"), gbc);

        txtSoLuong = new JTextField("1");

        gbc.gridy = 3;

        panel.add(txtSoLuong, gbc);

        // =====================================================
        // BUTTON THÊM
        // =====================================================
        JButton btnAdd = new JButton("Thêm Vào Giỏ");

        btnAdd.setBackground(SIDEBAR_BG);

        btnAdd.setForeground(Color.WHITE);

        btnAdd.setFocusPainted(false);

        btnAdd.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        gbc.gridy = 4;

        gbc.insets = new Insets(20, 10, 10, 10);

        panel.add(btnAdd, gbc);

        // =====================================================
        // EVENT BUTTON
        // =====================================================
        btnAdd.addActionListener(e -> themSanPham());

        gbc.gridy = 5;

        gbc.weighty = 1.0;

        panel.add(new JLabel(""), gbc);

        return panel;
    }

    // =========================================================
    // PANEL GIỎ HÀNG
    // =========================================================
    private JPanel buildCartAndCheckoutPanel() {

        JPanel panel = new JPanel(
                new BorderLayout(0, 10)
        );

        panel.setBackground(MAIN_BG);

        // =====================================================
        // TABLE
        // =====================================================
        String[] columns = {
                "Mã SP",
                "Tên SP",
                "Đơn Giá",
                "Số Lượng",
                "Thành Tiền"
        };

        cartTableModel = new DefaultTableModel(
                columns,
                0
        );

        cartTable = new JTable(cartTableModel);

        cartTable.setRowHeight(30);

        cartTable.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        JScrollPane scrollPane = new JScrollPane(cartTable);

        panel.add(scrollPane, BorderLayout.CENTER);

        // =====================================================
        // BOTTOM PANEL
        // =====================================================
        JPanel bottomPanel = new JPanel(
                new GridLayout(1, 2, 20, 0)
        );

        bottomPanel.setBackground(MAIN_BG);

        // =====================================================
        // ACTION PANEL
        // =====================================================
        JPanel actionPanel = new JPanel(
                new GridLayout(4, 1, 0, 10)
        );

        actionPanel.setBackground(MAIN_BG);

        // =====================================================
        // PANEL KHUYẾN MÃI
        // =====================================================
        JPanel promoPanel = new JPanel(
                new BorderLayout(5, 0)
        );

        txtKhuyenMai = new JTextField();

        JButton btnKhuyenMai =
                new JButton("Áp Dụng");

        btnKhuyenMai.setBackground(BRAND_GOLD);

        btnKhuyenMai.setForeground(Color.WHITE);

        promoPanel.add(
                new JLabel("Mã KM: "),
                BorderLayout.WEST
        );

        promoPanel.add(
                txtKhuyenMai,
                BorderLayout.CENTER
        );

        promoPanel.add(
                btnKhuyenMai,
                BorderLayout.EAST
        );

        btnKhuyenMai.addActionListener(
                e -> apDungKhuyenMai()
        );

        // =====================================================
        // PANEL KHÁCH HÀNG
        // =====================================================
        JPanel memberPanel = new JPanel(
                new BorderLayout(5, 0)
        );

        txtSdtKhachHang = new JTextField();

        JButton btnTichDiem =
                new JButton("Tìm");

        btnTichDiem.setBackground(
                new Color(50, 120, 200)
        );

        btnTichDiem.setForeground(Color.WHITE);

        memberPanel.add(
                new JLabel("SĐT KH: "),
                BorderLayout.WEST
        );

        memberPanel.add(
                txtSdtKhachHang,
                BorderLayout.CENTER
        );

        memberPanel.add(
                btnTichDiem,
                BorderLayout.EAST
        );

        btnTichDiem.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Khách hàng: "
                            + txtSdtKhachHang.getText()
            );
        });

        actionPanel.add(promoPanel);

        actionPanel.add(memberPanel);

        // =====================================================
        // TOTAL PANEL
        // =====================================================
        JPanel totalPanel = new JPanel(
                new GridLayout(4, 1, 0, 5)
        );

        totalPanel.setBackground(ROW_WHITE);

        totalPanel.setBorder(
                new EmptyBorder(10, 10, 10, 10)
        );

        lblTongTien = new JLabel(
                "Tổng tiền: 0đ",
                SwingConstants.RIGHT
        );

        lblGiamGia = new JLabel(
                "Giảm giá: 0đ",
                SwingConstants.RIGHT
        );

        lblThanhTien = new JLabel(
                "THANH TOÁN: 0đ",
                SwingConstants.RIGHT
        );

        lblThanhTien.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
        );

        lblThanhTien.setForeground(Color.RED);

        JButton btnCheckout =
                new JButton("XÁC NHẬN THANH TOÁN");

        btnCheckout.setBackground(
                new Color(40, 167, 69)
        );

        btnCheckout.setForeground(Color.WHITE);

        btnCheckout.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        btnCheckout.addActionListener(
                e -> thanhToan()
        );

        totalPanel.add(lblTongTien);

        totalPanel.add(lblGiamGia);

        totalPanel.add(lblThanhTien);

        totalPanel.add(btnCheckout);

        bottomPanel.add(actionPanel);

        bottomPanel.add(totalPanel);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    // =========================================================
    // THÊM SẢN PHẨM
    // =========================================================
    private void themSanPham() {

        String maSP = txtMaSP.getText().trim();

        String slStr = txtSoLuong.getText().trim();

        if (maSP.isEmpty() || slStr.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập đầy đủ thông tin!"
            );

            return;
        }

        int soLuong;

        try {

            soLuong = Integer.parseInt(slStr);

            if (soLuong <= 0) {
                throw new Exception();
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Số lượng không hợp lệ!"
            );

            return;
        }

        // =====================================================
        // DEMO DỮ LIỆU
        // =====================================================
        String tenSP = "Sản phẩm " + maSP;

        double donGia = 100000;

        double thanhTien = donGia * soLuong;

        cartTableModel.addRow(new Object[]{
                maSP,
                tenSP,
                currencyVN.format(donGia),
                soLuong,
                currencyVN.format(thanhTien)
        });

        tongTien += thanhTien;

        capNhatTongTien();

        txtMaSP.setText("");

        txtSoLuong.setText("1");
    }

    // =========================================================
    // ÁP DỤNG KHUYẾN MÃI
    // =========================================================
    private void apDungKhuyenMai() {

        String maKM = txtKhuyenMai
                .getText()
                .trim();

        if (maKM.equalsIgnoreCase("SALE10")) {

            tienGiamGia = tongTien * 0.1;

            JOptionPane.showMessageDialog(
                    this,
                    "Áp dụng mã thành công!"
            );

        } else {

            tienGiamGia = 0;

            JOptionPane.showMessageDialog(
                    this,
                    "Mã khuyến mãi không hợp lệ!"
            );
        }

        capNhatTongTien();
    }

    // =========================================================
    // CẬP NHẬT TỔNG TIỀN
    // =========================================================
    private void capNhatTongTien() {

        double thanhToan =
                tongTien - tienGiamGia;

        lblTongTien.setText(
                "Tổng tiền: "
                        + currencyVN.format(tongTien)
        );

        lblGiamGia.setText(
                "Giảm giá: -"
                        + currencyVN.format(tienGiamGia)
        );

        lblThanhTien.setText(
                "THANH TOÁN: "
                        + currencyVN.format(thanhToan)
        );
    }

    // =========================================================
    // THANH TOÁN
    // =========================================================
    private void thanhToan() {

        if (cartTableModel.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Giỏ hàng đang trống!"
            );

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Thanh toán thành công!"
        );

        // =====================================================
        // RESET GIỎ HÀNG
        // =====================================================
        cartTableModel.setRowCount(0);

        tongTien = 0;

        tienGiamGia = 0;

        capNhatTongTien();
    }
}