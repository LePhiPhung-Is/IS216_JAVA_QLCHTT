package src.view;

import javax.swing.*;
import java.awt.*;

public class AdminUI extends JFrame {

    public AdminUI() {

        setTitle("Admin Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout tổng
        setLayout(new BorderLayout());

        // ===== Sidebar =====
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 700));
        sidebar.setBackground(new Color(30, 30, 30));
        sidebar.setLayout(new GridLayout(10, 1, 10, 10));

        JButton btnProduct = new JButton("Quản lý sản phẩm");
        JButton btnOrder = new JButton("Quản lý đơn hàng");
        JButton btnCustomer = new JButton("Quản lý khách hàng");

        sidebar.add(btnProduct);
        sidebar.add(btnOrder);
        sidebar.add(btnCustomer);

        // ===== Content =====
        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);

        JLabel welcome = new JLabel("WELCOME ADMIN");
        welcome.setFont(new Font("Arial", Font.BOLD, 30));

        content.add(welcome);

        // ===== Add vào frame =====
        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
    }
}