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
   // Sửa đổi: Thêm "throws Exception" để ném lỗi chuẩn từ Oracle về màn hình UI
public boolean themKhachHang(KhachHang kh) throws Exception {
    // Gọi thẳng tên Procedure của Oracle, chỉ truyền 5 tham số (Bỏ Mã KH)
    String sql = "{call SP_THEM_KHACHHANG(?, ?, ?, ?, ?)}";
    
    try (Connection conn = DatabaseConnection.getConnection();
         java.sql.CallableStatement cs = conn.prepareCall(sql)) {

        // Set các tham số theo đúng thứ tự khai báo trong Procedure
        cs.setString(1, kh.getTenKH());
        cs.setString(2, kh.getSdt()); // khớp với phương thức getSdt() của bạn
        cs.setInt(3, kh.getDiemTichLuy());
        cs.setString(4, kh.getEmail());
        cs.setString(5, kh.getTenDangNhap());

        // Thực thi Procedure dưới Oracle
        cs.execute();
        return true; 
    }
    // Bỏ khối catch ở đây để lỗi từ raise_application_error bắn thẳng về lớp Giao diện xử lý
}

   // Hàm phụ trợ được gọi bên form Lập Đơn Hàng (Chức năng y hệt hàm trên)
public boolean themKhachHangMoi(KhachHang kh) throws Exception {
    return themKhachHang(kh);
}
    // =======================================================
    // 4. SỬA THÔNG TIN KHÁCH HÀNG
    // =======================================================
    public boolean suaKhachHang(KhachHang kh)  throws Exception{
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
    public boolean xoaKhachHang(String maKH) throws Exception {
    String sql = "{call SP_XOA_KHACHHANG(?)}";

    try (Connection conn = DatabaseConnection.getConnection();
         java.sql.CallableStatement cs = conn.prepareCall(sql)) {

        cs.setString(1, maKH);
        cs.execute();
        return true; // Trả về true nếu Oracle chạy mượt mà không vấp phải error nào
    }
}
}