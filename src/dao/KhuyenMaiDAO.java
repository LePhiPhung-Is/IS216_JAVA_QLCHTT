package src.dao;

import src.model.KhuyenMai;
import src.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class KhuyenMaiDAO {
    
    public KhuyenMai getKhuyenMaiHopLe(String maKM) {
        KhuyenMai km = null;
        // Dùng LEFT JOIN để lấy thông tin Khuyến Mãi và Chi Tiết của nó
        String sql = "SELECT km.MaKM, km.TenKM, km.NgayBatDau, km.NgayKetThuc, km.TrangThai, " +
                     "ct.MaSP, ct.PhanTramGiam " +
                     "FROM KHUYENMAI km " +
                     "LEFT JOIN CHITIET_KHUYENMAI ct ON km.MaKM = ct.MaKM " +
                     "WHERE km.MaKM = ? AND km.TrangThai = 'Hoạt động' " +
                     "AND SYSDATE BETWEEN km.NgayBatDau AND km.NgayKetThuc";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maKM);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                // Khởi tạo đối tượng KhuyenMai ở dòng dữ liệu đầu tiên
                if (km == null) {
                    km = new KhuyenMai();
                    km.setMaKM(rs.getString("MaKM"));
                    km.setTenKM(rs.getString("TenKM"));
                    km.setNgayBatDau(rs.getDate("NgayBatDau"));
                    km.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                    km.setTrangThai(rs.getString("TrangThai"));
                }
                
                // Đọc chi tiết sản phẩm được giảm (nếu có)
                String maSP = rs.getString("MaSP");
                if (maSP != null) {
                    double phanTram = rs.getDouble("PhanTramGiam");
                    km.themSanPhamGiamGia(maSP, phanTram);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return km; // Trả về null nếu mã sai/hết hạn, trả về đối tượng nếu hợp lệ
    }
}