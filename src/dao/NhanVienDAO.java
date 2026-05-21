package src.dao; // Khuyên dùng: Bỏ "src." nếu thư mục src đã là Source Root của dự án

import src.database.DatabaseConnection;
import src.model.NhanVien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane; // Thêm thư viện để thông báo trực tiếp lên giao diện Swing

public class NhanVienDAO {

    // =====================================================
    // GET ALL
    // =====================================================
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NHANVIEN";

        // Tách biệt rõ ràng Connection để quản lý tài nguyên tối ưu nhất
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

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
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi tải danh sách nhân viên: " + e.getMessage(), "Lỗi Database", JOptionPane.ERROR_MESSAGE);
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
            cs.setDate(2, nv.getNgaySinh() != null ? new java.sql.Date(nv.getNgaySinh().getTime()) : null);
            cs.setString(3, nv.getGioiTinh());
            cs.setString(4, nv.getSdt());
            cs.setString(5, nv.getDiaChi());
            cs.setString(6, nv.getChucVu());
            cs.setDate(7, nv.getNgayVaoLam() != null ? new java.sql.Date(nv.getNgayVaoLam().getTime()) : null);
            cs.setInt(8, nv.getTrangThai());

            if (nv.getTenDangNhap() == null || nv.getTenDangNhap().trim().isEmpty()) {
                cs.setNull(9, Types.VARCHAR);
            } else {
                cs.setString(9, nv.getTenDangNhap());
            }

            cs.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            // Lọc thông báo lỗi từ Oracle và hiển thị trực tiếp lên UI cho nhân viên đọc
            String friendlyError = cleanErrorMessage(e.getMessage());
            JOptionPane.showMessageDialog(null, friendlyError, "Lỗi Thêm Nhân Viên", JOptionPane.ERROR_MESSAGE);
            return false; 
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
            cs.setDate(3, nv.getNgaySinh() != null ? new java.sql.Date(nv.getNgaySinh().getTime()) : null);
            cs.setString(4, nv.getGioiTinh());
            cs.setString(5, nv.getSdt());
            cs.setString(6, nv.getDiaChi());
            cs.setString(7, nv.getChucVu());
            cs.setDate(8, nv.getNgayVaoLam() != null ? new java.sql.Date(nv.getNgayVaoLam().getTime()) : null);
            cs.setInt(9, nv.getTrangThai());

            if (nv.getTenDangNhap() == null || nv.getTenDangNhap().trim().isEmpty()) {
                cs.setNull(10, Types.VARCHAR);
            } else {
                cs.setString(10, nv.getTenDangNhap());
            }

            cs.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            String friendlyError = cleanErrorMessage(e.getMessage());
            JOptionPane.showMessageDialog(null, friendlyError, "Lỗi Cập Nhật Nhân Viên", JOptionPane.ERROR_MESSAGE);
            return false;
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
            
        } catch (SQLException e) {
            e.printStackTrace();
            String friendlyError = cleanErrorMessage(e.getMessage());
            JOptionPane.showMessageDialog(null, friendlyError, "Lỗi Xóa Nhân Viên", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // =====================================================
    // UTILITY: Lọc sạch chuỗi lỗi ORA nghiệp vụ
    // =====================================================
    private String cleanErrorMessage(String rawMessage) {
        if (rawMessage == null) return "Kết nối cơ sở dữ liệu thất bại hoặc có lỗi không xác định!";
        // Tìm và cắt chuỗi lỗi tự định nghĩa từ RAISE_APPLICATION_ERROR bên Oracle
        if (rawMessage.contains("ORA-")) {
            String[] parts = rawMessage.split("\n")[0].split(":", 3);
            if (parts.length >= 3) {
                return parts[2].trim();
            }
        }
        return rawMessage;
    }
}