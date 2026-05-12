package src.view;

import src.dao.DonHangDAO;
import src.dao.KhachHangDAO;
import src.dao.KhuyenMaiDAO;
import src.dao.SanPhamDAO;
import src.model.ChiTietDonHang;
import src.model.DonHang;
import src.model.KhachHang;
import src.model.KhuyenMai;
import src.model.SanPham;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class QuanLyDonHangPanel extends JPanel {

    // ===== MÀU GIAO DIỆN =====
    private final Color MAIN_BG = new Color(244, 247, 246);
    private final Color BRAND_GOLD = new Color(212, 175, 55);
    private final Color SIDEBAR_BG = new Color(5, 5, 5);
    private final Color ROW_WHITE = Color.WHITE;

    // ===== COMPONENT =====
    private DefaultTableModel cartTableModel;
    private JTable cartTable;
    private JTextField txtTimKiem, txtSoLuong, txtKhuyenMai, txtSdtKhachHang;
    private JLabel lblAnhSP, lblHienThiTenSP, lblHienThiGiaSP, lblHienThiTonKho;
    private JLabel lblTongTien, lblGiamGia, lblThanhTien;

    // ===== DỮ LIỆU TẠM =====
    private String currentMaSP = "";
    private String currentTenSP = "";
    private double currentGiaSP = 0;
    private int currentTonKho = 0; // Lưu số lượng tồn kho

    private String currentMaKH = null; // Lưu mã KH đang giao dịch
    private KhuyenMai khuyenMaiDangApDung = null;

    private double tongTien = 0;
    private double tienGiamGia = 0;
    private final NumberFormat currencyVN = NumberFormat.getCurrencyInstance(Locale.of("vi", "VN"));

    public QuanLyDonHangPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(MAIN_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("LẬP ĐƠN HÀNG TẠI QUẦY");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(SIDEBAR_BG);
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(20, 0));
        centerPanel.setBackground(MAIN_BG);
        centerPanel.add(buildProductSelectionPanel(), BorderLayout.WEST);
        centerPanel.add(buildCartAndCheckoutPanel(), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    // =========================================================
    // 1. PANEL TÌM KIẾM SẢN PHẨM
    // =========================================================
    private JPanel buildProductSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ROW_WHITE);
        panel.setPreferredSize(new Dimension(350, 0));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "🔍 Tìm Kiếm Sản Phẩm"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        txtTimKiem = new JTextField();
        JButton btnTimKiem = new JButton("Tìm");
        btnTimKiem.setBackground(BRAND_GOLD);
        btnTimKiem.setForeground(Color.BLACK); // YÊU CẦU 1: CHỮ ĐEN
        
        JPanel pnlSearch = new JPanel(new BorderLayout(5, 0));
        pnlSearch.add(txtTimKiem, BorderLayout.CENTER);
        pnlSearch.add(btnTimKiem, BorderLayout.EAST);
        
        gbc.gridy = 0; panel.add(new JLabel("Mã hoặc Tên SP:"), gbc);
        gbc.gridy = 1; panel.add(pnlSearch, gbc);

        // HIỂN THỊ ẢNH SP
        lblAnhSP = new JLabel("", SwingConstants.CENTER);
        lblAnhSP.setPreferredSize(new Dimension(150, 150));
        lblAnhSP.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        gbc.gridy = 2; gbc.insets = new Insets(15, 10, 10, 10);
        panel.add(lblAnhSP, gbc);

        lblHienThiTenSP = new JLabel("Tên: ---");
        lblHienThiTenSP.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridy = 3; gbc.insets = new Insets(5, 10, 2, 10);
        panel.add(lblHienThiTenSP, gbc);

        lblHienThiGiaSP = new JLabel("Giá: 0đ");
        lblHienThiGiaSP.setForeground(Color.RED);
        gbc.gridy = 4; panel.add(lblHienThiGiaSP, gbc);

        lblHienThiTonKho = new JLabel("Tồn kho: 0");
        gbc.gridy = 5; panel.add(lblHienThiTonKho, gbc);

        gbc.gridy = 6; panel.add(new JLabel("Số Lượng mua:"), gbc);
        txtSoLuong = new JTextField("1");
        gbc.gridy = 7; panel.add(txtSoLuong, gbc);

        JButton btnAdd = new JButton("THÊM VÀO ĐƠN HÀNG");
        btnAdd.setBackground(new Color(40, 167, 69)); 
        btnAdd.setForeground(Color.BLACK); // YÊU CẦU 1: CHỮ ĐEN
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridy = 8; gbc.insets = new Insets(15, 10, 10, 10);
        panel.add(btnAdd, gbc);

        btnTimKiem.addActionListener(e -> timKiemSanPham());
        btnAdd.addActionListener(e -> themSanPhamVaoGio());

        gbc.gridy = 9; gbc.weighty = 1.0; panel.add(new JLabel(""), gbc);
        return panel;
    }

    // =========================================================
    // 2. PANEL GIỎ HÀNG VÀ THANH TOÁN
    // =========================================================
    private JPanel buildCartAndCheckoutPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(MAIN_BG);

        String[] columns = {"Mã SP", "Tên SP", "Đơn Giá", "Số Lượng", "Thành Tiền"};
        cartTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
            @Override public Class<?> getColumnClass(int col) {
                if (col == 2 || col == 4) return Double.class;
                if (col == 3) return Integer.class;
                return String.class;
            }
        };
        cartTable = new JTable(cartTableModel);
        cartTable.setRowHeight(30);
        
        DefaultTableCellRenderer currencyRenderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Double) value = currencyVN.format(value);
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
        cartTable.getColumnModel().getColumn(2).setCellRenderer(currencyRenderer);
        cartTable.getColumnModel().getColumn(4).setCellRenderer(currencyRenderer);

        panel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlBottom.setBackground(MAIN_BG);
        JPanel actionPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        actionPanel.setBackground(MAIN_BG);

        // ==== KHUYẾN MÃI ====
        JPanel promoPanel = new JPanel(new BorderLayout(5, 0));
        txtKhuyenMai = new JTextField();
        JButton btnKhuyenMai = new JButton("Áp Dụng KM");
        btnKhuyenMai.setBackground(BRAND_GOLD);
        btnKhuyenMai.setForeground(Color.BLACK); // YÊU CẦU 1: CHỮ ĐEN
        promoPanel.add(new JLabel("Mã KM: "), BorderLayout.WEST);
        promoPanel.add(txtKhuyenMai, BorderLayout.CENTER);
        promoPanel.add(btnKhuyenMai, BorderLayout.EAST);
        btnKhuyenMai.addActionListener(e -> apDungKhuyenMai());

        // ==== KHÁCH HÀNG ====
        JPanel memberPanel = new JPanel(new BorderLayout(5, 0));
        txtSdtKhachHang = new JTextField();
        JButton btnTichDiem = new JButton("Tìm KH");
        btnTichDiem.setBackground(BRAND_GOLD);
        btnTichDiem.setForeground(Color.BLACK); // YÊU CẦU 1: CHỮ ĐEN
        memberPanel.add(new JLabel("SĐT KH: "), BorderLayout.WEST);
        memberPanel.add(txtSdtKhachHang, BorderLayout.CENTER);
        memberPanel.add(btnTichDiem, BorderLayout.EAST);
        btnTichDiem.addActionListener(e -> xuLyTimKhachHang());
        
        actionPanel.add(promoPanel);
        actionPanel.add(memberPanel);

        // ==== TỔNG KẾT ====
        JPanel totalPanel = new JPanel(new GridLayout(4, 1, 0, 5));
        totalPanel.setBackground(ROW_WHITE);
        totalPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        lblTongTien = new JLabel("Tổng tiền: 0đ", SwingConstants.RIGHT);
        lblGiamGia = new JLabel("Giảm giá: -0đ", SwingConstants.RIGHT);
        lblThanhTien = new JLabel("THANH TOÁN: 0đ", SwingConstants.RIGHT);
        lblThanhTien.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblThanhTien.setForeground(Color.RED);

        JButton btnCheckout = new JButton("THANH TOÁN & IN PDF");
        btnCheckout.setBackground(SIDEBAR_BG);
        btnCheckout.setForeground(Color.BLACK);
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCheckout.addActionListener(e -> xuLyThanhToan());

        totalPanel.add(lblTongTien);
        totalPanel.add(lblGiamGia);
        totalPanel.add(lblThanhTien);
        totalPanel.add(btnCheckout);

        pnlBottom.add(actionPanel);
        pnlBottom.add(totalPanel);
        panel.add(pnlBottom, BorderLayout.SOUTH);

        return panel;
    }

    // =========================================================
    // 3. LOGIC NGHIỆP VỤ SẢN PHẨM (YÊU CẦU 2)
    // =========================================================
    private void timKiemSanPham() {
        String key = txtTimKiem.getText().trim();
        if(key.isEmpty()) { JOptionPane.showMessageDialog(this, "Vui lòng nhập mã hoặc tên SP!"); return; }

        SanPhamDAO dao = new SanPhamDAO();
        SanPham sp = dao.timKiemSanPham(key);

        if (sp != null) {
            currentMaSP = sp.getMaSP();
            currentTenSP = sp.getTenSP();
            currentGiaSP = sp.getGiaBan();
            currentTonKho = sp.getSoLuongTon(); // Lưu số lượng tồn kho

            lblHienThiTenSP.setText("Tên: " + currentTenSP);
            lblHienThiGiaSP.setText("Giá: " + currencyVN.format(currentGiaSP));
            lblHienThiTonKho.setText("Tồn kho: " + currentTonKho);
            lblAnhSP.setIcon(loadAndResizeImage(sp.getHinhAnh(), 150, 150)); // Hiện ảnh
            txtSoLuong.setText("1");
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm!");
            lblAnhSP.setIcon(null);
            currentTonKho = 0;
        }
    }

    private ImageIcon loadAndResizeImage(String imgName, int w, int h) {
        try {
            ImageIcon icon = new ImageIcon("src/assets/product_images/" + imgName);
            return new ImageIcon(icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        } catch (Exception e) { return null; }
    }

    private void themSanPhamVaoGio() {
        if (currentMaSP.isEmpty()) { JOptionPane.showMessageDialog(this, "Vui lòng tìm và chọn sản phẩm trước!"); return; }
        try {
            int soLuongMua = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuongMua <= 0) { JOptionPane.showMessageDialog(this, "Số lượng phải > 0!"); return; }
            
            // YÊU CẦU 2: KIỂM TRA TỒN KHO
            if (soLuongMua > currentTonKho) {
                JOptionPane.showMessageDialog(this, "Chỉ còn " + currentTonKho + " sản phẩm trong kho. Vui lòng nhập lại!", "Lỗi kho", JOptionPane.WARNING_MESSAGE);
                return;
            }

            cartTableModel.addRow(new Object[]{ currentMaSP, currentTenSP, currentGiaSP, soLuongMua, currentGiaSP * soLuongMua });
            capNhatTongTien();

            // Trừ tạm tồn kho trên giao diện đề phòng ấn liên tục
            currentTonKho -= soLuongMua;
            lblHienThiTonKho.setText("Tồn kho (Tạm tính): " + currentTonKho);

        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Số lượng nhập không hợp lệ!"); }
    }

    // =========================================================
    // 4. LOGIC TÍCH ĐIỂM VÀ KHÁCH HÀNG (YÊU CẦU 3)
    // =========================================================
    private void xuLyTimKhachHang() {
        String sdt = txtSdtKhachHang.getText().trim();
        
        // Bỏ trống => Không tích điểm
        if(sdt.isEmpty()) {
            currentMaKH = null;
            JOptionPane.showMessageDialog(this, "Khách hàng không tích điểm (Vãng lai).");
            return;
        }

        KhachHangDAO khDao = new KhachHangDAO();
        KhachHang kh = khDao.getKhachHangBySDT(sdt);

        if (kh != null) {
            currentMaKH = kh.getMaKH();
            JOptionPane.showMessageDialog(this, "Khách hàng quen: " + kh.getTenKH() + "\nĐiểm tích lũy hiện có: " + kh.getDiemTichLuy());
        } else {
            hienThiFormThemKhachHang(sdt);
        }
    }

    private void hienThiFormThemKhachHang(String sdtSanCo) {
        JTextField txtTenKH = new JTextField();
        JTextField txtSdt = new JTextField(sdtSanCo); txtSdt.setEditable(false);
        Object[] msg = { "Số Điện Thoại:", txtSdt, "Tên Khách Hàng:", txtTenKH };

        int result = JOptionPane.showConfirmDialog(this, msg, "Đăng Ký Khách Hàng Mới", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            if(txtTenKH.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Tên không được để trống!"); return; }
            
            String newMaKH = "KH" + System.currentTimeMillis();
            // Khởi tạo KhachHang dựa trên model của bạn (có email và tenDangNhap để trống)
            KhachHang newKH = new KhachHang(newMaKH, txtTenKH.getText().trim(), sdtSanCo, 0, "", "");
            
            if (new KhachHangDAO().themKhachHangMoi(newKH)) {
                currentMaKH = newMaKH;
                JOptionPane.showMessageDialog(this, "Đăng ký thành công! Hệ thống sẽ tích điểm cho: " + newKH.getTenKH());
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL, không thể tạo khách hàng!");
            }
        } else {
            // Khách từ chối đăng ký -> Bán vãng lai
            txtSdtKhachHang.setText("");
            currentMaKH = null;
        }
    }

    // =========================================================
    // 5. KHUYẾN MÁI, TÍNH TIỀN, XUẤT HÓA ĐƠN
    // =========================================================
    private void apDungKhuyenMai() {
        String maKM = txtKhuyenMai.getText().trim();
        if(maKM.isEmpty()) { khuyenMaiDangApDung = null; capNhatTongTien(); return; }

        KhuyenMai km = new KhuyenMaiDAO().getKhuyenMaiHopLe(maKM);
        if (km != null) {
            khuyenMaiDangApDung = km;
            JOptionPane.showMessageDialog(this, "Đã áp dụng: " + km.getTenKM());
        } else {
            khuyenMaiDangApDung = null;
            JOptionPane.showMessageDialog(this, "Mã không hợp lệ hoặc đã hết hạn!");
        }
        capNhatTongTien();
    }

    private void capNhatTongTien() {
        tongTien = 0; tienGiamGia = 0;
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            String maSP = cartTableModel.getValueAt(i, 0).toString();
            double donGia = (Double) cartTableModel.getValueAt(i, 2);
            int sl = (Integer) cartTableModel.getValueAt(i, 3);
            double tien = donGia * sl;
            tongTien += tien;

            if (khuyenMaiDangApDung != null && khuyenMaiDangApDung.getChiTietGiamGia().containsKey(maSP)) {
                tienGiamGia += tien * (khuyenMaiDangApDung.getChiTietGiamGia().get(maSP) / 100.0);
            }
        }
        double thanhToan = Math.max(0, tongTien - tienGiamGia);

        lblTongTien.setText("Tổng tiền: " + currencyVN.format(tongTien));
        lblGiamGia.setText("Giảm giá: -" + currencyVN.format(tienGiamGia));
        lblThanhTien.setText("THANH TOÁN: " + currencyVN.format(thanhToan));
    }

    private void xuLyThanhToan() {
        if (cartTableModel.getRowCount() == 0) { JOptionPane.showMessageDialog(this, "Giỏ hàng trống!"); return; }

        double thanhToanCuoi = Math.max(0, tongTien - tienGiamGia);
        int diemThuong = (int) (thanhToanCuoi * 0.01); // 1% giá trị hóa đơn

        DonHang dh = new DonHang();
        dh.setMaDH("DH" + System.currentTimeMillis());
        dh.setMaKH(currentMaKH);
        dh.setMaNV("NV01"); // Tạm gán mã nhân viên
        dh.setMaGiamGia(khuyenMaiDangApDung != null ? khuyenMaiDangApDung.getMaKM() : null);
        dh.setTongTien(thanhToanCuoi);
        dh.setHinhThucThanhToan("Tiền mặt");
        dh.setDiemThuong(diemThuong);
        dh.setDiemSuDung(0);
        dh.setGhiChu("Bán tại quầy");

        List<ChiTietDonHang> dsCTDH = new ArrayList<>();
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            dsCTDH.add(new ChiTietDonHang(dh.getMaDH(), cartTableModel.getValueAt(i, 0).toString(), 
                       (Integer) cartTableModel.getValueAt(i, 3), (Double) cartTableModel.getValueAt(i, 2)));
        }

        String billText = taoChuoiHoaDon(dh);
        JTextArea textArea = new JTextArea(billText); textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        if (JOptionPane.showConfirmDialog(this, new JScrollPane(textArea), "Xem Trước Hóa Đơn", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if(new DonHangDAO().taoDonHangTaiQuay(dh, dsCTDH)) {
                xuatFilePDF(billText, dh.getMaDH());
                
                // Reset Giao diện
                cartTableModel.setRowCount(0); 
                khuyenMaiDangApDung = null; 
                currentMaKH = null;
                txtKhuyenMai.setText(""); 
                txtSdtKhachHang.setText("");
                lblAnhSP.setIcon(null);
                lblHienThiTenSP.setText("Tên: ---");
                lblHienThiGiaSP.setText("Giá: 0đ");
                lblHienThiTonKho.setText("Tồn kho: 0");
                capNhatTongTien();
                JOptionPane.showMessageDialog(this, "Giao dịch hoàn tất! (Khách hàng nhận được: " + diemThuong + " điểm)");
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL khi lưu hóa đơn!");
            }
        }
    }

    private String taoChuoiHoaDon(DonHang dh) {
        StringBuilder sb = new StringBuilder();
        sb.append("============= BEAUTY SHOP =============\n");
        sb.append("Mã ĐH: ").append(dh.getMaDH()).append("\nNgày: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append("\n");
        sb.append("Khách Hàng: ").append(dh.getMaKH() != null ? dh.getMaKH() : "Khách Vãng Lai").append("\n");
        sb.append("---------------------------------------\n");
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            sb.append(cartTableModel.getValueAt(i, 1)).append("\n  SL: ").append(cartTableModel.getValueAt(i, 3))
              .append(" - ").append(currencyVN.format(cartTableModel.getValueAt(i, 4))).append("\n");
        }
        sb.append("---------------------------------------\nTổng tiền: ").append(currencyVN.format(tongTien))
          .append("\nGiảm: -").append(currencyVN.format(tienGiamGia)).append("\nTHANH TOÁN: ").append(currencyVN.format(dh.getTongTien())).append("\n");
        return sb.toString();
    }

    private void xuatFilePDF(String noidung, String maDH) {
        try {
            JFileChooser fc = new JFileChooser(); fc.setSelectedFile(new File("HoaDon_" + maDH + ".pdf"));
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                Document doc = new Document(); PdfWriter.getInstance(doc, new FileOutputStream(fc.getSelectedFile()));
                doc.open(); doc.add(new Paragraph(noidung, FontFactory.getFont(FontFactory.COURIER, 12))); doc.close();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}