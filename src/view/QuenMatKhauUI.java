package src.view;

import src.dao.TaiKhoanDAO;
import src.dao.EmailService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class QuenMatKhauUI extends JFrame {

    public float opacityLevel = 0f;
    public String currentOTP = ""; 
    public ArrayList<Integer> baibao = new ArrayList<>();

    public QuenMatKhauUI() {
        setTitle("Beauty Shop - Quên Mật Khẩu");
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

        int panelW = 350;
        int panelH = 520; 
        int panelX = (width - panelW) / 2;
        int panelY = (height - panelH) / 2;

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(panelX, panelY, panelW, panelH);
        panel.setBackground(new Color(0, 0, 0, 200));
        panel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1));
        background.add(panel);

        JLabel lblTitle = new JLabel("QUÊN MẬT KHẨU");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBounds(85, 20, 200, 30);
        panel.add(lblTitle);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(Color.WHITE);
        lblUser.setBounds(40, 60, 100, 20);
        panel.add(lblUser);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(40, 80, 260, 30);
        panel.add(txtUser);

        JLabel lblEmail = new JLabel("Email đăng ký:");
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setBounds(40, 120, 150, 20);
        panel.add(lblEmail);

        JTextField txtEmail = new JTextField();
        txtEmail.setBounds(40, 140, 260, 30);
        panel.add(txtEmail);

        JLabel lblOTP = new JLabel("Mã OTP:");
        lblOTP.setForeground(Color.WHITE);
        lblOTP.setBounds(40, 180, 100, 20);
        panel.add(lblOTP);

        JTextField txtOTP = new JTextField();
        txtOTP.setBounds(40, 200, 150, 30);
        panel.add(txtOTP);

        JButton btnSendOTP = new JButton("Gửi mã");
        btnSendOTP.setBounds(200, 200, 100, 30);
        btnSendOTP.setBackground(new Color(212, 175, 55));
        btnSendOTP.setForeground(Color.BLACK);
        btnSendOTP.setFocusPainted(false);
        
        btnSendOTP.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String email = txtEmail.getText().trim();
            
            if (user.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Username và Email!");
                return;
            }

            // KIỂM TRA BẢO MẬT: Phải khớp Username và Email trong Database
            TaiKhoanDAO dao = new TaiKhoanDAO();
            if (!dao.kiemTraEmailTaiKhoan(user, email)) {
                JOptionPane.showMessageDialog(this, "Username không tồn tại hoặc Email không khớp!", "Lỗi xác thực", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            btnSendOTP.setText("Đang gửi...");
            btnSendOTP.setEnabled(false);

            new Thread(() -> {
                try {
                    currentOTP = String.valueOf((int)(Math.random() * 900000) + 100000);
                    boolean isSent = EmailService.sendOTP(email, currentOTP);
                    
                    SwingUtilities.invokeLater(() -> {
                        btnSendOTP.setText("Gửi mã");
                        btnSendOTP.setEnabled(true);
                        if (isSent) {
                            JOptionPane.showMessageDialog(this, "Đã gửi mã OTP! Vui lòng kiểm tra hộp thư.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Lỗi gửi mail! (Vui lòng thay thế file mail-api.jar bằng file mail.jar full)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        btnSendOTP.setText("Gửi mã");
                        btnSendOTP.setEnabled(true);
                        JOptionPane.showMessageDialog(this, "Lỗi thư viện Mail: " + ex.getMessage());
                    });
                }
            }).start();
        });
        panel.add(btnSendOTP);

        JLabel lblPass = new JLabel("Mật khẩu mới:");
        lblPass.setForeground(Color.WHITE);
        lblPass.setBounds(40, 240, 150, 20);
        panel.add(lblPass);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(40, 260, 260, 30);
        panel.add(txtPass);

        JLabel lblConfirm = new JLabel("Xác nhận mật khẩu:");
        lblConfirm.setForeground(Color.WHITE);
        lblConfirm.setBounds(40, 300, 150, 20);
        panel.add(lblConfirm);

        JPasswordField txtConfirm = new JPasswordField();
        txtConfirm.setBounds(40, 320, 260, 30);
        panel.add(txtConfirm);

        JButton btnXacNhan = new JButton("Xác nhận");
        btnXacNhan.setBounds(40, 380, 125, 40); 
        btnXacNhan.setBackground(new Color(50, 50, 50));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFocusPainted(false);
        panel.add(btnXacNhan);

        JButton btnBack = new JButton("Quay lại");
        btnBack.setBounds(175, 380, 125, 40); 
        btnBack.setBackground(new Color(150, 40, 40)); 
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        panel.add(btnBack);

        btnBack.addActionListener(e -> {
            new LoginUI().setVisible(true);
            this.dispose();
        });

        btnXacNhan.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String otpInput = txtOTP.getText().trim();
            String pass = new String(txtPass.getPassword());
            String confirm = new String(txtConfirm.getPassword());

            if (user.isEmpty() || otpInput.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }
            if (currentOTP.isEmpty() || !otpInput.equals(currentOTP)) {
                JOptionPane.showMessageDialog(this, "Mã OTP không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TaiKhoanDAO dao = new TaiKhoanDAO();
            if (dao.datLaiMatKhau(user, pass)) {
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
                new LoginUI().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi cập nhật mật khẩu!");
            }
        });

        JLabel lblLogin = new JLabel("Đăng nhập");
        lblLogin.setForeground(Color.CYAN);
        lblLogin.setBounds(140, 435, 200, 20);
        lblLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLogin.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                new LoginUI().setVisible(true);
                QuenMatKhauUI.this.dispose();
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