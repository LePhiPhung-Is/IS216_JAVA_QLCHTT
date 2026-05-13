package src.model;

import java.util.Date;

public class DonHang {
    private String maDH;
    private String maKH;
    private String maNV;
    private String maDC;
    private String maGiamGia;
    private String loaiDon;
    private Date ngayDat;
    private Date ngayGiaoDuKien;
    private String trangThai;
    private double tongTien;
    private double phiShip;
    private String hinhThucThanhToan;
    private int diemThuong;
    private int diemSuDung;
    private String ghiChu;

    public DonHang() {
        this.loaiDon = "OFFLINE"; // Mặc định bán tại quầy
        this.phiShip = 0;
        this.trangThai = "Hoàn thành";
    }

    // Khai báo Getter và Setter cho toàn bộ thuộc tính ở trên...
    public String getMaDH() { return maDH; }
    public void setMaDH(String maDH) { this.maDH = maDH; }
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }
    public String getMaDC() { return maDC; }
    public void setMaDC(String maDC) { this.maDC = maDC; }
    public String getMaGiamGia() { return maGiamGia; }
    public void setMaGiamGia(String maGiamGia) { this.maGiamGia = maGiamGia; }
    public String getLoaiDon() { return loaiDon; }
    public void setLoaiDon(String loaiDon) { this.loaiDon = loaiDon; }
    public Date getNgayDat() { return ngayDat; }
    public void setNgayDat(Date ngayDat) { this.ngayDat = ngayDat; }
    public Date getNgayGiaoDuKien() { return ngayGiaoDuKien; }
    public void setNgayGiaoDuKien(Date ngayGiaoDuKien) { this.ngayGiaoDuKien = ngayGiaoDuKien; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }
    public double getPhiShip() { return phiShip; }
    public void setPhiShip(double phiShip) { this.phiShip = phiShip; }
    public String getHinhThucThanhToan() { return hinhThucThanhToan; }
    public void setHinhThucThanhToan(String hinhThucThanhToan) { this.hinhThucThanhToan = hinhThucThanhToan; }
    public int getDiemThuong() { return diemThuong; }
    public void setDiemThuong(int diemThuong) { this.diemThuong = diemThuong; }
    public int getDiemSuDung() { return diemSuDung; }
    public void setDiemSuDung(int diemSuDung) { this.diemSuDung = diemSuDung; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}