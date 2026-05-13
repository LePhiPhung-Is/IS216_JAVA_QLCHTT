package src.dao;

import src.database.DatabaseConnection;
import src.model.SanPham;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {

    // =======================================================
    // 1. HÀM LẤY TOÀN BỘ SẢN PHẨM (Dùng cho màn hình Quản lý)
    // =======================================================
    public List<SanPham> getAllSanPham() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT MASP, MADM, MAUSAC, KICHCO, GIABAN, SOLUONGTON, TRANGTHAI, TENSP, MAKHO, HINHANH FROM SANPHAM"; 

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String ma = rs.getString("MASP");
                String maDM = rs.getString("MADM");
                String mauSac = rs.getString("MAUSAC");
                String kichCo = rs.getString("KICHCO");
                double gia = rs.getDouble("GIABAN");
                int tonKho = rs.getInt("SOLUONGTON");
                String trangThai = rs.getString("TRANGTHAI");
                String ten = rs.getString("TENSP");
                String maKho = rs.getString("MAKHO");
                
                String anh = rs.getString("HINHANH");
                if (anh == null || anh.trim().isEmpty()) {
                    anh = "no_image.png"; 
                }

                // Gắn 10 tham số vào object SanPham
                SanPham sp = new SanPham(ma, maDM, mauSac, kichCo, gia, tonKho, trangThai, ten, maKho, anh);
                list.add(sp);
            }
        } catch (Exception e) {
            System.out.println("Lỗi DAO: Không thể lấy danh sách Sản Phẩm từ Database!");
            e.printStackTrace();
        }
        return list;
    }

    // =======================================================
    // 2. HÀM TÌM KIẾM SẢN PHẨM (Dùng cho Lập Đơn & Đổi Trả)
    // =======================================================
    public SanPham timKiemSanPham(String tuKhoa) {
        SanPham sp = null;
        // Tìm theo Mã (chính xác) hoặc Tên SP (gần đúng bằng LIKE)
        String sql = "SELECT MASP, MADM, MAUSAC, KICHCO, GIABAN, SOLUONGTON, TRANGTHAI, TENSP, MAKHO, HINHANH " +
                     "FROM SANPHAM WHERE UPPER(MASP) = ? OR UPPER(TENSP) LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            // Chuyển từ khóa thành IN HOA để tìm kiếm không bị phân biệt chữ hoa/thường
            pst.setString(1, tuKhoa.toUpperCase());
            pst.setString(2, "%" + tuKhoa.toUpperCase() + "%");

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String ma = rs.getString("MASP");
                    String maDM = rs.getString("MADM");
                    String mauSac = rs.getString("MAUSAC");
                    String kichCo = rs.getString("KICHCO");
                    double gia = rs.getDouble("GIABAN");
                    int tonKho = rs.getInt("SOLUONGTON");
                    String trangThai = rs.getString("TRANGTHAI");
                    String ten = rs.getString("TENSP");
                    String maKho = rs.getString("MAKHO");
                    
                    String anh = rs.getString("HINHANH");
                    if (anh == null || anh.trim().isEmpty()) {
                        anh = "no_image.png"; 
                    }

                    sp = new SanPham(ma, maDM, mauSac, kichCo, gia, tonKho, trangThai, ten, maKho, anh);
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi DAO: Không thể tìm kiếm Sản Phẩm!");
            e.printStackTrace();
        }
        return sp; // Trả về null nếu không tìm thấy món nào
    }
}