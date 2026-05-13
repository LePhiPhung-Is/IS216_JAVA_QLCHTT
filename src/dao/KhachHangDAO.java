package src.dao;

import src.database.DatabaseConnection;
import src.model.KhachHang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    // =======================================================
    // 1. LẤY TOÀN BỘ KHÁCH HÀNG (Dùng cho Form Quản Lý)
    // =======================================================
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT MAKH, TENKH, SDT, DIEMTICHLUY, EMAIL, TENDANGNHAP FROM KHACHHANG";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhachHang kh = new KhachHang(
                        rs.getString("MAKH"),
                        rs.getString("TENKH"),
                        rs.getString("SDT"),
                        rs.getInt("DIEMTICHLUY"),
                        rs.getString("EMAIL"),
                        rs.getString("TENDANGNHAP")
                );
                list.add(kh);
            }
        } catch (Exception e) {
            System.out.println("Lỗi DAO: Không thể lấy danh sách Khách Hàng!");
            e.printStackTrace();
        }
        return list;
    }

    // =======================================================
    // 2. TÌM KHÁCH HÀNG THEO SĐT (Dùng cho Form Lập Đơn Hàng)
    // =======================================================
    public KhachHang getKhachHangBySDT(String sdt) {
        String sql = "SELECT MAKH, TENKH, SDT, DIEMTICHLUY, EMAIL, TENDANGNHAP FROM KHACHHANG WHERE SDT = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sdt);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhachHang(
                            rs.getString("MAKH"),
                            rs.getString("TENKH"),
                            rs.getString("SDT"),
                            rs.getInt("DIEMTICHLUY"),
                            rs.getString("EMAIL"),
                            rs.getString("TENDANGNHAP")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi DAO: Không thể tìm Khách Hàng theo SĐT!");
            e.printStackTrace();
        }
        return null; // Trả về null nếu số điện thoại chưa được đăng ký
    }

    // =======================================================
    // 3. THÊM KHÁCH HÀNG MỚI
    // =======================================================
    public boolean themKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KHACHHANG (MAKH, TENKH, SDT, DIEMTICHLUY, EMAIL, TENDANGNHAP) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getSdt());
            ps.setInt(4, kh.getDiemTichLuy());
            ps.setString(5, kh.getEmail());
            ps.setString(6, kh.getTenDangNhap());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi DAO: Không thể thêm Khách Hàng!");
            e.printStackTrace();
            return false;
        }
    }

    // Hàm phụ trợ được gọi bên form Lập Đơn Hàng (Chức năng y hệt hàm trên)
    public boolean themKhachHangMoi(KhachHang kh) {
        return themKhachHang(kh);
    }

    // =======================================================
    // 4. SỬA THÔNG TIN KHÁCH HÀNG
    // =======================================================
    public boolean suaKhachHang(KhachHang kh) {
        String sql = "UPDATE KHACHHANG SET TENKH = ?, SDT = ?, DIEMTICHLUY = ?, EMAIL = ?, TENDANGNHAP = ? WHERE MAKH = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getSdt());
            ps.setInt(3, kh.getDiemTichLuy());
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getTenDangNhap());
            ps.setString(6, kh.getMaKH());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi DAO: Không thể cập nhật Khách Hàng!");
            e.printStackTrace();
            return false;
        }
    }

    // =======================================================
    // 5. XÓA KHÁCH HÀNG
    // =======================================================
    public boolean xoaKhachHang(String maKH) {
        String sql = "DELETE FROM KHACHHANG WHERE MAKH = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi DAO: Không thể xóa Khách Hàng (Có thể bị dính khóa ngoại với Đơn hàng)!");
            e.printStackTrace();
            throw new RuntimeException("Lỗi khóa ngoại khi xóa KH", e); 
        }
    }
}