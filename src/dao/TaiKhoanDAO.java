package src.dao;

import src.database.DatabaseConnection;
import src.model.TaiKhoan;
import src.database.PasswordUtil;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaiKhoanDAO {
    // 1. Cập nhật hàm Đăng ký có lưu Email
public boolean dangKy(TaiKhoan tk) {
    String sql = "{call SP_DANGKY_TAIKHOAN(?, ?, ?, ?)}";
    
    try (Connection conn = DatabaseConnection.getConnection();
         CallableStatement cstmt = conn.prepareCall(sql)) {
        
        cstmt.setString(1, tk.getTenDangNhap());
        
        // 🔑 BĂM MẬT KHẨU TẠI ĐÂY TRƯỚC KHI GỬI
        String hashedPassword = PasswordUtil.hashSHA256(tk.getMatKhau());
        cstmt.setString(2, hashedPassword);
        
        cstmt.setString(3, tk.getQuyen()); 
        cstmt.setString(4, tk.getEmail());
        
        cstmt.execute();
        return true; 
    } catch (SQLException e) {
        String errorMsg = cleanErrorMessage(e.getMessage());
        javax.swing.JOptionPane.showMessageDialog(null, errorMsg, "Lỗi Đăng Ký", javax.swing.JOptionPane.ERROR_MESSAGE);
        return false;
    }
}


/**
 * Hàm lọc bỏ các ký tự rác của hệ thống Oracle (ORA-2005X) 
 * Chỉ giữ lại chuỗi thông báo thuần túy để hiển thị lên UI cho người dùng đọc
 */
private String cleanErrorMessage(String rawMessage) {
    if (rawMessage == null) return "Kết nối cơ sở dữ liệu thất bại!";
    if (rawMessage.contains("ORA-")) {
        String[] parts = rawMessage.split("\n")[0].split(":", 3);
        if (parts.length >= 3) {
            return parts[2].trim();
        }
    }
    return rawMessage;
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