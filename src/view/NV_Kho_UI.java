package src.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NV_Kho_UI extends JFrame {

    // ================= MÀU GIAO DIỆN =================
    private final Color SIDEBAR_BG = new Color(5, 5, 5);
    private final Color MAIN_BG = new Color(244, 247, 246);
    private final Color BRAND_GOLD = new Color(212, 175, 55);
    private final Color TEXT_LIGHT = new Color(209, 209, 209);
    private final Color HOVER_BG = new Color(26, 26, 26);
    private final Color DIVIDER_COLOR = new Color(51, 51, 51);

    // ================= CARD LAYOUT =================
    private CardLayout cardLayout;
    private JPanel content;

    public NV_Kho_UI() {

        setTitle("Nhân viên Quản lý kho");
        setUndecorated(true); 
        setExtendedState(JFrame.MAXIMIZED_BOTH); 

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= SIDEBAR =================
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(280, 700));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, HOVER_BG));

        // ================= LOGO =================
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

        // ================= USER PANEL =================
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

        // ================= MENU PANEL =================
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(SIDEBAR_BG);
        menuPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Các chức năng bám sát Use Case và Sequence Diagram
        menuPanel.add(createMenuItem("LẬP PHIẾU NHẬP KHO", "nhapkho"));
        menuPanel.add(createMenuItem("KIỂM KÊ KHO", "kiemke"));
        menuPanel.add(createMenuItem("THỐNG KÊ HÀNG TỒN", "thongke"));

        // ===== Divider =====
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

        // ================= CONTENT PANELS =================
        cardLayout = new CardLayout();
        content = new JPanel(cardLayout);
        content.setBackground(MAIN_BG);

        // Thêm các trang chức năng (đã được thiết kế layout cơ bản bên dưới)
<<<<<<< HEAD
        content.add(new LapPhieuNhapKho(), "nhapkho");
=======
        content.add(createPhieuNhapKhoPanel(), "nhapkho");
>>>>>>> 36338e03d74ada1567e1935570181a80aa421186
        content.add(createKiemKeKhoPanel(), "kiemke");
        content.add(createThongKePanel(), "thongke");
        content.add(createPage("ĐĂNG XUẤT"), "logout");

        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
    }

    // ================= MENU ITEM =================
    private JPanel createMenuItem(String text, String cardName) {
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
                        dispose(); // Đóng cửa sổ hiện tại
                    }
                } else {
                    cardLayout.show(content, cardName);
                }
            }
        });

        return panel;
    }

    // ================= TRANG TRỐNG MẶC ĐỊNH =================
    private JPanel createPage(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(MAIN_BG);
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));
        label.setForeground(Color.GRAY);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    // ================= UI: LẬP PHIẾU NHẬP KHO =================
    // Dựa trên Activity: Chọn NCC -> Chọn SP -> Nhập SL, Đơn giá -> Validate -> Lưu
    private JPanel createPhieuNhapKhoPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(MAIN_BG);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("LẬP PHIẾU NHẬP KHO");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 15));
        formPanel.setBackground(MAIN_BG);
        
        formPanel.add(new JLabel("Chọn Nhà Cung Cấp:"));
        formPanel.add(new JComboBox<>(new String[]{" "}));
        
        formPanel.add(new JLabel("Chọn Sản Phẩm:"));
        formPanel.add(new JComboBox<>(new String[]{"  "}));
        
        formPanel.add(new JLabel("Số Lượng Nhập:"));
        formPanel.add(new JTextField());
        
        formPanel.add(new JLabel("Đơn Giá Nhập (VNĐ):"));
        formPanel.add(new JTextField());

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(MAIN_BG);
        centerContainer.add(formPanel, BorderLayout.NORTH);
        
        // Bảng tạm chứa các chi tiết phiếu nhập
        String[] columns = {"Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        JTable table = new JTable(new DefaultTableModel(columns, 0));
        JScrollPane scrollPane = new JScrollPane(table);
        centerContainer.add(scrollPane, BorderLayout.CENTER);

        panel.add(centerContainer, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(MAIN_BG);
        JButton btnAdd = new JButton("Thêm vào danh sách");
        JButton btnSave = new JButton("Hoàn tất lập phiếu");
        btnSave.setBackground(BRAND_GOLD);
        btnSave.setForeground(Color.BLACK);
        
        bottomPanel.add(btnAdd);
        bottomPanel.add(btnSave);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ================= UI: KIỂM KÊ KHO =================
    // Dựa trên Activity: Thống kê hệ thống -> Nhập SL thực tế -> Tính chênh lệch -> Ghi lý do -> Lưu
    private JPanel createKiemKeKhoPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(MAIN_BG);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("KIỂM KÊ KHO");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Mã SP", "Tên Sản Phẩm", "Tồn Hệ Thống", "Tồn Thực Tế", "Chênh Lệch", "Lý Do"};
        Object[][] data = {
           /*  {"SP001", "Son môi MAC", 50, "", "", ""},
            {"SP002", "Kem dưỡng ẩm Vichy", 30, "", "", ""}*/
        };
        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(MAIN_BG);
        JButton btnCalc = new JButton("Tính Chênh Lệch");
        JButton btnSave = new JButton("Xác Nhận Kiểm Kê");
        btnSave.setBackground(BRAND_GOLD);
        btnSave.setForeground(Color.BLACK);

        bottomPanel.add(btnCalc);
        bottomPanel.add(btnSave);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ================= UI: THỐNG KÊ HÀNG TỒN =================
    // Dựa trên Activity: Chọn tiêu chí -> Truy xuất DB -> Tính tổng -> Hiển thị danh sách
    private JPanel createThongKePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(MAIN_BG);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(MAIN_BG);
        JLabel title = new JLabel("THỐNG KÊ HÀNG TỒN KHO");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        topPanel.add(title);
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(MAIN_BG);
        filterPanel.add(new JLabel("Tiêu chí lọc:"));
        filterPanel.add(new JComboBox<>(new String[]{"Tất cả sản phẩm", "Sắp hết hàng (< 10)", "Tồn nhiều (> 50)"}));
        JButton btnThongKe = new JButton("Thống Kê");
        filterPanel.add(btnThongKe);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(MAIN_BG);
        headerPanel.add(topPanel, BorderLayout.NORTH);
        headerPanel.add(filterPanel, BorderLayout.SOUTH);
        
        panel.add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"Mã SP", "Tên Sản Phẩm", "Phân Loại", "Size", "Số Lượng Tồn"};
        JTable table = new JTable(new DefaultTableModel(columns, 0));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new NV_Kho_UI().setVisible(true);
        });
    }
}