package src.dao;

import src.model.DonHang;
import src.model.ChiTietDonHang;
import src.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DonHangDAO {

    public boolean taoDonHangTaiQuay(DonHang dh, List<ChiTietDonHang> listCTDH) {
        Connection conn = null;
        PreparedStatement psDonHang = null;
        PreparedStatement psChiTiet = null;
        PreparedStatement psCapNhatKho = null;

        try {
            conn = DatabaseConnection.getConnection();
            
            // TẮT AUTO-COMMIT ĐỂ BẮT ĐẦU TRANSACTION
            conn.setAutoCommit(false); 

            // ==========================================================
            // 1. LƯU ĐƠN HÀNG (Bảng DONHANG)
            // ==========================================================
            String sqlDH = "INSERT INTO DONHANG (MaDH, MaKH, MaNV, MaGiamGia, LoaiDon, TrangThai, TongTien, HinhThucThanhToan, DiemThuong, DiemSuDung, GhiChu) " +
                           "VALUES (?, ?, ?, ?, 'OFFLINE', 'Hoàn thành', ?, ?, ?, ?, ?)";
            psDonHang = conn.prepareStatement(sqlDH);
            psDonHang.setString(1, dh.getMaDH());
            psDonHang.setString(2, dh.getMaKH()); // Có thể null
            psDonHang.setString(3, dh.getMaNV());
            psDonHang.setString(4, dh.getMaGiamGia());
            psDonHang.setDouble(5, dh.getTongTien());
            psDonHang.setString(6, dh.getHinhThucThanhToan());
            psDonHang.setInt(7, dh.getDiemThuong());
            psDonHang.setInt(8, dh.getDiemSuDung());
            psDonHang.setString(9, dh.getGhiChu());
            
            psDonHang.executeUpdate();

            // ==========================================================
            // 2. LƯU CHI TIẾT ĐƠN HÀNG 
            // ĐÃ SỬA THÀNH: CHITIET_DONHANG (CÓ DẤU GẠCH DƯỚI)
            // ==========================================================
            String sqlCT = "INSERT INTO CHITIET_DONHANG (MaDH, MaSP, SoLuong, DonGia, ThanhTien) VALUES (?, ?, ?, ?, ?)";
            psChiTiet = conn.prepareStatement(sqlCT);

            // ==========================================================
            // 3. CẬP NHẬT TỒN KHO SẢN PHẨM 
            // (Đã sửa SoLuong thành SoLuongTon cho bảng SANPHAM)
            // ==========================================================
            String sqlKho = "UPDATE SANPHAM SET SoLuongTon = SoLuongTon - ? WHERE MaSP = ?";
            psCapNhatKho = conn.prepareStatement(sqlKho);

            // Duyệt qua giỏ hàng và Add vào Batch
            for (ChiTietDonHang ct : listCTDH) {
                // Set parameter cho chi tiết
                psChiTiet.setString(1, ct.getMaDH());
                psChiTiet.setString(2, ct.getMaSP());
                psChiTiet.setInt(3, ct.getSoLuong());
                psChiTiet.setDouble(4, ct.getDonGia());
                
                // Tính và gán Thành Tiền cho cột thứ 5
                double thanhTienMonHang = ct.getSoLuong() * ct.getDonGia();
                psChiTiet.setDouble(5, thanhTienMonHang); 
                
                psChiTiet.addBatch();

                // Set parameter cho tồn kho
                psCapNhatKho.setInt(1, ct.getSoLuong());
                psCapNhatKho.setString(2, ct.getMaSP());
                psCapNhatKho.addBatch();
            }

            // Thực thi Batch
            psChiTiet.executeBatch();
            psCapNhatKho.executeBatch();

            // NẾU MỌI THỨ THÀNH CÔNG -> LƯU VĨNH VIỄN VÀO DB
            conn.commit(); 
            return true;

        } catch (Exception e) {
            e.printStackTrace(); 
            
            // NẾU CÓ BẤT KỲ LỖI GÌ -> HOÀN TÁC TOÀN BỘ
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (psDonHang != null) psDonHang.close();
                if (psChiTiet != null) psChiTiet.close();
                if (psCapNhatKho != null) psCapNhatKho.close();
                if (conn != null) {
                    conn.setAutoCommit(true); 
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}