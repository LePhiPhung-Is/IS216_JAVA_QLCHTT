package src.model;

public class TaiKhoan {
    private String tenDangNhap;
    private String matKhau;
    private String quyen;
    private int trangThai;
    private String email; // Thêm trường Email

    public TaiKhoan() {}

    public TaiKhoan(String tenDangNhap, String matKhau, String quyen, int trangThai, String email) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.quyen = quyen;
        this.trangThai = trangThai;
        this.email = email;
    }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getQuyen() { return quyen; }
    public void setQuyen(String quyen) { this.quyen = quyen; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}