package src.dao;

import src.database.DatabaseConnection;
import src.model.SanPham;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {

    public List<SanPham> getAllSanPham() {
        List<SanPham> list = new ArrayList<>();
        // Lấy đầy đủ các cột khớp với các bảng bạn đã thiết kế trong Oracle
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
            System.out.println("Lỗi DAO: Không thể lấy dữ liệu Sản Phẩm từ Database!");
            e.printStackTrace();
        }
        return list;
    }
    public boolean insertSanPham(SanPham sp) {
    String sql = "INSERT INTO SANPHAM (MASP, MADM, MAUSAC, KICHCO, GIABAN, SOLUONGTON, TRANGTHAI, TENSP, MAKHO, HINHANH) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, sp.getMaSP());
        ps.setString(2, sp.getMaDM());
        ps.setString(3, sp.getMauSac());
        ps.setString(4, sp.getKichCo());
        ps.setDouble(5, sp.getGiaBan());
        ps.setInt(6, sp.getSoLuongTon());
        ps.setString(7, sp.getTrangThai());
        ps.setString(8, sp.getTenSP());
        ps.setString(9, sp.getMaKho());
        ps.setString(10, sp.getHinhAnh());

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    public boolean updateSanPham(SanPham sp) {
    String sql = "UPDATE SANPHAM SET MADM=?, MAUSAC=?, KICHCO=?, GIABAN=?, SOLUONGTON=?, TRANGTHAI=?, TENSP=?, MAKHO=?, HINHANH=? WHERE MASP=?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, sp.getMaDM());
        ps.setString(2, sp.getMauSac());
        ps.setString(3, sp.getKichCo());
        ps.setDouble(4, sp.getGiaBan());
        ps.setInt(5, sp.getSoLuongTon());
        ps.setString(6, sp.getTrangThai());
        ps.setString(7, sp.getTenSP());
        ps.setString(8, sp.getMaKho());
        ps.setString(9, sp.getHinhAnh());
        ps.setString(10, sp.getMaSP());

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
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
public boolean deleteSanPham(String maSP) {
    String sql = "DELETE FROM SanPham WHERE MaSP = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, maSP);

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        throw new RuntimeException(e); // để UI catch
    }

   
}
}