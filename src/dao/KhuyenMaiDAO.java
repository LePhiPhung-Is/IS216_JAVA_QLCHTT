package src.dao;

import src.model.KhuyenMai;
import src.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class KhuyenMaiDAO {
    
    public KhuyenMai getKhuyenMaiHopLe(String maKM) {
        // Kiểm tra mã: Đang chạy, còn hạn, còn lượt dùng
        String sql = "SELECT * FROM KHUYENMAI WHERE MaKM = ? " +
                     "AND TrangThai = 'Đang chạy' " +
                     "AND SYSDATE BETWEEN NgayBatDau AND NgayKetThuc " +
                     "AND SoLuotDung > 0";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maKM);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                KhuyenMai km = new KhuyenMai();
                km.setMaKM(rs.getString("MaKM"));
                km.setTenKM(rs.getString("TenKM"));
                km.setPhanTramGiam(rs.getDouble("PhanTramGiam"));
                km.setGiaTriToiThieu(rs.getDouble("GiaTriToiThieu"));
                km.setGiamToiDa(rs.getDouble("GiamToiDa"));
                km.setNgayBatDau(rs.getDate("NgayBatDau"));
                km.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                km.setSoLuotDung(rs.getInt("SoLuotDung"));
                km.setTrangThai(rs.getString("TrangThai"));
                return km;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}