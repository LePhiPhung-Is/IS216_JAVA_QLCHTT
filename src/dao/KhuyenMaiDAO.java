package src.dao;

import src.database.DatabaseConnection;
import src.model.KhuyenMai;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiDAO {

    // =================== GET ALL ===================
    public List<KhuyenMai> getAllKhuyenMai() {
        List<KhuyenMai> list = new ArrayList<>();

        String sql = "SELECT MAKM, TENKM, PHANTRAMGIAM, GIATRITOITHIEU, GIAMTOIDA, " +
                     "NGAYBATDAU, NGAYKETTHUC, SOLUOTDUNG, TRANGTHAI " +
                     "FROM KHUYENMAI";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                String maKM = rs.getString("MAKM");
                String tenKM = rs.getString("TENKM");

                double phanTramGiam = rs.getDouble("PHANTRAMGIAM");
                double giaTriToiThieu = rs.getDouble("GIATRITOITHIEU");
                double giamToiDa = rs.getDouble("GIAMTOIDA");

                java.util.Date ngayBatDau = rs.getDate("NGAYBATDAU");
                java.util.Date ngayKetThuc = rs.getDate("NGAYKETTHUC");

                long soLuotDung = rs.getLong("SOLUOTDUNG");

                String trangThai = rs.getString("TRANGTHAI");

                KhuyenMai km = new KhuyenMai(
                        maKM,
                        tenKM,
                        phanTramGiam,
                        giaTriToiThieu,
                        giamToiDa,
                        ngayBatDau,
                        ngayKetThuc,
                        soLuotDung,
                        trangThai
                );

                list.add(km);
            }

        } catch (Exception e) {
            System.out.println("Lỗi DAO: Không thể lấy dữ liệu Khuyến Mãi!");
            e.printStackTrace();
        }

        return list;
    }

    // =================== INSERT ===================
    public boolean insertKhuyenMai(KhuyenMai km) {

        String sql = "INSERT INTO KHUYENMAI " +
                "(MAKM, TENKM, PHANTRAMGIAM, GIATRITOITHIEU, GIAMTOIDA, " +
                "NGAYBATDAU, NGAYKETTHUC, SOLUOTDUNG, TRANGTHAI) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, km.getMaKM());
            pst.setString(2, km.getTenKM());
            pst.setDouble(3, km.getPhanTramGiam());
            pst.setDouble(4, km.getGiaTriToiThieu());
            pst.setDouble(5, km.getGiamToiDa());
            pst.setDate(6, new java.sql.Date(km.getNgayBatDau().getTime()));
            pst.setDate(7, new java.sql.Date(km.getNgayKetThuc().getTime()));
            pst.setLong(8, km.getSoLuotDung());
            pst.setString(9, km.getTrangThai());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Lỗi DAO: Insert Khuyến Mãi thất bại!");
            e.printStackTrace();
        }

        return false;
    }

    // =================== UPDATE ===================
    public boolean updateKhuyenMai(KhuyenMai km) {

        String sql = "UPDATE KHUYENMAI SET " +
                "TENKM=?, PHANTRAMGIAM=?, GIATRITOITHIEU=?, GIAMTOIDA=?, " +
                "NGAYBATDAU=?, NGAYKETTHUC=?, SOLUOTDUNG=?, TRANGTHAI=? " +
                "WHERE MAKM=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, km.getTenKM());
            pst.setDouble(2, km.getPhanTramGiam());
            pst.setDouble(3, km.getGiaTriToiThieu());
            pst.setDouble(4, km.getGiamToiDa());
            pst.setDate(5, new java.sql.Date(km.getNgayBatDau().getTime()));
            pst.setDate(6, new java.sql.Date(km.getNgayKetThuc().getTime()));
            pst.setLong(7, km.getSoLuotDung());
            pst.setString(8, km.getTrangThai());
            pst.setString(9, km.getMaKM());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Lỗi DAO: Update Khuyến Mãi thất bại!");
            e.printStackTrace();
        }

        return false;
    }

    // =================== DELETE ===================
    public boolean deleteKhuyenMai(String maKM) {

        String sql = "DELETE FROM KHUYENMAI WHERE MAKM = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, maKM);
            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Lỗi DAO: Delete Khuyến Mãi thất bại!");
            e.printStackTrace();
        }

        return false;
    }

    // =================== GET BY ID ===================
    public KhuyenMai getById(String maKM) {

        String sql = "SELECT * FROM KHUYENMAI WHERE MAKM = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, maKM);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                return new KhuyenMai(
                        rs.getString("MAKM"),
                        rs.getString("TENKM"),
                        rs.getDouble("PHANTRAMGIAM"),
                        rs.getDouble("GIATRITOITHIEU"),
                        rs.getDouble("GIAMTOIDA"),
                        rs.getDate("NGAYBATDAU"),
                        rs.getDate("NGAYKETTHUC"),
                        rs.getLong("SOLUOTDUNG"),
                        rs.getString("TRANGTHAI")
                );
            }

        } catch (Exception e) {
            System.out.println("Lỗi DAO: GetById Khuyến Mãi thất bại!");
            e.printStackTrace();
        }

        return null;
    }

    // =================== GET ACTIVE ===================
    public List<KhuyenMai> getActiveKhuyenMai() {

        List<KhuyenMai> list = new ArrayList<>();

        String sql = "SELECT * FROM KHUYENMAI " +
                "WHERE TRANGTHAI = 'ACTIVE' " +
                "AND SYSDATE BETWEEN NGAYBATDAU AND NGAYKETTHUC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                KhuyenMai km = new KhuyenMai(
                        rs.getString("MAKM"),
                        rs.getString("TENKM"),
                        rs.getDouble("PHANTRAMGIAM"),
                        rs.getDouble("GIATRITOITHIEU"),
                        rs.getDouble("GIAMTOIDA"),
                        rs.getDate("NGAYBATDAU"),
                        rs.getDate("NGAYKETTHUC"),
                        rs.getLong("SOLUOTDUNG"),
                        rs.getString("TRANGTHAI")
                );

                list.add(km);
            }

        } catch (Exception e) {
            System.out.println("Lỗi DAO: Get Active Khuyến Mãi thất bại!");
            e.printStackTrace();
        }

        return list;
    }
}