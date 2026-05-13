package src.view;

import src.dao.SanPhamDAO;
import src.model.DonHang;
import src.model.SanPham;
import src.database.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;

public class DoiTraPanel extends JPanel {

    // ===== MÀU GIAO DIỆN =====
    private final Color MAIN_BG = new Color(244, 247, 246);
    private final Color BRAND_GOLD = new Color(212, 175, 55);
    private final Color SIDEBAR_BG = new Color(5, 5, 5);
    private final Color ROW_WHITE = Color.WHITE;

    private final NumberFormat currencyVN = NumberFormat.getCurrencyInstance(Locale.of("vi", "VN"));

    // ===== COMPONENTS =====
    private DefaultTableModel tableModel;
    private JTable table;
    
    private JTextField txtTimDH, txtTimSpMoi;
    private JLabel lblDonHangInfo, lblSpCuInfo, lblSpMoiInfo;
    private JRadioButton rdoTraHang, rdoDoiHang;
    private JPanel pnlTimSpMoi;
    private JComboBox<String> cmbLyDo;
    private JSpinner spnSoLuongMoi; // Ô chọn số lượng cho SP mới

    // ===== LƯU TRỮ =====
    private DonHang currentDH = null;
    private SanPham currentSP_Moi = null;
    private double tongTienTraLai = 0;
    private int soLuongMonTraLai = 0;

    public DoiTraPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(MAIN_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("XỬ LÝ ĐỔI TRẢ ĐƠN HÀNG");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(SIDEBAR_BG);
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(20, 0));
        centerPanel.setBackground(MAIN_BG);
        
        centerPanel.add(buildFormPanel(), BorderLayout.WEST);
        centerPanel.add(buildTablePanel(), BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ROW_WHITE);
        panel.setPreferredSize(new Dimension(450, 0));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "📝 Form Xử Lý"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;

        // 1. Tìm đơn hàng
        JPanel pnlTimDH = new JPanel(new BorderLayout(5, 0));
        pnlTimDH.setBackground(ROW_WHITE);
        txtTimDH = new JTextField(); txtTimDH.setPreferredSize(new Dimension(150, 32));
        JButton btnTimDH = new JButton("Tìm Đơn");
        btnTimDH.setBackground(BRAND_GOLD); btnTimDH.setForeground(Color.BLACK);
        pnlTimDH.add(txtTimDH, BorderLayout.CENTER); pnlTimDH.add(btnTimDH, BorderLayout.EAST);
        
        gbc.gridy = 0; panel.add(new JLabel("Nhập Mã Đơn Hàng:"), gbc);
        gbc.gridy = 1; panel.add(pnlTimDH, gbc);

        lblDonHangInfo = new JLabel("Chưa chọn đơn hàng...");
        lblDonHangInfo.setForeground(new Color(100, 100, 100));
        gbc.gridy = 2; panel.add(lblDonHangInfo, gbc);

