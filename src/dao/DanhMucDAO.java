package src.dao;

import src.database.DatabaseConnection;
import src.model.DanhMuc;
import src.model.SanPham;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DanhMucDAO {

    // ===== LẤY TẤT CẢ DANH MỤC =====
    public List<DanhMuc> getAllDanhMuc() {
        List<DanhMuc> list = new ArrayList<>();

        // TODO: thay "DANHMUC", "MADM", "TENDM" đúng với tên bảng/cột Oracle của bạn
        String sql = "SELECT MADM, TENDM FROM DANHMUC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String maDM  = rs.getString("MADM");
                String tenDM = rs.getString("TENDM");

                DanhMuc dm = new DanhMuc(maDM, tenDM);

                // Nạp sản phẩm thuộc danh mục này
                dm.setDanhSachSanPham(getSanPhamTheoDM(conn, maDM));

                list.add(dm);
            }

        } catch (Exception e) {
            System.out.println("Lỗi DAO: Không thể lấy dữ liệu Danh Mục từ Database!");
            e.printStackTrace();
        }

        return list;
    }
    // ===== LẤY TẤT CẢ MÃ DANH MỤC =====
    public List<String> getAllMaDanhMuc() {
    List<String> list = new ArrayList<>();
    String sql = "SELECT MADM FROM DANHMUC";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {
            list.add(rs.getString("MADM"));
        }

    } catch (Exception e) {
        System.out.println("Lỗi DAO: Không thể lấy mã danh mục!");
        e.printStackTrace();
    }

    return list;
}
    // ===== LẤY SẢN PHẨM THEO DANH MỤC =====
    // Dùng chung Connection để tránh mở/đóng nhiều lần trong vòng lặp
    private List<SanPham> getSanPhamTheoDM(Connection conn, String maDM) {
        List<SanPham> list = new ArrayList<>();

        // TODO: thay tên cột cho khớp với Oracle của bạn (giống SanPhamDAO)
        String sql = "SELECT MASP, MADM, MAUSAC, KICHCO, GIABAN, SOLUONGTON, TRANGTHAI, TENSP, MAKHO, HINHANH " +
                     "FROM SANPHAM WHERE MADM = ?";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, maDM);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String anh = rs.getString("HINHANH");
                    if (anh == null || anh.trim().isEmpty()) anh = "no_image.png";

                    SanPham sp = new SanPham(
                        rs.getString("MASP"),
                        rs.getString("MADM"),
                        rs.getString("MAUSAC"),
                        rs.getString("KICHCO"),
                        rs.getDouble("GIABAN"),
                        rs.getInt("SOLUONGTON"),
                        rs.getString("TRANGTHAI"),
                        rs.getString("TENSP"),
                        rs.getString("MAKHO"),
                        anh
                    );
                    list.add(sp);
                }
            }

        } catch (Exception e) {
            System.out.println("Lỗi DAO: Không thể lấy sản phẩm của danh mục " + maDM);
            e.printStackTrace();
        }

        return list;
    }

    // ===== CẬP NHẬT TRẠNG THÁI SẢN PHẨM (ẩn / hiện) =====
    public boolean capNhatTrangThai(String maSP, String trangThai) {
        // TODO: thay tên bảng/cột cho đúng
        String sql = "UPDATE SANPHAM SET TRANGTHAI = ? WHERE MASP = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, trangThai);
            pst.setString(2, maSP);
            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Lỗi DAO: Không thể cập nhật trạng thái sản phẩm " + maSP);
            e.printStackTrace();
            return false;
        }
    }

    // ===== THÊM SẢN PHẨM VÀO DANH MỤC =====
    public boolean themSanPhamVaoDanhMuc(String maSP, String maDM) {
        // TODO: thay tên bảng/cột cho đúng
        String sql = "UPDATE SANPHAM SET MADM = ? WHERE MASP = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, maDM);
            pst.setString(2, maSP);
            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Lỗi DAO: Không thể thêm sản phẩm " + maSP + " vào danh mục " + maDM);
            e.printStackTrace();
            return false;
        }
    }
    // ===== XÓA DANH MỤC =====
public boolean delete(String maDM) {
    String sql = "DELETE FROM DANHMUC WHERE MADM = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, maDM);

        return pst.executeUpdate() > 0;

    } catch (Exception e) {
        System.out.println("Lỗi DAO: Không thể xóa danh mục " + maDM);
        e.printStackTrace();
        return false;
    }
}
public boolean update(DanhMuc dm) {
    String sql = "UPDATE DANHMUC SET TENDM = ? WHERE MADM = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, dm.getTenDM());
        ps.setString(2, dm.getMaDM());

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
}