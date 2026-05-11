package src.view;

import src.dao.TaiKhoanDAO;
import src.model.TaiKhoan;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DangKyUI extends JFrame {

    private float opacityLevel = 0f; // Cho hiệu ứng fade-in

    public DangKyUI() {
        setTitle("Beauty Shop - Đăng Ký");
        setUndecorated(true); // Loại bỏ viền và thanh tiêu đề
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Toàn màn hình
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

        // ===== Panel Đăng ký (bo góc giả lập, đen mờ) =====
        int panelW = 350;
        int panelH = 400; // Tăng chiều cao lên một chút để chứa ô Xác nhận mật khẩu

        int panelX = (width - panelW) / 2;
        int panelY = (height - panelH) / 2;

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(panelX, panelY, panelW, panelH);
        panel.setBackground(new Color(0, 0, 0, 200));
        panel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1));
        background.add(panel);

        // ===== Title =====
        JLabel lblTitle = new JLabel("ĐĂNG KÝ");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBounds(115, 20, 200, 30);
        panel.add(lblTitle);

        // ===== Username =====
        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(Color.WHITE);
        lblUser.setBounds(40, 70, 100, 20);
        panel.add(lblUser);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(40, 90, 260, 30);
        panel.add(txtUser);

        // ===== Password =====
        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(Color.WHITE);
        lblPass.setBounds(40, 130, 100, 20);
        panel.add(lblPass);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(40, 150, 260, 30);
        panel.add(txtPass);

        // ===== Confirm Password =====
        JLabel lblConfirm = new JLabel("Confirm Password:");
        lblConfirm.setForeground(Color.WHITE);
        lblConfirm.setBounds(40, 190, 150, 20);
        panel.add(lblConfirm);

        JPasswordField txtConfirm = new JPasswordField();
        txtConfirm.setBounds(40, 210, 260, 30);
        panel.add(txtConfirm);

        // ==========================================
        // ===== Buttons: Đăng ký & Quay lại =====
        // ==========================================
        
        // Nút Đăng ký
        JButton btnRegister = new JButton("Đăng ký");
        btnRegister.setBounds(40, 270, 125, 40); 
        btnRegister.setBackground(new Color(50, 50, 50));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        btnRegister.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnRegister.setBackground(new Color(80, 80, 80));
            }
            public void mouseExited(MouseEvent e) {
                btnRegister.setBackground(new Color(50, 50, 50));
            }
        });
        panel.add(btnRegister);

        // Nút Quay lại
        JButton btnBack = new JButton("Quay lại");
        btnBack.setBounds(175, 270, 125, 40); 
        btnBack.setBackground(new Color(150, 40, 40)); 
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        btnBack.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnBack.setBackground(new Color(200, 50, 50)); 
            }
            public void mouseExited(MouseEvent e) {
                btnBack.setBackground(new Color(150, 40, 40));
            }
        });
        panel.add(btnBack);

        // Sự kiện cho nút Quay lại
        btnBack.addActionListener(e -> {
            new LoginUI().setVisible(true);
            this.dispose();
        });

        // Sự kiện cho nút Đăng ký (Xử lý lưu Database)
        btnRegister.addActionListener(e -> {
            String user = txtUser.getText();
            String pass = new String(txtPass.getPassword());
            String confirm = new String(txtConfirm.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!");
                return;
            }

            // Tạo đối tượng TaiKhoan chuẩn theo Model của nhóm (quyền Khách hàng, trạng thái 1 - Hoạt động)
            TaiKhoan tk = new TaiKhoan(user, pass, "Khách hàng", 1);
            TaiKhoanDAO dao = new TaiKhoanDAO();
            
            if (dao.dangKy(tk)) {
                JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
                new LoginUI().setVisible(true); // Trở về màn hình đăng nhập
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Đăng ký thất bại (Tài khoản có thể đã tồn tại)!");
            }
        });

        // ==========================================

        // ===== Link sang trang Đăng nhập =====
        JLabel lblLogin = new JLabel("Đã có tài khoản? Đăng nhập");
        lblLogin.setForeground(Color.CYAN);
        lblLogin.setBounds(85, 335, 200, 20);
        lblLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        lblLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LoginUI().setVisible(true);
                DangKyUI.this.dispose();
            }
        });
        panel.add(lblLogin);

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
}