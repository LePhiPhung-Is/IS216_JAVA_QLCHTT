package src;

import src.view.LoginUI;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Khởi chạy trên luồng sự kiện của Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Đặt giao diện theo phong cách Hệ điều hành (Windows)
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // LỰA CHỌN 1: CHẠY TỪ MÀN HÌNH ĐĂNG NHẬP (Chuẩn nhất khi nộp đồ án)
            LoginUI loginForm = new LoginUI();
            loginForm.setVisible(true);

         
        });
    }
}