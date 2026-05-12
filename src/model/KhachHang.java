package src.model;

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String sdt;
    private int diemTichLuy;
    private String email;
    private String tenDangNhap;

    public KhachHang() {}

    public KhachHang(String maKH, String tenKH, String sdt, int diemTichLuy, String email, String tenDangNhap) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.sdt = sdt;
        this.diemTichLuy = diemTichLuy;
        this.email = email;
        this.tenDangNhap = tenDangNhap;
    }

    // Getter & Setter
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }
    public String getTenKH() { return tenKH; }
    public void setTenKH(String tenKH) { this.tenKH = tenKH; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public int getDiemTichLuy() { return diemTichLuy; }
    public void setDiemTichLuy(int diemTichLuy) { this.diemTichLuy = diemTichLuy; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
}