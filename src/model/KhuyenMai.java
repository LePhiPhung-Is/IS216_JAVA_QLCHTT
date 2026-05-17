package src.model;

import java.util.Date;

public class KhuyenMai {
    private String maKM;
    private String tenKM;
    private double phanTramGiam;
    private double giaTriToiThieu;
    private double giamToiDa;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private long soLuotDung; // Dùng long
    private String trangThai;

    // Constructor rỗng
    public KhuyenMai() {}

    // Constructor đầy đủ tham số
    public KhuyenMai(String maKM, String tenKM, double phanTramGiam, double giaTriToiThieu, 
                     double giamToiDa, Date ngayBatDau, Date ngayKetThuc, long soLuotDung, String trangThai) {
        this.maKM = maKM;
        this.tenKM = tenKM;
        this.phanTramGiam = phanTramGiam;
        this.giaTriToiThieu = giaTriToiThieu;
        this.giamToiDa = giamToiDa;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.soLuotDung = soLuotDung;
        this.trangThai = trangThai;
    }

    // ================= GETTERS & SETTERS =================
    
    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }

    public String getTenKM() { return tenKM; }
    public void setTenKM(String tenKM) { this.tenKM = tenKM; }

    public double getPhanTramGiam() { return phanTramGiam; }
    public void setPhanTramGiam(double phanTramGiam) { this.phanTramGiam = phanTramGiam; }

    public double getGiaTriToiThieu() { return giaTriToiThieu; }
    public void setGiaTriToiThieu(double giaTriToiThieu) { this.giaTriToiThieu = giaTriToiThieu; }

    public double getGiamToiDa() { return giamToiDa; }
    public void setGiamToiDa(double giamToiDa) { this.giamToiDa = giamToiDa; }

    public Date getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(Date ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public Date getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(Date ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public long getSoLuotDung() { return soLuotDung; }
    public void setSoLuotDung(long soLuotDung) { this.soLuotDung = soLuotDung; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}