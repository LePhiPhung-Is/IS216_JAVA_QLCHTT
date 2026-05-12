package src.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class QuanLyUI extends JFrame {

    // ================= MÀU GIAO DIỆN =================
    private final Color SIDEBAR_BG = new Color(5, 5, 5);
    private final Color MAIN_BG = new Color(244, 247, 246);
    private final Color BRAND_GOLD = new Color(212, 175, 55);
    private final Color TEXT_LIGHT = new Color(209, 209, 209);
    private final Color HOVER_BG = new Color(26, 26, 26);
    private final Color DIVIDER_COLOR = new Color(51, 51, 51);

    // CHỈNH SỬA: Đưa content ra làm biến toàn cục để hàm createMenuItem có thể sử dụng
    private JPanel content;

    public QuanLyUI() {

        setTitle("Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
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

        JLabel avatar = new JLabel("A", SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(80, 80));
        avatar.setMaximumSize(new Dimension(80, 80));
        avatar.setOpaque(true);
        avatar.setBackground(BRAND_GOLD);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 35));
        avatar.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel("Phùng Lê Phi");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(new EmptyBorder(15, 0, 5, 0));

        JLabel roleLabel = new JLabel("Quản lý");
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

        // ===== Các mục Menu =====
        menuPanel.add(createMenuItem("QUẢN LÝ NHÂN VIÊN"));
        menuPanel.add(createMenuItem("QUẢN LÝ NHÀ CUNG CẤP"));
        menuPanel.add(createMenuItem("QUẢN LÝ KHUYẾN MÃI"));
        menuPanel.add(createMenuItem("THỐNG KÊ DOANH THU"));
        menuPanel.add(createMenuItem("THỐNG KÊ HÀNG TỒN"));

        // ===== Divider =====
        JPanel divider = new JPanel();
        divider.setBackground(DIVIDER_COLOR);
        divider.setMaximumSize(new Dimension(230, 1));
        JPanel dividerContainer = new JPanel();
        dividerContainer.setBackground(SIDEBAR_BG);
        dividerContainer.setBorder(new EmptyBorder(20, 0, 20, 0));
        dividerContainer.add(divider);

        menuPanel.add(dividerContainer);
        menuPanel.add(createMenuItem("ĐĂNG XUẤT"));

        // ===== Add Sidebar =====
        sidebar.add(brandPanel);
        sidebar.add(userPanel);
        sidebar.add(menuPanel);
        sidebar.add(Box.createVerticalGlue());

        // ================= CONTENT =================
        // CHỈNH SỬA: Khởi tạo biến content (không viết JPanel phía trước)
        content = new JPanel();
        content.setBackground(MAIN_BG);
        content.setLayout(new BorderLayout());

        JLabel welcome = new JLabel("WELCOME ADMIN", SwingConstants.CENTER);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcome.setForeground(Color.GRAY);
        content.add(welcome, BorderLayout.CENTER);

        // ================= ADD FRAME =================
        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
    }

    // ================= MENU ITEM =================
    private JPanel createMenuItem(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SIDEBAR_BG);
        panel.setMaximumSize(new Dimension(280, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_LIGHT);
        label.setBorder(new EmptyBorder(10, 25, 10, 20));
        panel.add(label, BorderLayout.CENTER);

        // ===== Sự kiện chuột =====
        // ===== Sự kiện chuột khôi phục cho Doanh thu =====
panel.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        // Logic thay đổi trang khi click
        if (text.equals("THỐNG KÊ HÀNG TỒN")) {
            content.removeAll(); 
            content.add(new ThongKeHangTon(), BorderLayout.CENTER); 
            content.revalidate();
            content.repaint();
        } 
        // --- ĐOẠN KHÔI PHỤC CHO BÁO CÁO DOANH THU ---
        else if (text.equals("THỐNG KÊ DOANH THU")) {
            content.removeAll();
            content.add(new ThongKeDoanhThu(), BorderLayout.CENTER); // Nạp trang doanh thu
            content.revalidate();
            content.repaint();
        }
    }

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
});

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
            new QuanLyUI().setVisible(true);
        });
    }
}