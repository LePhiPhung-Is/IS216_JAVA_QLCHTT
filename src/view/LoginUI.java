package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginUI extends JFrame {

    private float opacityLevel = 0f; // cho hiệu ứng fade

    public LoginUI() {
        setTitle("Beauty Shop");
        setUndecorated(true); // Loại bỏ viền và thanh tiêu đề để tránh lỗi
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setOpacity(0f); // Bắt đầu mờ

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        // ===== Background =====
        ImageIcon icon = new ImageIcon("src/assets/background.png");
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

        JLabel background = new JLabel(new ImageIcon(img));
        background.setBounds(0, 0, width, height);
        background.setLayout(null);
        add(background);

        // ===== Panel login (bo góc giả lập) =====
        int panelW = 350;
        int panelH = 320;

        int panelX = (width - panelW) / 2;
        int panelY = (height - panelH) / 2;

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(panelX, panelY, panelW, panelH);
        panel.setBackground(new Color(0, 0, 0, 200));
        panel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1));
        background.add(panel);

        // ===== Title =====
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBounds(100, 20, 200, 30);
        panel.add(lblTitle);

        // ===== Username =====
        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(Color.WHITE);
        lblUser.setBounds(40, 80, 100, 20);
        panel.add(lblUser);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(40, 100, 260, 30);
        panel.add(txtUser);

        // ===== Password =====
        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(Color.WHITE);
        lblPass.setBounds(40, 140, 100, 20);
        panel.add(lblPass);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(40, 160, 260, 30);
        panel.add(txtPass);

        // ==========================================
        // ===== Buttons: Đăng nhập & Thoát =====
        // ==========================================
        
        // Nút Đăng nhập
        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setBounds(40, 210, 125, 40); // Thu hẹp chiều rộng để nhường chỗ
        btnLogin.setBackground(new Color(50, 50, 50));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(80, 80, 80));
            }
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(50, 50, 50));
            }
        });
        panel.add(btnLogin);

        // Nút Thoát
        JButton btnExit = new JButton("Thoát");
        btnExit.setBounds(175, 210, 125, 40); // Đặt cạnh nút Đăng nhập
        btnExit.setBackground(new Color(150, 40, 40)); // Màu đỏ sậm
        btnExit.setForeground(Color.WHITE);
        btnExit.setFocusPainted(false);
        btnExit.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        btnExit.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnExit.setBackground(new Color(200, 50, 50)); // Sáng lên khi hover
            }
            public void mouseExited(MouseEvent e) {
                btnExit.setBackground(new Color(150, 40, 40));
            }
        });
        panel.add(btnExit);

        // Sự kiện cho nút Thoát
        btnExit.addActionListener(e -> {
            // Tắt hiệu ứng fade out (tùy chọn) trước khi thoát, hoặc thoát luôn
            System.exit(0);
        });

        // ==========================================
// ===== Link =====\
        JLabel lblRegister = new JLabel("Đăng ký");
        lblRegister.setForeground(Color.CYAN);
        lblRegister.setBounds(40, 265, 80, 20);

        //lblRegister.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        
        // Bắt sự kiện khi click chuột vào chữ "Đăng ký"
        lblRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new DangKyUI().setVisible(true);
                LoginUI.this.dispose(); 
            }
        });
        
        panel.add(lblRegister);

        JLabel lblForgot = new JLabel("Quên mật khẩu?");
        lblForgot.setForeground(Color.CYAN);
        lblForgot.setBounds(180, 265, 150, 20);
        panel.add(lblForgot);

        // ===== Logo =====
        JLabel logo = new JLabel("BEAUTY SHOP");
        logo.setFont(new Font("Serif", Font.BOLD, 40));
        logo.setForeground(new Color(255, 215, 0));
        logo.setBounds(60, 60, 500, 50);
        background.add(logo);

        // ===== Slogan =====
        JLabel slogan = new JLabel("Quý khách chính là vẻ đẹp của chúng tôi");
        slogan.setFont(new Font("Serif", Font.PLAIN, 22));
        slogan.setForeground(Color.WHITE);
        slogan.setBounds(100, height - 100, 800, 30);
        background.add(slogan);

        // ===== Login giả =====
        // ===== Login giả =====
btnLogin.addActionListener(e -> {
    String user = txtUser.getText();
    String pass = new String(txtPass.getPassword());

    if (user.equals("admin") && pass.equals("123")) {
        // Có thể giữ lại hoặc bỏ dòng thông báo này tùy ý bạn
        JOptionPane.showMessageDialog(this, "Đăng nhập thành công!"); 
        
        // Mở trang AdminUI
        new QuanLyUI().setVisible(true); 
        
        // Đóng trang LoginUI hiện tại
        this.dispose(); 
    } else {
        JOptionPane.showMessageDialog(this, "Sai tài khoản!");
    }
});

        // ===== Fade-in effect =====
        Timer timer = new Timer(20, e -> {
            opacityLevel += 0.05f;
            if (opacityLevel >= 1f) {
                opacityLevel = 1f;
                ((Timer) e.getSource()).stop();
            }
            setOpacity(opacityLevel);
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}