package src.dao;

import src.database.DatabaseConnection;
import src.model.TaiKhoan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaiKhoanDAO {
    // 1. Cập nhật hàm Đăng ký có lưu Email
    public boolean dangKy(TaiKhoan tk) {
        String sql = "INSERT INTO TAIKHOAN (TENDANGNHAP, MATKHAU, QUYEN, TRANGTHAI, EMAIL) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tk.getTenDangNhap());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getQuyen()); 
            ps.setInt(4, tk.getTrangThai());
            ps.setString(5, tk.getEmail()); // Lưu Email
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. HÀM MỚI: Kiểm tra Username và Email có khớp nhau không
    public boolean kiemTraEmailTaiKhoan(String username, String email) {
        String sql = "SELECT * FROM TAIKHOAN WHERE TENDANGNHAP = ? AND EMAIL = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, username);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Có dữ liệu -> Khớp
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Hàm đổi mật khẩu giữ nguyên
    public boolean datLaiMatKhau(String username, String newPassword) {
        String sql = "UPDATE TAIKHOAN SET MATKHAU = ? WHERE TENDANGNHAP = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}