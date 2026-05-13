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

    private final Color MAIN_BG = new Color(244, 247, 246);
    private final Color BRAND_GOLD = new Color(212, 175, 55);
    private final Color SIDEBAR_BG = new Color(5, 5, 5);
    private final Color ROW_WHITE = Color.WHITE;

    private DefaultTableModel cartTableModel;
    private JTable cartTable;
    private JTextField txtTimKiem, txtKhuyenMai, txtSdtKhachHang;
    
    private JLabel lblTongTien, lblGiamGia, lblThanhTien; 
    
    private JPanel pnlSearchResults; 

    private String currentMaKH = null; 
    private KhuyenMai khuyenMaiDangApDung = null;

    private double tongTien = 0;
    private double tienGiamGia = 0;
    
    // Đã sửa lại thành new Locale để tương thích mọi phiên bản Java
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

    private JPanel buildProductSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(MAIN_BG);
        panel.setPreferredSize(new Dimension(380, 0));

        JPanel pnlSearchTop = new JPanel(new BorderLayout(5, 0));
        pnlSearchTop.setBackground(MAIN_BG);
        pnlSearchTop.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "🔍 Tìm Kiếm Sản Phẩm"));

        txtTimKiem = new JTextField();
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTimKiem.setPreferredSize(new Dimension(200, 35));
        txtTimKiem.setToolTipText("Nhập mã SP (VD: SP01, SP02) hoặc Tên SP");

        JButton btnTimKiem = new JButton("Tìm");
        btnTimKiem.setBackground(BRAND_GOLD);
        btnTimKiem.setForeground(Color.BLACK); 
        btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlSearchTop.add(txtTimKiem, BorderLayout.CENTER);
        pnlSearchTop.add(btnTimKiem, BorderLayout.EAST);
        
        pnlSearchResults = new JPanel();
        pnlSearchResults.setLayout(new BoxLayout(pnlSearchResults, BoxLayout.Y_AXIS));
        pnlSearchResults.setBackground(ROW_WHITE);

        JScrollPane scrollPane = new JScrollPane(pnlSearchResults);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        panel.add(pnlSearchTop, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        btnTimKiem.addActionListener(e -> timKiemSanPham());
        txtTimKiem.addActionListener(e -> timKiemSanPham()); 

        return panel;
    }

    private void timKiemSanPham() {
        String key = txtTimKiem.getText().trim();
        if(key.isEmpty()) { 
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã hoặc tên SP!"); 
            return; 
        }

        pnlSearchResults.removeAll();
        SanPhamDAO dao = new SanPhamDAO();

        if(key.contains(",")) {
            String[] dsMa = key.split(",");
            boolean foundAny = false;
            for(String ma : dsMa) {
                SanPham sp = dao.timKiemSanPham(ma.trim());
                if(sp != null) {
                    pnlSearchResults.add(createProductCard(sp));
                    foundAny = true;
                }
            }
            if(!foundAny) showNoResult();
        } else {
            SanPham sp = dao.timKiemSanPham(key);
            if (sp != null) {
                pnlSearchResults.add(createProductCard(sp));
            } else {
                showNoResult();
            }
        }
        
        pnlSearchResults.revalidate();
        pnlSearchResults.repaint();
    }

    private void showNoResult() {
        JLabel lblNoResult = new JLabel("Không tìm thấy sản phẩm nào!");
        lblNoResult.setForeground(Color.RED);
        lblNoResult.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNoResult.setBorder(new EmptyBorder(20, 0, 0, 0));
        pnlSearchResults.add(lblNoResult);
    }

    private JPanel createProductCard(SanPham sp) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(ROW_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(400, 150));

        JLabel lblImg = new JLabel();
        lblImg.setIcon(loadAndResizeImage(sp.getHinhAnh(), 90, 90));
        lblImg.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.add(lblImg, BorderLayout.WEST);

        JPanel pnlInfo = new JPanel(new GridLayout(4, 1));
        pnlInfo.setBackground(ROW_WHITE);
        
        JLabel lblTen = new JLabel(sp.getTenSP() + " (" + sp.getMaSP() + ")");
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel lblGia = new JLabel("Giá: " + currencyVN.format(sp.getGiaBan()));
        lblGia.setForeground(Color.RED);
        
        JLabel lblChiTiet = new JLabel("Size: " + sp.getKichCo() + " | Màu: " + sp.getMauSac());
        lblChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblChiTiet.setForeground(Color.DARK_GRAY);

        JLabel lblTonKho = new JLabel("Tồn kho: " + sp.getSoLuongTon());
        lblTonKho.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        pnlInfo.add(lblTen);
        pnlInfo.add(lblGia);
        pnlInfo.add(lblChiTiet);
        pnlInfo.add(lblTonKho);
        card.add(pnlInfo, BorderLayout.CENTER);

        JPanel pnlAction = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlAction.setBackground(ROW_WHITE);
        
        pnlAction.add(new JLabel("SL:"));
        
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, sp.getSoLuongTon() > 0 ? sp.getSoLuongTon() : 1, 1);
        JSpinner spnSoLuong = new JSpinner(spinnerModel);
        spnSoLuong.setPreferredSize(new Dimension(60, 30));

        JButton btnAdd = new JButton("Thêm");
        btnAdd.setBackground(new Color(40, 167, 69));
        btnAdd.setForeground(Color.BLACK);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnAdd.addActionListener(e -> {
            int soLuongMua = (int) spnSoLuong.getValue();
            if (soLuongMua > sp.getSoLuongTon()) {
                JOptionPane.showMessageDialog(this, "Không đủ hàng trong kho!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // YÊU CẦU 2: Gắn Size và Màu vào ngay phía sau Tên SP
            String tenSpCoChiTiet = sp.getTenSP() + " (Size: " + sp.getKichCo() + " - Màu: " + sp.getMauSac() + ")";

            cartTableModel.addRow(new Object[]{ 
                sp.getMaSP(), tenSpCoChiTiet, sp.getGiaBan(), soLuongMua, sp.getGiaBan() * soLuongMua 
            });
            capNhatTongTien();

            int tonKhoMoi = sp.getSoLuongTon() - soLuongMua;
            sp.setSoLuongTon(tonKhoMoi);
            lblTonKho.setText("Tồn kho: " + tonKhoMoi);
            spinnerModel.setMaximum(tonKhoMoi > 0 ? tonKhoMoi : 1);
            if(tonKhoMoi == 0) spinnerModel.setValue(0);
        });

        if (sp.getSoLuongTon() <= 0) {
            btnAdd.setEnabled(false);
            btnAdd.setText("Hết hàng");
            btnAdd.setBackground(Color.GRAY);
        }

        pnlAction.add(spnSoLuong);
        pnlAction.add(btnAdd);
        
        card.add(pnlAction, BorderLayout.SOUTH);

        return card;
    }

    private ImageIcon loadAndResizeImage(String imgName, int w, int h) {
        try {
            ImageIcon icon = new ImageIcon("src/assets/product_images/" + imgName);
            if(icon.getImageLoadStatus() == MediaTracker.ERRORED) {
                 return new ImageIcon(new ImageIcon("src/assets/product_images/no_image.png").getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
            }
            return new ImageIcon(icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        } catch (Exception e) { return null; }
    }

    private JPanel buildCartAndCheckoutPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(MAIN_BG);

        String[] columns = {"Mã SP", "Tên SP", "Đơn Giá", "Số Lượng", "Thành Tiền"};
        cartTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        cartTable = new JTable(cartTableModel);
        cartTable.setRowHeight(30);
        
        // Tùy chỉnh độ rộng cột Tên SP to ra để chứa được Size và Màu
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(250);

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

        JPanel promoPanel = new JPanel(new BorderLayout(5, 0));
        txtKhuyenMai = new JTextField();
        JButton btnKhuyenMai = new JButton("Áp Dụng KM");
        btnKhuyenMai.setBackground(BRAND_GOLD);
        btnKhuyenMai.setForeground(Color.BLACK); 
        promoPanel.add(new JLabel("Mã KM: "), BorderLayout.WEST);
        promoPanel.add(txtKhuyenMai, BorderLayout.CENTER);
        promoPanel.add(btnKhuyenMai, BorderLayout.EAST);
        btnKhuyenMai.addActionListener(e -> apDungKhuyenMai());

        JPanel memberPanel = new JPanel(new BorderLayout(5, 0));
        txtSdtKhachHang = new JTextField();
        JButton btnTichDiem = new JButton("Tìm KH");
        btnTichDiem.setBackground(BRAND_GOLD);
        btnTichDiem.setForeground(Color.BLACK); 
        memberPanel.add(new JLabel("SĐT KH: "), BorderLayout.WEST);
        memberPanel.add(txtSdtKhachHang, BorderLayout.CENTER);
        memberPanel.add(btnTichDiem, BorderLayout.EAST);
        btnTichDiem.addActionListener(e -> xuLyTimKhachHang());
        
        actionPanel.add(promoPanel);
        actionPanel.add(memberPanel);

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

    private void xuLyTimKhachHang() {
        String sdt = txtSdtKhachHang.getText().trim();
        if(sdt.isEmpty()) {
            currentMaKH = null;
            JOptionPane.showMessageDialog(this, "Khách hàng không tích điểm (Vãng lai).");
            return;
        }

        KhachHang kh = new KhachHangDAO().getKhachHangBySDT(sdt);
        if (kh != null) {
            currentMaKH = kh.getMaKH();
            JOptionPane.showMessageDialog(this, "Khách hàng quen: " + kh.getTenKH() + "\nĐiểm: " + kh.getDiemTichLuy());
        } else {
            JTextField txtTenKH = new JTextField();
            Object[] msg = { "SĐT:", new JTextField(sdt){{setEditable(false);}}, "Tên KH:", txtTenKH };
            if (JOptionPane.showConfirmDialog(this, msg, "Đăng Ký KH", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                String newMaKH = "KH" + System.currentTimeMillis();
                KhachHang newKH = new KhachHang(newMaKH, txtTenKH.getText().trim(), sdt, 0, "", "");
                if (new KhachHangDAO().themKhachHangMoi(newKH)) {
                    currentMaKH = newMaKH;
                    JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
                }
            } else {
                txtSdtKhachHang.setText(""); currentMaKH = null;
            }
        }
    }

    private void apDungKhuyenMai() {
        String maKM = txtKhuyenMai.getText().trim();
        if(maKM.isEmpty()) { khuyenMaiDangApDung = null; capNhatTongTien(); return; }

        KhuyenMai km = new KhuyenMaiDAO().getKhuyenMaiHopLe(maKM);
        if (km != null) {
            khuyenMaiDangApDung = km;
            JOptionPane.showMessageDialog(this, "Đã lưu mã: " + km.getTenKM() + "\nHệ thống sẽ tự tính giảm giá dựa trên tổng tiền.");
        } else {
            khuyenMaiDangApDung = null;
            JOptionPane.showMessageDialog(this, "Mã không hợp lệ hoặc đã hết lượt dùng!");
        }
        capNhatTongTien();
    }

    private void capNhatTongTien() {
        tongTien = 0; 
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            tongTien += (Double) cartTableModel.getValueAt(i, 2) * (Integer) cartTableModel.getValueAt(i, 3);
        }

        tienGiamGia = 0;
        if (khuyenMaiDangApDung != null) {
            if (tongTien >= khuyenMaiDangApDung.getGiaTriToiThieu()) {
                tienGiamGia = tongTien * (khuyenMaiDangApDung.getPhanTramGiam() / 100.0);
                if(khuyenMaiDangApDung.getGiamToiDa() > 0 && tienGiamGia > khuyenMaiDangApDung.getGiamToiDa()) {
                    tienGiamGia = khuyenMaiDangApDung.getGiamToiDa();
                }
            } else {
                khuyenMaiDangApDung = null;
                txtKhuyenMai.setText("");
                JOptionPane.showMessageDialog(this, "Đơn hàng chưa đạt giá trị tối thiểu để áp dụng mã này!");
            }
        }
        
        double thanhToan = Math.max(0, tongTien - tienGiamGia);
        lblTongTien.setText("Tổng tiền: " + currencyVN.format(tongTien));
        lblGiamGia.setText("Giảm giá: -" + currencyVN.format(tienGiamGia));
        lblThanhTien.setText("THANH TOÁN: " + currencyVN.format(thanhToan));
    }

    private void xuLyThanhToan() {
        if (cartTableModel.getRowCount() == 0) { JOptionPane.showMessageDialog(this, "Giỏ hàng trống!"); return; }

        // YÊU CẦU 1: Hộp thoại nhập Mã Nhân Viên để Test lưu Database
        String inputMaNV = JOptionPane.showInputDialog(this, "Nhập Mã Nhân Viên (Đang có trong Database) để test:", "NV01");
        if (inputMaNV == null || inputMaNV.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Đã hủy thanh toán vì thiếu Mã Nhân Viên!");
            return;
        }

        double thanhToanCuoi = Math.max(0, tongTien - tienGiamGia);
        int diemThuong = (int) (thanhToanCuoi * 0.01); 

        DonHang dh = new DonHang();
        dh.setMaDH("DH" + System.currentTimeMillis());
        dh.setMaKH(currentMaKH);
        dh.setMaNV(inputMaNV.trim()); // Gắn mã NV vừa nhập
        dh.setMaGiamGia(khuyenMaiDangApDung != null ? khuyenMaiDangApDung.getMaKM() : null);
        dh.setTongTien(thanhToanCuoi);
        dh.setHinhThucThanhToan("Tiền mặt");
        dh.setDiemThuong(diemThuong);
        dh.setDiemSuDung(0);
        dh.setGhiChu("Bán tại quầy");

        List<ChiTietDonHang> dsCTDH = new ArrayList<>();
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            // Lấy lại mã SP gốc ở cột 0
            dsCTDH.add(new ChiTietDonHang(dh.getMaDH(), cartTableModel.getValueAt(i, 0).toString(), 
                       (Integer) cartTableModel.getValueAt(i, 3), (Double) cartTableModel.getValueAt(i, 2)));
        }

        JEditorPane previewPane = new JEditorPane("text/html", taoChuoiHTMLHoaDon(dh));
        previewPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(previewPane);
        scrollPane.setPreferredSize(new Dimension(550, 600));
        
        if (JOptionPane.showConfirmDialog(this, scrollPane, "XÁC NHẬN HÓA ĐƠN", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.YES_OPTION) {
            
            if(new DonHangDAO().taoDonHangTaiQuay(dh, dsCTDH)) {
                xuatFilePDF(taoChuoiHoadonText(dh), dh.getMaDH());
                
                cartTableModel.setRowCount(0); 
                pnlSearchResults.removeAll(); pnlSearchResults.revalidate(); pnlSearchResults.repaint();
                khuyenMaiDangApDung = null; currentMaKH = null;
                txtKhuyenMai.setText(""); txtSdtKhachHang.setText(""); txtTimKiem.setText("");
                capNhatTongTien();
                JOptionPane.showMessageDialog(this, "Lưu dữ liệu và Xuất Hóa đơn thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL! Vui lòng kiểm tra xem Mã NV có thực sự tồn tại chưa.");
            }
        }
    }

    private String taoChuoiHTMLHoaDon(DonHang dh) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='font-family: Arial, sans-serif; padding: 10px; color: #333;'>");
        sb.append("<h2 style='text-align: center; color: #000; margin-bottom: 5px;'>BEAUTY SHOP</h2>");
        sb.append("<h4 style='text-align: center; color: #555; margin-top: 0;'>HÓA ĐƠN BÁN HÀNG</h4>");
        sb.append("<hr style='border: 1px dashed #ccc;'/>");
        sb.append("<p style='font-size: 13px;'><b>Mã đơn:</b> ").append(dh.getMaDH()).append("<br>");
        sb.append("<b>Mã thu ngân:</b> ").append(dh.getMaNV()).append("<br>");
        sb.append("<b>Ngày:</b> ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append("<br>");
        sb.append("<b>Khách:</b> ").append(dh.getMaKH() != null ? dh.getMaKH() : "Khách vãng lai").append("</p>");
        
        sb.append("<table width='100%' style='border-collapse: collapse; margin-top: 10px; font-size: 13px;'>");
        sb.append("<tr style='background-color: #f9f9f9; border-bottom: 2px solid #ddd;'>");
        sb.append("<th align='left' style='padding: 6px;'>Sản phẩm</th>");
        sb.append("<th align='center'>SL</th>");
        sb.append("<th align='right'>Thành tiền</th></tr>");

        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            sb.append("<tr>");
            // Cột 1 đang chứa tên đã được ghép size và màu ở trên
            sb.append("<td style='padding: 6px; border-bottom: 1px solid #eee;'>").append(cartTableModel.getValueAt(i, 1)).append("</td>");
            sb.append("<td align='center' style='border-bottom: 1px solid #eee;'>").append(cartTableModel.getValueAt(i, 3)).append("</td>");
            sb.append("<td align='right' style='border-bottom: 1px solid #eee;'>").append(currencyVN.format(cartTableModel.getValueAt(i, 4))).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        sb.append("<br><p style='text-align: right; font-size: 14px;'>Tổng cộng: <b>").append(currencyVN.format(tongTien)).append("</b><br>");
        sb.append("Giảm giá: <b style='color: #e74c3c;'>-").append(currencyVN.format(tienGiamGia)).append("</b><br></p>");
        sb.append("<h3 style='text-align: right; color: #2ecc71; margin-top: 0;'>THANH TOÁN: ").append(currencyVN.format(dh.getTongTien())).append("</h3>");
        sb.append("<hr style='border: 1px dashed #ccc;'/>");
        sb.append("<p style='text-align: center; font-style: italic; font-size: 12px;'>Cảm ơn quý khách và hẹn gặp lại!</p>");
        sb.append("</body></html>");
        return sb.toString();
    }

    private String taoChuoiHoadonText(DonHang dh) {
        StringBuilder sb = new StringBuilder();
        sb.append("============= BEAUTY SHOP =============\n");
        sb.append("Mã ĐH: ").append(dh.getMaDH()).append("\nNgày: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append("\n");
        sb.append("Thu Ngân: ").append(dh.getMaNV()).append("\n");
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