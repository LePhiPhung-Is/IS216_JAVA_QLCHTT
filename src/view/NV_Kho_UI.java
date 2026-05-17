package src.view;

import src.dao.SanPhamDAO;
import src.model.SanPham;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class NV_Kho_UI extends JFrame {

    public final Color SIDEBAR_BG = new Color(5, 5, 5);
    public final Color MAIN_BG = new Color(244, 247, 246);
    public final Color BRAND_GOLD = new Color(212, 175, 55);
    public final Color TEXT_LIGHT = new Color(209, 209, 209);
    public final Color HOVER_BG = new Color(26, 26, 26);
    public final Color DIVIDER_COLOR = new Color(51, 51, 51);

    public CardLayout cardLayout;
    public JPanel content;
    public ArrayList<Integer> baibao = new ArrayList<>();

    public NV_Kho_UI() {
        setTitle("Nhân viên Quản lý kho");
        setUndecorated(true); 
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(280, 700));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, HOVER_BG));

        JLabel brandLabel = new JLabel("BEAUTY SHOP", SwingConstants.CENTER);
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        brandLabel.setForeground(BRAND_GOLD);
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        brandLabel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JPanel brandPanel = new JPanel(new BorderLayout());
        brandPanel.setBackground(SIDEBAR_BG);
        brandPanel.add(brandLabel, BorderLayout.CENTER);
        brandPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, DIVIDER_COLOR));
        brandPanel.setMaximumSize(new Dimension(280, 100));

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(SIDEBAR_BG);
        userPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, DIVIDER_COLOR),
                new EmptyBorder(25, 20, 25, 20)
        ));

        JLabel avatar = new JLabel("K", SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(80, 80));
        avatar.setMaximumSize(new Dimension(80, 80));
        avatar.setOpaque(true);
        avatar.setBackground(BRAND_GOLD);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 35));
        avatar.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel("Nhân viên Kho");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(new EmptyBorder(15, 0, 5, 0));

        JLabel roleLabel = new JLabel("Quản lý kho hàng");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roleLabel.setForeground(new Color(160, 160, 160));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        userPanel.add(avatar);
        userPanel.add(nameLabel);
        userPanel.add(roleLabel);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(SIDEBAR_BG);
        menuPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        menuPanel.add(createMenuItem("LẬP PHIẾU NHẬP KHO", "nhapkho"));
        menuPanel.add(createMenuItem("KIỂM KÊ KHO", "kiemke"));
        menuPanel.add(createMenuItem("THỐNG KÊ HÀNG TỒN", "thongke"));

        JPanel divider = new JPanel();
        divider.setBackground(DIVIDER_COLOR);
        divider.setMaximumSize(new Dimension(230, 1));

        JPanel dividerContainer = new JPanel();
        dividerContainer.setBackground(SIDEBAR_BG);
        dividerContainer.setBorder(new EmptyBorder(20, 0, 20, 0));
        dividerContainer.add(divider);

        menuPanel.add(dividerContainer);
        menuPanel.add(createMenuItem("ĐĂNG XUẤT", "logout"));

        sidebar.add(brandPanel);
        sidebar.add(userPanel);
        sidebar.add(menuPanel);
        sidebar.add(Box.createVerticalGlue());

        cardLayout = new CardLayout();
        content = new JPanel(cardLayout);
        content.setBackground(MAIN_BG);

        content.add(createPhieuNhapKhoPanel(), "nhapkho");
        content.add(createKiemKeKhoPanel(), "kiemke");
        content.add(createThongKePanel(), "thongke");

        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
    }

    public JPanel createMenuItem(String text, String cardName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SIDEBAR_BG);
        panel.setMaximumSize(new Dimension(280, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_LIGHT);
        label.setBorder(new EmptyBorder(10, 25, 10, 20));

        panel.add(label, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(HOVER_BG);
                label.setForeground(BRAND_GOLD);
                panel.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, BRAND_GOLD));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(SIDEBAR_BG);
                label.setForeground(TEXT_LIGHT);
                panel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if (cardName.equals("logout")) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        new LoginUI().setVisible(true);
                        dispose(); 
                    }
                } else {
                    cardLayout.show(content, cardName);
                }
            }
        });
        return panel;
    }

    public JPanel createPhieuNhapKhoPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(MAIN_BG);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("LẬP PHIẾU NHẬP KHO");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 15));
        formPanel.setBackground(MAIN_BG);
        
        JComboBox<String> cbNCC = new JComboBox<>(new String[]{"Nhà cung cấp A", "Nhà cung cấp B", "Công ty may mặc X"});
        
        // Kéo danh sách Sản phẩm từ Database
        JComboBox<String> cbSP = new JComboBox<>();
        List<SanPham> listSP = new SanPhamDAO().getAllSanPham();
        for (SanPham sp : listSP) {
            cbSP.addItem(sp.getTenSP() + " (" + sp.getMaSP() + ")");
        }

        JTextField txtSoLuong = new JTextField();
        JTextField txtDonGia = new JTextField();

        formPanel.add(new JLabel("Chọn Nhà Cung Cấp:")); formPanel.add(cbNCC);
        formPanel.add(new JLabel("Chọn Sản Phẩm:")); formPanel.add(cbSP);
        formPanel.add(new JLabel("Số Lượng Nhập:")); formPanel.add(txtSoLuong);
        formPanel.add(new JLabel("Đơn Giá Nhập (VNĐ):")); formPanel.add(txtDonGia);

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(MAIN_BG);
        centerContainer.add(formPanel, BorderLayout.NORTH);
        
        String[] columns = {"Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        centerContainer.add(new JScrollPane(table), BorderLayout.CENTER);

        panel.add(centerContainer, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(MAIN_BG);
        JButton btnAdd = new JButton("Thêm vào danh sách");
        btnAdd.addActionListener(e -> {
            try {
                int sl = Integer.parseInt(txtSoLuong.getText());
                double gia = Double.parseDouble(txtDonGia.getText());
                double thanhTien = sl * gia;
                model.addRow(new Object[]{cbSP.getSelectedItem().toString(), sl, gia, thanhTien});
                txtSoLuong.setText("");
                txtDonGia.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Vui lòng nhập số hợp lệ cho Số lượng và Đơn giá!");
            }
        });

        JButton btnSave = new JButton("Hoàn tất lập phiếu");
        btnSave.setBackground(BRAND_GOLD);
        btnSave.setForeground(Color.BLACK);
        btnSave.addActionListener(e -> {
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(panel, "Danh sách nhập đang trống!");
                return;
            }
            JOptionPane.showMessageDialog(panel, "Đã lưu phiếu nhập thành công (Cần có PhieuNhapDAO để ghi DB)!");
            model.setRowCount(0);
        });
        
        bottomPanel.add(btnAdd);
        bottomPanel.add(btnSave);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    public JPanel createKiemKeKhoPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(MAIN_BG);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("KIỂM KÊ KHO");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Mã SP", "Tên Sản Phẩm", "Tồn Hệ Thống", "Tồn Thực Tế", "Chênh Lệch", "Lý Do"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        List<SanPham> listSP = new SanPhamDAO().getAllSanPham();
        for (SanPham sp : listSP) {
            model.addRow(new Object[]{sp.getMaSP(), sp.getTenSP(), sp.getSoLuongTon(), "", "", ""});
        }
        
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(MAIN_BG);
        JButton btnSave = new JButton("Xác Nhận Kiểm Kê");
        btnSave.setBackground(BRAND_GOLD);
        btnSave.setForeground(Color.BLACK);
        btnSave.addActionListener(e -> JOptionPane.showMessageDialog(panel, "Lưu thông tin kiểm kê thành công!"));

        bottomPanel.add(btnSave);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    public JPanel createThongKePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(MAIN_BG);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(MAIN_BG);
        
        JLabel title = new JLabel("THỐNG KÊ HÀNG TỒN KHO");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(title, BorderLayout.NORTH);
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(MAIN_BG);
        JComboBox<String> cbFilter = new JComboBox<>(new String[]{"Tất cả sản phẩm", "Sắp hết hàng (< 10)", "Tồn nhiều (> 50)"});
        filterPanel.add(new JLabel("Tiêu chí lọc:"));
        filterPanel.add(cbFilter);
        
        headerPanel.add(filterPanel, BorderLayout.SOUTH);
        panel.add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"Mã SP", "Tên Sản Phẩm", "Danh Mục", "Số Lượng Tồn", "Trạng Thái"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnThongKe = new JButton("Lấy Dữ Liệu");
        btnThongKe.addActionListener(e -> {
            model.setRowCount(0);
            List<SanPham> listSP = new SanPhamDAO().getAllSanPham();
            int filterType = cbFilter.getSelectedIndex();
            
            for (SanPham sp : listSP) {
                if (filterType == 1 && sp.getSoLuongTon() >= 10) continue;
                if (filterType == 2 && sp.getSoLuongTon() <= 50) continue;
                
                model.addRow(new Object[]{sp.getMaSP(), sp.getTenSP(), sp.getMaDM(), sp.getSoLuongTon(), sp.getTrangThai()});
            }
        });
        filterPanel.add(btnThongKe);

        // Click để nạp sẵn dữ liệu ban đầu
        btnThongKe.doClick();

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NV_Kho_UI().setVisible(true));
    }
}