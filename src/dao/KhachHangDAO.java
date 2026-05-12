package src.dao;

import src.model.KhachHang;
import src.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class KhachHangDAO {

    // Tìm khách hàng qua Số Điện Thoại
    public KhachHang getKhachHangBySDT(String sdt) {
        String sql = "SELECT * FROM KHACHHANG WHERE SDT = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, sdt);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new KhachHang(
                            rs.getString("MaKH"),
                            rs.getString("TenKH"),
                            rs.getString("SDT"),
                            rs.getInt("DiemTichLuy"),
                            rs.getString("Email"),
                            rs.getString("TenDangNhap")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm khách hàng mới
    public boolean themKhachHangMoi(KhachHang kh) {
        String sql = "INSERT INTO KHACHHANG (MaKH, TenKH, SDT, DiemTichLuy) VALUES (?, ?, ?, 0)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, kh.getMaKH());
            pst.setString(2, kh.getTenKH());
            pst.setString(3, kh.getSdt());
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
