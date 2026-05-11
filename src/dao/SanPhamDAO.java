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
}