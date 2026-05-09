package src.model;
import java.util.Date;
public class DonHang {
    private String maDH;
    private String maKH;
    private String maNV;
    private String maDC;

    private Date ngayDat;
    private Date ngayGiaoDuKien;

    private String trangThai;

    private double tongTien;
    private double phiShip;

    private String maGiamGia;
    private String ghiChu;

    // Constructor rỗng
    public DonHang() {
    }

    // Constructor đầy đủ
    public DonHang(String maDH, String maKH,
                   String maNV, String maDC,
                   Date ngayDat, Date ngayGiaoDuKien,
                   String trangThai,
                   double tongTien,
                   double phiShip,
                   String maGiamGia,
                   String ghiChu) {

        this.maDH = maDH;
        this.maKH = maKH;
        this.maNV = maNV;
        this.maDC = maDC;
        this.ngayDat = ngayDat;
        this.ngayGiaoDuKien = ngayGiaoDuKien;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.phiShip = phiShip;
        this.maGiamGia = maGiamGia;
        this.ghiChu = ghiChu;
    }

    // Getter Setter

    public String getMaDH() {
        return maDH;
    }

    public void setMaDH(String maDH) {
        this.maDH = maDH;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaDC() {
        return maDC;
    }

    public void setMaDC(String maDC) {
        this.maDC = maDC;
    }

    public Date getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(Date ngayDat) {
        this.ngayDat = ngayDat;
    }

    public Date getNgayGiaoDuKien() {
        return ngayGiaoDuKien;
    }

    public void setNgayGiaoDuKien(Date ngayGiaoDuKien) {
        this.ngayGiaoDuKien = ngayGiaoDuKien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public double getPhiShip() {
        return phiShip;
    }

    public void setPhiShip(double phiShip) {
        this.phiShip = phiShip;
    }

    public String getMaGiamGia() {
        return maGiamGia;
    }

    public void setMaGiamGia(String maGiamGia) {
        this.maGiamGia = maGiamGia;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public String toString() {
        return "DonHang{" +
                "maDH='" + maDH + '\'' +
                ", tongTien=" + tongTien +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}
