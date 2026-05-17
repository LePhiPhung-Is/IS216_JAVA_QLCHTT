package src.view;

import src.dao.TaiKhoanDAO;
import src.model.TaiKhoan;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class DangKyUI extends JFrame {

    public float opacityLevel = 0f;
    public ArrayList<Integer> baibao = new ArrayList<>();

    public DangKyUI() {
        setTitle("Beauty Shop - Đăng Ký");
        setUndecorated(true); 
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setOpacity(0f); 

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        ImageIcon icon = new ImageIcon("src/assets/background.png");
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(img));
        background.setBounds(0, 0, width, height);
        add(background);

        // Kéo dài form ra chút để chứa ô Email
        int panelW = 350;
        int panelH = 460; 
        int panelX = (width - panelW) / 2;
        int panelY = (height - panelH) / 2;

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(panelX, panelY, panelW, panelH);
        panel.setBackground(new Color(0, 0, 0, 200));
        panel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1));
        background.add(panel);

        JLabel lblTitle = new JLabel("ĐĂNG KÝ");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBounds(115, 20, 200, 30);
        panel.add(lblTitle);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(Color.WHITE);
        lblUser.setBounds(40, 70, 100, 20);
        panel.add(lblUser);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(40, 90, 260, 30);
        panel.add(txtUser);

        // ===== Ô nhập Email =====
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setBounds(40, 130, 100, 20);
        panel.add(lblEmail);

        JTextField txtEmail = new JTextField();
        txtEmail.setBounds(40, 150, 260, 30);
        panel.add(txtEmail);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(Color.WHITE);
        lblPass.setBounds(40, 190, 100, 20);
        panel.add(lblPass);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(40, 210, 260, 30);
        panel.add(txtPass);

        JLabel lblConfirm = new JLabel("Confirm Password:");
        lblConfirm.setForeground(Color.WHITE);
        lblConfirm.setBounds(40, 250, 150, 20);
        panel.add(lblConfirm);

        JPasswordField txtConfirm = new JPasswordField();
        txtConfirm.setBounds(40, 270, 260, 30);
        panel.add(txtConfirm);

        JButton btnRegister = new JButton("Đăng ký");
        btnRegister.setBounds(40, 330, 125, 40); 
        btnRegister.setBackground(new Color(50, 50, 50));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        panel.add(btnRegister);

        JButton btnBack = new JButton("Quay lại");
        btnBack.setBounds(175, 330, 125, 40); 
        btnBack.setBackground(new Color(150, 40, 40)); 
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        panel.add(btnBack);

        btnBack.addActionListener(e -> {
            new LoginUI().setVisible(true);
            this.dispose();
        });

        btnRegister.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String email = txtEmail.getText().trim();
            String pass = new String(txtPass.getPassword());
            String confirm = new String(txtConfirm.getPassword());

            if (user.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }
            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Email không hợp lệ!");
                return;
            }
            if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!");
                return;
            }

            TaiKhoan tk = new TaiKhoan(user, pass, "Khách hàng", 1, email);
            TaiKhoanDAO dao = new TaiKhoanDAO();
            
            if (dao.dangKy(tk)) {
                JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
                new LoginUI().setVisible(true); 
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Đăng ký thất bại (Username đã tồn tại)!");
            }
        });

        JLabel lblLogin = new JLabel("Đã có tài khoản? Đăng nhập");
        lblLogin.setForeground(Color.CYAN);
        lblLogin.setBounds(85, 395, 200, 20);
        lblLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LoginUI().setVisible(true);
                DangKyUI.this.dispose();
            }
        });
        panel.add(lblLogin);

        JLabel logo = new JLabel("BEAUTY SHOP");
        logo.setFont(new Font("Serif", Font.BOLD, 40));
        logo.setForeground(new Color(255, 215, 0));
        logo.setBounds(60, 60, 500, 50);
        background.add(logo);

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