package src.dao;

import src.database.DatabaseConnection;
import src.model.NhanVien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    // =====================================================
    // GET ALL
    // =====================================================
    public List<NhanVien> getAllNhanVien() {

        List<NhanVien> list = new ArrayList<>();

        String sql = "SELECT * FROM NHANVIEN";

        try (
                Connection con = DatabaseConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)
        ) {

            while (rs.next()) {

                NhanVien nv = new NhanVien();

                nv.setMaNV(rs.getString("MaNV"));
                nv.setTenNV(rs.getString("TenNV"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setGioiTinh(rs.getString("GioiTinh"));
                nv.setSdt(rs.getString("SDT"));
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setChucVu(rs.getString("ChucVu"));
                nv.setNgayVaoLam(rs.getDate("NgayVaoLam"));
                nv.setTrangThai(rs.getInt("TrangThai"));
                nv.setTenDangNhap(rs.getString("TenDangNhap"));

                list.add(nv);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // INSERT
    // =====================================================
public boolean insertNhanVien(NhanVien nv) {
        String sql = "{call SP_THEM_NHANVIEN(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DatabaseConnection.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, nv.getTenNV());
            cs.setDate(2, new java.sql.Date(nv.getNgaySinh().getTime()));
            cs.setString(3, nv.getGioiTinh());
            cs.setString(4, nv.getSdt());
            cs.setString(5, nv.getDiaChi());
            cs.setString(6, nv.getChucVu());
            cs.setDate(7, new java.sql.Date(nv.getNgayVaoLam().getTime()));
            cs.setInt(8, nv.getTrangThai());

            if (nv.getTenDangNhap() == null || nv.getTenDangNhap().trim().isEmpty()) {
                cs.setNull(9, Types.VARCHAR);
            } else {
                cs.setString(9, nv.getTenDangNhap());
            }

            cs.execute();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    // =====================================================
    // UPDATE
    // =====================================================
    public boolean updateNhanVien(NhanVien nv) {
        String sql = "{call SP_SUA_NHANVIEN(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DatabaseConnection.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, nv.getMaNV());
            cs.setString(2, nv.getTenNV());
            cs.setDate(3, new java.sql.Date(nv.getNgaySinh().getTime()));
            cs.setString(4, nv.getGioiTinh());
            cs.setString(5, nv.getSdt());
            cs.setString(6, nv.getDiaChi());
            cs.setString(7, nv.getChucVu());
            cs.setDate(8, new java.sql.Date(nv.getNgayVaoLam().getTime()));
            cs.setInt(9, nv.getTrangThai());

            if (nv.getTenDangNhap() == null || nv.getTenDangNhap().trim().isEmpty()) {
                cs.setNull(10, Types.VARCHAR);
            } else {
                cs.setString(10, nv.getTenDangNhap());
            }

            cs.execute();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    // =====================================================
    // DELETE
    // =====================================================
    public boolean deleteNhanVien(String maNV) {
        String sql = "{call SP_XOA_NHANVIEN(?)}";
        try (Connection con = DatabaseConnection.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, maNV);
            cs.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}