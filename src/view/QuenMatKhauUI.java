package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class QuenMatKhauUI extends JFrame {

    private float opacityLevel = 0f; // Cho hiệu ứng fade-in

    public QuenMatKhauUI() {
        setTitle("Beauty Shop - Quên Mật Khẩu");
        setUndecorated(true); // Loại bỏ viền và thanh tiêu đề
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Toàn màn hình
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setOpacity(0f); // Bắt đầu mờ

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        // ===== Background =====
        ImageIcon icon = new ImageIcon("src/assets/background.png"); // Nhớ check đuôi .png hay .jpg nhé
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

        JLabel background = new JLabel(new ImageIcon(img));
        background.setBounds(0, 0, width, height);
        background.setLayout(null);
        add(background);

        // ===== Panel Quên mật khẩu (bo góc giả lập, đen mờ) ====
        int panelW = 350;
        int panelH = 400;

        int panelX = (width - panelW) / 2;
        int panelY = (height - panelH) / 2;

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(panelX, panelY, panelW, panelH);
        panel.setBackground(new Color(0, 0, 0, 200));
        panel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1));
        background.add(panel);

        // ===== Title =====
        JLabel lblTitle = new JLabel("QUÊN MẬT KHẨU");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBounds(85, 20, 200, 30);
        panel.add(lblTitle);

        // ===== Username =====
        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(Color.WHITE);
        lblUser.setBounds(40, 70, 100, 20);
        panel.add(lblUser);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(40, 90, 260, 30);
        panel.add(txtUser);

        // ===== New Password =====
        JLabel lblPass = new JLabel("New Password:");
        lblPass.setForeground(Color.WHITE);
        lblPass.setBounds(40, 130, 150, 20);
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
        // ===== Buttons: Xác nhận & Quay lại =====
        // ==========================================
        
        // Nút Xác nhận
        JButton btnXacNhan = new JButton("Xác nhận");
        btnXacNhan.setBounds(40, 270, 125, 40); 
        btnXacNhan.setBackground(new Color(50, 50, 50));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFocusPainted(false);
        btnXacNhan.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        btnXacNhan.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnXacNhan.setBackground(new Color(80, 80, 80));
            }
            public void mouseExited(MouseEvent e) {
                btnXacNhan.setBackground(new Color(50, 50, 50));
            }
        });
        panel.add(btnXacNhan);

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

        // Sự kiện cho nút Xác nhận
       // Sự kiện cho nút Xác nhận
        btnXacNhan.addActionListener(e -> {
            String user = txtUser.getText();
            String pass = new String(txtPass.getPassword());
            String confirm = new String(txtConfirm.getPassword());

            // 1. Kiểm tra rỗng
            if (user.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Kiểm tra mật khẩu khớp
            if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. GỌI XUỐNG DATABASE ĐỂ KIỂM TRA VÀ CẬP NHẬT
            src.dao.TaiKhoanDAO dao = new src.dao.TaiKhoanDAO();
            
            if (dao.datLaiMatKhau(user, pass)) {
                // Nếu hàm trả về true -> Có tài khoản này -> Đổi thành công
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!\nVui lòng đăng nhập lại bằng mật khẩu mới.");
                new LoginUI().setVisible(true);
                this.dispose();
            } else {
                // Nếu hàm trả về false -> Không tìm thấy username trong Database
                JOptionPane.showMessageDialog(this, "Tên đăng nhập không tồn tại trong hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ==========================================
        // ===== Link sang trang Đăng nhập =====
        JLabel lblLogin = new JLabel("Đã nhớ mật khẩu? Đăng nhập");
        lblLogin.setForeground(Color.CYAN);
        lblLogin.setBounds(80, 335, 200, 20);
        lblLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        lblLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LoginUI().setVisible(true);
                QuenMatKhauUI.this.dispose();
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