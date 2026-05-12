package src.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class KhuyenMai {
    private String maKM;
    private String tenKM; // <-- Bạn phải có dòng này
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String trangThai;
    
    private Map<String, Double> chiTietGiamGia = new HashMap<>();

    public KhuyenMai() {}

    // ============ GETTER & SETTER CHO TenKM ============
    public String getTenKM() { 
        return tenKM; 
    }
    
    public void setTenKM(String tenKM) { 
        this.tenKM = tenKM; 
    }
    // ===================================================

    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }

    public Date getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(Date ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public Date getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(Date ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public Map<String, Double> getChiTietGiamGia() { return chiTietGiamGia; }
    
    public void themSanPhamGiamGia(String maSP, double phanTram) {
        this.chiTietGiamGia.put(maSP, phanTram);
    }
}