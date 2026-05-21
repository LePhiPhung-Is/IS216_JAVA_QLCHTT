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
    //Gọi PROCEDURE để thêm sản phẩm
    public boolean insertSanPham(SanPham sp) {
    // Chỉ còn 9 dấu hỏi chấm vì mã sản phẩm do DB tự lo
    String sql = "{call SP_THEM_SANPHAM(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    try (Connection conn = DatabaseConnection.getConnection();
         java.sql.CallableStatement cs = conn.prepareCall(sql)) {

        // Tham số 1 lúc này dịch chuyển thành MaDM
        cs.setString(1, sp.getMaDM());
        cs.setString(2, sp.getMaKho());
        cs.setString(3, sp.getTenSP());
        cs.setString(4, sp.getMauSac());
        cs.setString(5, sp.getKichCo());
        cs.setDouble(6, sp.getGiaBan());
        cs.setInt(7, sp.getSoLuongTon());
        cs.setString(8, sp.getTrangThai());
        cs.setString(9, sp.getHinhAnh());

        cs.execute();
        return true;

    } catch (Exception e) {
        System.out.println("Lỗi gọi thủ tục thêm sản phẩm tự động mã!");
        e.printStackTrace();
        return false;
    }
}
    public boolean updateSanPham(SanPham sp) {
    // Thứ tự 10 dấu chấm hỏi khớp chính xác với khai báo tham số trong SP_SUA_SANPHAM
    String sql = "{call SP_SUA_SANPHAM(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    try (Connection conn = DatabaseConnection.getConnection();
         java.sql.CallableStatement cs = conn.prepareCall(sql)) {

        cs.setString(1, sp.getMaSP());
        cs.setString(2, sp.getMaDM());
        cs.setString(3, sp.getMaKho());
        cs.setString(4, sp.getTenSP());
        cs.setString(5, sp.getMauSac());
        cs.setString(6, sp.getKichCo());
        cs.setDouble(7, sp.getGiaBan());
        cs.setInt(8, sp.getSoLuongTon());
        cs.setString(9, sp.getTrangThai());
        cs.setString(10, sp.getHinhAnh());

        cs.execute();
        return true;

    } catch (Exception e) {
        System.out.println("Lỗi khi gọi Procedure SP_SUA_SANPHAM!");
        e.printStackTrace();
        return false;
    }
}
 public SanPham timKiemSanPham(String tuKhoa) {
        SanPham sp = null;
        // Tìm theo Mã (chính xác) hoặc Tên SP (gần đúng)
        String sql = "SELECT MASP, MADM, MAUSAC, KICHCO, GIABAN, SOLUONGTON, TRANGTHAI, TENSP, MAKHO, HINHANH " +
                     "FROM SANPHAM WHERE UPPER(MASP) = ? OR UPPER(TENSP) LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            // Truyền tham số (Chuyển hết thành in hoa để tìm kiếm không phân biệt hoa thường)
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
        return sp; // Trả về null nếu không tìm thấy
    }

    // Gọi PROCEDURE xóa sản phẩm
public boolean deleteSanPham(String maSP) {
    String sql = "{call SP_XOA_SANPHAM(?)}";

    try (Connection conn = DatabaseConnection.getConnection();
         java.sql.CallableStatement cs = conn.prepareCall(sql)) {

        // Chỉ cần truyền duy nhất mã sản phẩm cần xóa
        cs.setString(1, maSP);

        cs.execute();
        return true;

    } catch (Exception e) {
        System.out.println("Lỗi khi gọi Procedure SP_XOA_SANPHAM!");
        e.printStackTrace();
        return false;
    }
}
}