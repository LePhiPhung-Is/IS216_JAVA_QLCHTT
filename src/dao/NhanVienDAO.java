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

        String sql =
                "INSERT INTO NHANVIEN " +
                "(MaNV, TenNV, NgaySinh, GioiTinh, SDT, DiaChi, " +
                "ChucVu, NgayVaoLam, TrangThai, TenDangNhap) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, nv.getMaNV());

            ps.setString(2, nv.getTenNV());

            ps.setDate(
                    3,
                    new java.sql.Date(
                            nv.getNgaySinh().getTime()
                    )
            );

            ps.setString(4, nv.getGioiTinh());

            ps.setString(5, nv.getSdt());

            ps.setString(6, nv.getDiaChi());

            ps.setString(7, nv.getChucVu());

            ps.setDate(
                    8,
                    new java.sql.Date(
                            nv.getNgayVaoLam().getTime()
                    )
            );

            ps.setInt(9, nv.getTrangThai());

            if (
                    nv.getTenDangNhap() == null
                    || nv.getTenDangNhap().trim().isEmpty()
            ) {

                ps.setNull(10, Types.VARCHAR);

            } else {

                ps.setString(10, nv.getTenDangNhap());
            }

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // UPDATE
    // =====================================================
    public boolean updateNhanVien(NhanVien nv) {

        String sql =
                "UPDATE NHANVIEN SET " +
                "TenNV=?, NgaySinh=?, GioiTinh=?, SDT=?, " +
                "DiaChi=?, ChucVu=?, NgayVaoLam=?, " +
                "TrangThai=?, TenDangNhap=? " +
                "WHERE MaNV=?";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, nv.getTenNV());

            ps.setDate(
                    2,
                    new java.sql.Date(
                            nv.getNgaySinh().getTime()
                    )
            );

            ps.setString(3, nv.getGioiTinh());

            ps.setString(4, nv.getSdt());

            ps.setString(5, nv.getDiaChi());

            ps.setString(6, nv.getChucVu());

            ps.setDate(
                    7,
                    new java.sql.Date(
                            nv.getNgayVaoLam().getTime()
                    )
            );

            ps.setInt(8, nv.getTrangThai());

            if (
                    nv.getTenDangNhap() == null
                    || nv.getTenDangNhap().trim().isEmpty()
            ) {

                ps.setNull(9, Types.VARCHAR);

            } else {

                ps.setString(9, nv.getTenDangNhap());
            }

            ps.setString(10, nv.getMaNV());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // DELETE
    // =====================================================
    public boolean deleteNhanVien(String maNV) {

        String sql =
                "DELETE FROM NHANVIEN WHERE MaNV=?";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, maNV);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}