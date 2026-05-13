package src.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import src.dao.DanhMucDAO;

public class DanhMuc {

    private String maDM;
    private String tenDM;

    // Danh sách sản phẩm thuộc danh mục
    private List<SanPham> danhSachSanPham;

    // Constructor rỗng
    public DanhMuc() {
        danhSachSanPham = new ArrayList<>();
    }

    // Constructor đầy đủ
    public DanhMuc(String maDM, String tenDM) {
        this.maDM = maDM;
        this.tenDM = tenDM;
        this.danhSachSanPham = new ArrayList<>();
    }

    // ===== Getter & Setter =====
    public String getMaDM() {
        return maDM;
    }

    public void setMaDM(String maDM) {
        this.maDM = maDM;
    }

    public String getTenDM() {
        return tenDM;
    }

    public void setTenDM(String tenDM) {
        this.tenDM = tenDM;
    }

    public List<SanPham> getDanhSachSanPham() {
        return danhSachSanPham;
    }

    public void setDanhSachSanPham(List<SanPham> danhSachSanPham) {
        this.danhSachSanPham = danhSachSanPham;
    }

    // ========================
    // BUSINESS LOGIC
    // ========================

    // Thêm sản phẩm vào danh mục
    public void themSanPham(SanPham sp) {
        if (sp != null) {
            sp.setMaDM(this.maDM); // Gán đúng danh mục
            danhSachSanPham.add(sp);
        }
    }

    // Xóa sản phẩm khỏi danh mục
    public void xoaSanPham(SanPham sp) {
        if (sp != null) {
            danhSachSanPham.remove(sp);
            sp.setMaDM(null); // bỏ khỏi danh mục
        }
    }

    // Ẩn sản phẩm (soft delete)
    public void anSanPham(SanPham sp) {
        if (sp != null) {
            sp.setTrangThai("AN"); // bạn đang dùng String → OK
        }
    }
    // 
   
    // Lấy sản phẩm còn hoạt động
    public List<SanPham> getSanPhamDangBan() {
        List<SanPham> list = new ArrayList<>();
        for (SanPham sp : danhSachSanPham) {
            if (!"AN".equalsIgnoreCase(sp.getTrangThai())) {
                list.add(sp);
            }
        }
        return list;
    }

    // Lấy ComboBox cho danh mục ====================================
        @Override
    public String toString() {
        return this.getTenDM(); // HIỂN THỊ TÊN
    }
    private void loadDanhMucToCombo(JComboBox<DanhMuc> combo) {
    combo.removeAllItems();

    List<DanhMuc> list = new DanhMucDAO().getAllDanhMuc();

    for (DanhMuc dm : list) {
        combo.addItem(dm);
    }
}

//================================================
}