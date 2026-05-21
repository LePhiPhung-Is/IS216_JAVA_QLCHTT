package src.dao;

import src.model.KhuyenMai;
import src.database.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiDAO {

    // 1. Lấy toàn bộ danh sách khuyến mãi
    public List<KhuyenMai> getAllKhuyenMai() {
        List<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhuyenMai km = new KhuyenMai();
                km.setMaKM(rs.getString("MaKM").trim()); // Thêm trim() cho sạch dữ liệu
                km.setTenKM(rs.getString("TenKM"));
                km.setPhanTramGiam(rs.getDouble("PhanTramGiam"));
                km.setGiaTriToiThieu(rs.getDouble("GiaTriToiThieu"));
                km.setGiamToiDa(rs.getDouble("GiamToiDa"));
                km.setNgayBatDau(rs.getDate("NgayBatDau"));
                km.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                km.setSoLuotDung(rs.getLong("SoLuotDung"));
                km.setTrangThai(rs.getString("TrangThai"));
                list.add(km);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Lấy khuyến mãi theo Mã
    public KhuyenMai getById(String maKM) {
        // Thêm TRIM để chống lỗi dư dấu cách trong DB
        String sql = "SELECT * FROM KhuyenMai WHERE TRIM(MaKM) = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKM.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    KhuyenMai km = new KhuyenMai();
                    km.setMaKM(rs.getString("MaKM").trim());
                    km.setTenKM(rs.getString("TenKM"));
                    km.setPhanTramGiam(rs.getDouble("PhanTramGiam"));
                    km.setGiaTriToiThieu(rs.getDouble("GiaTriToiThieu"));
                    km.setGiamToiDa(rs.getDouble("GiamToiDa"));
                    km.setNgayBatDau(rs.getDate("NgayBatDau"));
                    km.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                    km.setSoLuotDung(rs.getLong("SoLuotDung"));
                    km.setTrangThai(rs.getString("TrangThai"));
                    return km;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 3. Thêm mới khuyến mãi
        // Sửa lại insertKhuyenMai trong DAO
public boolean insertKhuyenMai(KhuyenMai km) {
    String sql = "{call SP_THEM_KHUYENMAI(?, ?, ?, ?, ?, ?, ?, ?, ?)}"; // 9 tham số
    try (Connection conn = DatabaseConnection.getConnection();
         CallableStatement cs = conn.prepareCall(sql)) {

        cs.setString(1, km.getMaKM());        // Người dùng tự nhập VD: FREE60
        cs.setString(2, km.getTenKM());
        cs.setDouble(3, km.getPhanTramGiam());
        cs.setDouble(4, km.getGiaTriToiThieu());
        cs.setDouble(5, km.getGiamToiDa());
        cs.setDate(6, new java.sql.Date(km.getNgayBatDau().getTime()));
        cs.setDate(7, new java.sql.Date(km.getNgayKetThuc().getTime()));
        cs.setLong(8, km.getSoLuotDung());
        cs.setString(9, km.getTrangThai());

        cs.execute();
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e.getMessage(), e);
    }
}

    // 4. Cập nhật khuyến mãi
    public boolean updateKhuyenMai(KhuyenMai km) {
        String sql = "UPDATE KhuyenMai SET TenKM=?, PhanTramGiam=?, GiaTriToiThieu=?, GiamToiDa=?, NgayBatDau=?, NgayKetThuc=?, SoLuotDung=?, TrangThai=? WHERE TRIM(MaKM)=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, km.getTenKM());
            ps.setDouble(2, km.getPhanTramGiam());
            ps.setDouble(3, km.getGiaTriToiThieu());
            ps.setDouble(4, km.getGiamToiDa());
            ps.setDate(5, new java.sql.Date(km.getNgayBatDau().getTime()));
            ps.setDate(6, new java.sql.Date(km.getNgayKetThuc().getTime()));
            ps.setLong(7, km.getSoLuotDung());
            ps.setString(8, km.getTrangThai());
            ps.setString(9, km.getMaKM().trim());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. Xóa khuyến mãi
    public boolean deleteKhuyenMai(String maKM) {
    String sql = "{call SP_XOA_KHUYENMAI(?)}";  // ← tên procedure của bạn
    try (Connection con = DatabaseConnection.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {
        cs.setString(1, maKM);
        cs.execute();
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e.getMessage(), e);
    }
}

    // 6. Lấy khuyến mãi hợp lệ
    public KhuyenMai getKhuyenMaiHopLe(String maKM) {
        // Đã sửa lại chữ 'Đang diễn ra' cho khớp với DB và thêm TRIM
        String sql = "SELECT * FROM KHUYENMAI WHERE TRIM(MaKM) = ? " +
                     "AND TrangThai = 'Đang diễn ra' " +
                     "AND SYSDATE BETWEEN NgayBatDau AND NgayKetThuc " +
                     "AND SoLuotDung > 0";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maKM.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    KhuyenMai km = new KhuyenMai();
                    km.setMaKM(rs.getString("MaKM").trim());
                    km.setTenKM(rs.getString("TenKM"));
                    km.setPhanTramGiam(rs.getDouble("PhanTramGiam"));
                    km.setGiaTriToiThieu(rs.getDouble("GiaTriToiThieu"));
                    km.setGiamToiDa(rs.getDouble("GiamToiDa"));
                    km.setNgayBatDau(rs.getDate("NgayBatDau"));
                    km.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                    km.setSoLuotDung(rs.getLong("SoLuotDung")); 
                    km.setTrangThai(rs.getString("TrangThai"));
                    return km;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}