package src.model;

public class TaiKhoan {
    private String tenDangNhap;
    private String matKhau;
    private String quyen;
    private int trangThai;

    public TaiKhoan() {}

    public TaiKhoan(String tenDangNhap, String matKhau, String quyen, int trangThai) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.quyen = quyen;
        this.trangThai = trangThai;
    }

    // Các hàm Getters và Setters
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getQuyen() { return quyen; }
    public void setQuyen(String quyen) { this.quyen = quyen; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }
}