        // 2. Info chọn bảng
        gbc.gridy = 3; panel.add(new JLabel("Đang chọn từ bảng:"), gbc);
        lblSpCuInfo = new JLabel("<html><i>Hãy tick (☑) và sửa số lượng trên bảng</i></html>");
        lblSpCuInfo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(0, 150, 136)), new EmptyBorder(10, 10, 10, 10)));
        gbc.gridy = 4; panel.add(lblSpCuInfo, gbc);

        // 3. Lý do
        gbc.gridy = 5; panel.add(new JLabel("Lý do Đổi/Trả:"), gbc);
        cmbLyDo = new JComboBox<>(new String[]{"Sản phẩm lỗi do cửa hàng", "Khách đổi ý/không thích", "Đổi size, màu sắc khác", "Lý do khác..."});
        cmbLyDo.setPreferredSize(new Dimension(0, 32)); cmbLyDo.setBackground(Color.WHITE);
        gbc.gridy = 6; panel.add(cmbLyDo, gbc);

        // 4. Hình thức
        gbc.gridy = 7; panel.add(new JLabel("Hình thức xử lý:"), gbc);
        JPanel pnlRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0)); pnlRadio.setBackground(ROW_WHITE);
        rdoTraHang = new JRadioButton("Trả hàng"); rdoTraHang.setBackground(ROW_WHITE);
        rdoDoiHang = new JRadioButton("Đổi SP mới"); rdoDoiHang.setBackground(ROW_WHITE);
        ButtonGroup bg = new ButtonGroup(); bg.add(rdoTraHang); bg.add(rdoDoiHang);
        rdoTraHang.setSelected(true); 
        pnlRadio.add(rdoTraHang); pnlRadio.add(rdoDoiHang);
        gbc.gridy = 8; panel.add(pnlRadio, gbc);

        // 5. Tìm SP Mới + Số lượng lấy
        pnlTimSpMoi = new JPanel(new BorderLayout(5, 5)); pnlTimSpMoi.setBackground(ROW_WHITE);
        JPanel pnlSearchNew = new JPanel(new BorderLayout(5, 0));
        txtTimSpMoi = new JTextField(); txtTimSpMoi.setPreferredSize(new Dimension(100, 32));
        JButton btnTimSpMoi = new JButton("Tìm SP"); btnTimSpMoi.setBackground(SIDEBAR_BG); btnTimSpMoi.setForeground(Color.WHITE);
        pnlSearchNew.add(txtTimSpMoi, BorderLayout.CENTER); pnlSearchNew.add(btnTimSpMoi, BorderLayout.EAST);
        
        JPanel pnlSoLuongMoi = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); pnlSoLuongMoi.setBackground(ROW_WHITE);
        pnlSoLuongMoi.add(new JLabel("Số lượng lấy: "));
        spnSoLuongMoi = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1)); 
        spnSoLuongMoi.setPreferredSize(new Dimension(70, 25));
        pnlSoLuongMoi.add(spnSoLuongMoi);

        lblSpMoiInfo = new JLabel("SP Mới: ..."); lblSpMoiInfo.setForeground(new Color(0, 100, 0));
        pnlTimSpMoi.add(pnlSearchNew, BorderLayout.NORTH); pnlTimSpMoi.add(lblSpMoiInfo, BorderLayout.CENTER); pnlTimSpMoi.add(pnlSoLuongMoi, BorderLayout.SOUTH);
        pnlTimSpMoi.setVisible(false); 
        gbc.gridy = 9; panel.add(pnlTimSpMoi, gbc);

        // 6. Nút Xác nhận
        JPanel pnlBtns = new JPanel(new GridLayout(1, 2, 10, 0)); pnlBtns.setBackground(ROW_WHITE);
        JButton btnXacNhan = new JButton("XÁC NHẬN"); btnXacNhan.setBackground(new Color(220, 53, 69)); btnXacNhan.setForeground(Color.WHITE);
        JButton btnLamMoi = new JButton("Làm Mới"); btnLamMoi.setBackground(Color.LIGHT_GRAY);
        pnlBtns.add(btnLamMoi); pnlBtns.add(btnXacNhan);
        gbc.gridy = 10; gbc.insets = new Insets(15, 10, 10, 10); panel.add(pnlBtns, gbc);
        gbc.gridy = 11; gbc.weighty = 1.0; panel.add(new JLabel(""), gbc);

        rdoTraHang.addActionListener(e -> pnlTimSpMoi.setVisible(false));
        rdoDoiHang.addActionListener(e -> pnlTimSpMoi.setVisible(true));
        btnTimDH.addActionListener(e -> timKiemDonHang());
        btnTimSpMoi.addActionListener(e -> timKiemSanPhamMoi());
        btnLamMoi.addActionListener(e -> lamMoiForm());
        btnXacNhan.addActionListener(e -> xacNhanDoiTra());

        return panel;
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(MAIN_BG);

        String[] cols = {"Chọn", "Mã SP", "Tên SP (Màu-Size)", "SL Mua", "SL Đổi/Trả", "Đơn Giá", "Thành Tiền"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class; 
                if (columnIndex == 3 || columnIndex == 4) return Integer.class;
                if (columnIndex == 5 || columnIndex == 6) return Double.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 || col == 4; // Chỉ cho sửa Checkbox và Cột SL Đổi Trả
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                // LOGIC THÔNG MINH CHO CỘT SỐ LƯỢNG
                if (column == 4) {
                    try {
                        int slNhap = Integer.parseInt(aValue.toString());
                        int slMua = (int) getValueAt(row, 3);
                        
                        if (slNhap < 0) slNhap = 0;
                        if (slNhap > slMua) {
                            slNhap = slMua;
                            JOptionPane.showMessageDialog(null, "Không được nhập quá số lượng đã mua!");
                        }
                        
                        super.setValueAt(slNhap, row, column); // Cập nhật số lượng
                        
                        // Tự động tick hoặc bỏ tick dựa trên số lượng
                        super.setValueAt(slNhap > 0, row, 0);

                        // Tính lại Thành Tiền
                        double donGia = (double) getValueAt(row, 5);
                        super.setValueAt(slNhap * donGia, row, 6);
                        tinhTongTienCacMonDaChon();
                    } catch (Exception ignored) {}
                } 
                // LOGIC KHI CLICK VÀO CHECKBOX
                else if (column == 0) {
                    boolean isChecked = (boolean) aValue;
                    super.setValueAt(isChecked, row, column);
                    
                    int currentSl = (int) getValueAt(row, 4);
                    double donGia = (double) getValueAt(row, 5);
                    
                    if (isChecked && currentSl == 0) {
                        super.setValueAt(1, row, 4); // Nhảy lên 1 khi tick
                        super.setValueAt(1 * donGia, row, 6);
                    } else if (!isChecked) {
                        super.setValueAt(0, row, 4); // Về 0 khi bỏ tick
                        super.setValueAt(0.0, row, 6);
                    }
                    tinhTongTienCacMonDaChon();
                } else {
                    super.setValueAt(aValue, row, column);
                }
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);

        DefaultTableCellRenderer currencyRenderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Double) value = currencyVN.format(value);
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
        table.getColumnModel().getColumn(5).setCellRenderer(currencyRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(currencyRenderer);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Bảng Sản Phẩm (Nhấp đúp vào cột [SL Đổi/Trả] để sửa)"));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void tinhTongTienCacMonDaChon() {
        tongTienTraLai = 0; soLuongMonTraLai = 0;
        for(int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean isChecked = (Boolean) tableModel.getValueAt(i, 0);
            int sl = (int) tableModel.getValueAt(i, 4);
            if(isChecked != null && isChecked && sl > 0) {
                tongTienTraLai += (double) tableModel.getValueAt(i, 6);
                soLuongMonTraLai += sl;
            }
        }
        if(soLuongMonTraLai > 0) {
            lblSpCuInfo.setText(String.format("<html><b>Đang xử lý: %d sản phẩm</b><br><span style='color:red; font-size:12px;'>Giá trị thu hồi: %s</span></html>", 
                    soLuongMonTraLai, currencyVN.format(tongTienTraLai)));
        } else {
            lblSpCuInfo.setText("<html><i>Hãy tick (☑) và sửa số lượng trên bảng</i></html>");
        }
    }

    private void timKiemDonHang() {
        String maDH = txtTimDH.getText().trim();
        if(maDH.isEmpty()) return;
        currentDH = null; 
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT MaDH, TongTien FROM DONHANG WHERE MaDH = ?")) {
             ps.setString(1, maDH);
             ResultSet rs = ps.executeQuery();
             if(rs.next()) {
                 currentDH = new DonHang();
                 currentDH.setMaDH(rs.getString("MaDH"));
                 currentDH.setTongTien(rs.getDouble("TongTien"));
             }
        } catch (Exception e) { e.printStackTrace(); }

        if(currentDH != null) {
            lblDonHangInfo.setText("Tổng tiền đơn: " + currencyVN.format(currentDH.getTongTien()));
            loadChiTietDonHang(maDH);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy đơn hàng!"); lamMoiForm();
        }
    }

    private void loadChiTietDonHang(String maDH) {
        tableModel.setRowCount(0); 
        String sql = "SELECT ct.MaSP, sp.TenSP, sp.MauSac, sp.KichCo, ct.SoLuong, ct.DonGia " +
                     "FROM CHITIET_DONHANG ct JOIN SANPHAM sp ON ct.MaSP = sp.MaSP WHERE ct.MaDH = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             ps.setString(1, maDH);
             ResultSet rs = ps.executeQuery();
             while(rs.next()) {
                 String tenSp = rs.getString("TenSP") + " (" + rs.getString("MauSac") + " - " + rs.getString("KichCo") + ")";
                 int slMua = rs.getInt("SoLuong");
                 double donGia = rs.getDouble("DonGia");
                 // Mặc định SL Xử Lý = 0, Thành tiền = 0. Khi tick nó sẽ nhảy lên.
                 tableModel.addRow(new Object[]{false, rs.getString("MaSP"), tenSp, slMua, 0, donGia, 0.0});
             }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void timKiemSanPhamMoi() {
        currentSP_Moi = new SanPhamDAO().timKiemSanPham(txtTimSpMoi.getText().trim()); 
        if(currentSP_Moi != null) {
            lblSpMoiInfo.setText(String.format("<html>%s<br>Kho: <b>%d</b> | Giá: %s</html>", currentSP_Moi.getTenSP(), currentSP_Moi.getSoLuongTon(), currencyVN.format(currentSP_Moi.getGiaBan())));
            ((SpinnerNumberModel) spnSoLuongMoi.getModel()).setMaximum(currentSP_Moi.getSoLuongTon());
            spnSoLuongMoi.setValue(1);
        } else {
            lblSpMoiInfo.setText("Không tìm thấy!");
        }
    }

    private void xacNhanDoiTra() {
        if(currentDH == null || soLuongMonTraLai == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất 1 sản phẩm trên bảng để xử lý!"); return;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); 

            // 1. DUYỆT BẢNG VÀ XỬ LÝ HÀNG TRẢ LẠI
            for(int i = 0; i < table.getRowCount(); i++) {
                Boolean isChecked = (Boolean) table.getValueAt(i, 0);
                int slTraLai = (int) table.getValueAt(i, 4);

                if(isChecked != null && isChecked && slTraLai > 0) {
                    String maSpCu = table.getValueAt(i, 1).toString();
                    int slMuaGoc = (int) table.getValueAt(i, 3);
                    
                    // Cộng tồn kho
                    try (PreparedStatement psKho = conn.prepareStatement("UPDATE SANPHAM SET SoLuongTon = SoLuongTon + ? WHERE MaSP = ?")) {
                        psKho.setInt(1, slTraLai); psKho.setString(2, maSpCu); psKho.executeUpdate();
                    }

                    // Cập nhật Chi Tiết Đơn Hàng
                    if (slTraLai == slMuaGoc) {
                        try (PreparedStatement psDel = conn.prepareStatement("DELETE FROM CHITIET_DONHANG WHERE MaDH = ? AND MaSP = ?")) {
                            psDel.setString(1, currentDH.getMaDH()); psDel.setString(2, maSpCu); psDel.executeUpdate();
                        }
                    } else {
                        double donGiaCu = (double) table.getValueAt(i, 5);
                        try (PreparedStatement psUpd = conn.prepareStatement("UPDATE CHITIET_DONHANG SET SoLuong = ?, ThanhTien = ? WHERE MaDH = ? AND MaSP = ?")) {
                            psUpd.setInt(1, slMuaGoc - slTraLai); psUpd.setDouble(2, (slMuaGoc - slTraLai) * donGiaCu); 
                            psUpd.setString(3, currentDH.getMaDH()); psUpd.setString(4, maSpCu); psUpd.executeUpdate();
                        }
                    }
                }
            }

            // 2. XỬ LÝ TIỀN & SP MỚI
            if(rdoTraHang.isSelected()) {
                try (PreparedStatement psDH = conn.prepareStatement("UPDATE DONHANG SET TongTien = ? WHERE MaDH = ?")) {
                    psDH.setDouble(1, Math.max(0, currentDH.getTongTien() - tongTienTraLai)); 
                    psDH.setString(2, currentDH.getMaDH()); psDH.executeUpdate();
                }
                conn.commit();
                JOptionPane.showMessageDialog(this, "TRẢ HÀNG THÀNH CÔNG!\nHoàn tiền: " + currencyVN.format(tongTienTraLai));

            } else {
                if(currentSP_Moi == null) { JOptionPane.showMessageDialog(this, "Chưa chọn sản phẩm mới!"); return; }
                int slLay = (int) spnSoLuongMoi.getValue();
                
                // Trừ kho mới
                try (PreparedStatement psKhoMoi = conn.prepareStatement("UPDATE SANPHAM SET SoLuongTon = SoLuongTon - ? WHERE MaSP = ?")) {
                    psKhoMoi.setInt(1, slLay); psKhoMoi.setString(2, currentSP_Moi.getMaSP()); psKhoMoi.executeUpdate();
                }

                // Thêm SP mới vào chi tiết
                double thanhTienSPMoi = slLay * currentSP_Moi.getGiaBan();
                try (PreparedStatement psInsert = conn.prepareStatement("INSERT INTO CHITIET_DONHANG (MaDH, MaSP, SoLuong, DonGia, ThanhTien) VALUES (?, ?, ?, ?, ?)")) {
                    psInsert.setString(1, currentDH.getMaDH()); psInsert.setString(2, currentSP_Moi.getMaSP());
                    psInsert.setInt(3, slLay); psInsert.setDouble(4, currentSP_Moi.getGiaBan()); psInsert.setDouble(5, thanhTienSPMoi);
                    psInsert.executeUpdate();
                }

                // Cập nhật tổng tiền đơn
                double tongTienMoi = currentDH.getTongTien() - tongTienTraLai + thanhTienSPMoi;
                try (PreparedStatement psDH = conn.prepareStatement("UPDATE DONHANG SET TongTien = ? WHERE MaDH = ?")) {
                    psDH.setDouble(1, tongTienMoi); psDH.setString(2, currentDH.getMaDH()); psDH.executeUpdate();
                }
                
                conn.commit();
                double chenhLech = thanhTienSPMoi - tongTienTraLai;
                if (chenhLech > 0) JOptionPane.showMessageDialog(this, "ĐỔI HÀNG THÀNH CÔNG!\nKhách BÙ THÊM: " + currencyVN.format(chenhLech));
                else JOptionPane.showMessageDialog(this, "ĐỔI HÀNG THÀNH CÔNG!\nCửa hàng TRẢ LẠI: " + currencyVN.format(Math.abs(chenhLech)));
            }

            // Refesh lại bảng
            timKiemDonHang();

        } catch (Exception ex) {
            if (conn != null) try { conn.rollback(); } catch (SQLException e) {}
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi Database!");
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) {}
        }
    }

    private void lamMoiForm() {
        tableModel.setRowCount(0); lblDonHangInfo.setText("...");
        lblSpCuInfo.setText("<html><i>Hãy tick (☑) và sửa số lượng trên bảng</i></html>");
        txtTimSpMoi.setText(""); lblSpMoiInfo.setText("SP Mới: ...");
        currentDH = null; currentSP_Moi = null;
        tongTienTraLai = 0; soLuongMonTraLai = 0;
    }
}