package src.dao;

import src.database.DatabaseConnection;
import src.model.TaiKhoan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TaiKhoanDAO {
    // Hàm xử lý đăng ký tài khoản mới
    public boolean dangKy(TaiKhoan tk) {
        String sql = "INSERT INTO TAIKHOAN (TENDANGNHAP, MATKHAU, QUYEN, TRANGTHAI) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Lấy dữ liệu từ file Model
            ps.setString(1, tk.getTenDangNhap());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getQuyen()); 
            ps.setInt(4, tk.getTrangThai());
            
            int result = ps.executeUpdate();
            return result > 0; // Trả về true nếu thêm thành công
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}