package src.model;

public class ChiTietDonHang {
    private String maDH;
    private String maSP;
    private int soLuong;
    private double donGia;
    // Bỏ qua thành tiền vì có thể tính bằng SoLuong * DonGia

    public ChiTietDonHang(String maDH, String maSP, int soLuong, double donGia) {
        this.maDH = maDH;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    // Getter & Setter
    public String getMaDH() { return maDH; }
    public void setMaDH(String maDH) { this.maDH = maDH; }
    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }
}
