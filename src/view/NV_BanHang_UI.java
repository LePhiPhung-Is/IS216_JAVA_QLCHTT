package src.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NV_BanHang_UI extends JFrame {

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
    
    private boolean isCollapsed = false;
    private JPanel sidebar;
    public NV_BanHang_UI() {

        setTitle("Nhân viên bán hàng");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
       //Hàm Toggle
       


        // ================= SIDEBAR =================
        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(280, 700));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, HOVER_BG));
        //============== chỉ đổi màu chữ
        JButton btnToggle = new JButton("☰");
        btnToggle.setText("☰");
        

        // màu nền đồng bộ sidebar
        btnToggle.setBackground(SIDEBAR_BG);
        btnToggle.setOpaque(true);

        // bỏ viền + focus
        btnToggle.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btnToggle.setFocusPainted(false);

        // màu icon mặc định
        btnToggle.setForeground(TEXT_LIGHT);

        // cursor tay
        btnToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnToggle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnToggle.setForeground(BRAND_GOLD);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnToggle.setForeground(TEXT_LIGHT);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                toggleSidebar(); // 👈 THÊM DÒNG NÀY
            }
});
        // ================= LOGO =================
        JLabel brandLabel = new JLabel("BEAUTY SHOP", SwingConstants.CENTER);
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        brandLabel.setForeground(BRAND_GOLD);
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        brandLabel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JPanel brandPanel = new JPanel(new BorderLayout());
        brandPanel.setBackground(SIDEBAR_BG);
        brandPanel.add(btnToggle, BorderLayout.WEST);
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

        JLabel avatar = new JLabel("S", SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(80, 80));
        avatar.setMaximumSize(new Dimension(80, 80));
        avatar.setOpaque(true);
        avatar.setBackground(BRAND_GOLD);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 35));
        avatar.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel("Nhân viên A");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(new EmptyBorder(15, 0, 5, 0));

        JLabel roleLabel = new JLabel("Nhân viên bán hàng");
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

        menuPanel.add(createMenuItem("QUẢN LÝ SẢN PHẨM", "product"));
        menuPanel.add(createMenuItem("QUẢN LÝ KHÁCH HÀNG", "customer"));
        menuPanel.add(createMenuItem("QUẢN LÝ DANH MỤC", "category"));
        menuPanel.add(createMenuItem("QUẢN LÝ ĐƠN HÀNG", "order"));
        menuPanel.add(createMenuItem("XỬ LÝ ĐỔI TRẢ", "return"));

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

        // ================= CONTENT =================
        cardLayout = new CardLayout();
        content = new JPanel(cardLayout);
        content.setBackground(MAIN_BG);
        

        content.add(new ProductManagementPanel(), "product");        content.add(createPage("QUẢN LÝ KHÁCH HÀNG"), "customer");
        content.add(new CategoryManagementPanel(), "category");
        content.add(createPage("QUẢN LÝ ĐƠN HÀNG"), "order");
        content.add(createPage("XỬ LÝ ĐỔI TRẢ"), "return");
        content.add(createPage("ĐĂNG XUẤT"), "logout");

        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
    }

    // ================= PAGE =================
    private JPanel createPage(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(MAIN_BG);

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));
        label.setForeground(Color.GRAY);

        panel.add(label, BorderLayout.CENTER);
        return panel;
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
                cardLayout.show(content, cardName);
            }
        });

        return panel;
    }
    //Hàm toggle 
    private void toggleSidebar() {
        isCollapsed = !isCollapsed;

        if (isCollapsed) {
            sidebar.setPreferredSize(new Dimension(80, getHeight()));
        } else {
            sidebar.setPreferredSize(new Dimension(280, getHeight()));
        }

        sidebar.revalidate();
    }
    // ================= MAIN =================
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
               System.out.println("LỖI INSERT SẢN PHẨM:");
                 e.printStackTrace();
   
        }

        SwingUtilities.invokeLater(() -> {
            new NV_BanHang_UI().setVisible(true);
        });
    }
}