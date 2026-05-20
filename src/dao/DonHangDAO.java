package src.dao;

import src.database.DatabaseConnection;
import src.model.ChiTietDonHang;
import src.model.DonHang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class DonHangDAO {

    public boolean taoDonHangTaiQuay(DonHang dh, List<ChiTietDonHang> dsCTDH) {
        Connection con = null;
        PreparedStatement psDH = null;
        PreparedStatement psCT = null;

        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false); 

            // Câu lệnh SQL khớp 100% với cấu trúc bảng trong ảnh của bạn
            String sqlDH = "INSERT INTO DONHANG (MADH, MAKH, MANV, MAKM, LOAIDON, NGAYDAT, TRANGTHAI, TONGTIEN, HINHTHUCTHANHTOAN, DIEMTHUONG, DIEMSUDUNG, GHICHU) " +
                           "VALUES (?, ?, ?, ?, 'OFFLINE', SYSDATE, 'Đã hoàn thành', ?, ?, ?, ?, ?)";
            
            psDH = con.prepareStatement(sqlDH);
            psDH.setString(1, dh.getMaDH());
            psDH.setString(2, dh.getMaKH());
            psDH.setString(3, dh.getMaNV());
            psDH.setString(4, dh.getMaGiamGia());
            psDH.setDouble(5, dh.getTongTien());
            psDH.setString(6, dh.getHinhThucThanhToan());
            psDH.setInt(7, dh.getDiemThuong());
            psDH.setInt(8, dh.getDiemSuDung());
            psDH.setString(9, dh.getGhiChu());
            
            psDH.executeUpdate();

            // INSERT CHI TIẾT
            String sqlCT = "INSERT INTO CHITIETDONHANG (MADH, MASP, SOLUONG, DONGIA) VALUES (?, ?, ?, ?)";
            psCT = con.prepareStatement(sqlCT);
            for (ChiTietDonHang ct : dsCTDH) {
                psCT.setString(1, ct.getMaDH());
                psCT.setString(2, ct.getMaSP());
                psCT.setInt(3, ct.getSoLuong());
                psCT.setDouble(4, ct.getDonGia());
                psCT.addBatch();
            }
            psCT.executeBatch();

            con.commit();
            return true;

        } catch (Exception e) {
            if (con != null) try { con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            e.printStackTrace();
            System.err.println("LỖI SQL CHI TIẾT: " + e.getMessage());
            return false;
        } finally {
            try {
                if (psDH != null) psDH.close();
                if (psCT != null) psCT.close();
                if (con != null) con.close();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}