package src.model;

public class QuanLy {
    private String maQL;
    private String tenQL;
    private String taiKhoan;
    private String matKhau;
    private String vaiTro;

    
    public QuanLy() {
    }

    
    public QuanLy(String maQL, String tenQL, String taiKhoan, String matKhau, String vaiTro) {
        this.maQL = maQL;
        this.tenQL = tenQL;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
    }

    
    public String getMaQL() { return maQL; }
    public void setMaQL(String maQL) { this.maQL = maQL; }

    public String getTenQL() { return tenQL; }
    public void setTenQL(String tenQL) { this.tenQL = tenQL; }

    public String getTaiKhoan() { return taiKhoan; }
    public void setTaiKhoan(String taiKhoan) { this.taiKhoan = taiKhoan; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
}

