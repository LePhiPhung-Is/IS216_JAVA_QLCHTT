    package src.model;

    public class SanPham {

        private String maSP;
        private String maDM;
        private String mauSac;
        private String kichCo;
        private double giaBan;
        private int soLuongTon;
        private String trangThai;
        private String tenSP;
        private String maKho;
        private String hinhAnh; // BẮT BUỘC THÊM để load ảnh lên UI

        // Constructor rỗng
        public SanPham() {
        }

        // Constructor đầy đủ 10 tham số
        public SanPham(String maSP, String maDM, String mauSac, 
                    String kichCo, double giaBan, int soLuongTon, 
                    String trangThai, String tenSP, String maKho, String hinhAnh) {
            this.maSP = maSP;
            this.maDM = maDM;
            this.mauSac = mauSac;
            this.kichCo = kichCo;
            this.giaBan = giaBan;
            this.soLuongTon = soLuongTon;
            this.trangThai = trangThai;
            this.tenSP = tenSP;
            this.maKho = maKho;
            this.hinhAnh = hinhAnh;
        }

        // --- GETTER & SETTER ---
        public String getMaSP() { return maSP; }
        public void setMaSP(String maSP) { this.maSP = maSP; }

        public String getMaDM() { return maDM; }
        public void setMaDM(String maDM) { this.maDM = maDM; }

        public String getMauSac() { return mauSac; }
        public void setMauSac(String mauSac) { this.mauSac = mauSac; }

        public String getKichCo() { return kichCo; }
        public void setKichCo(String kichCo) { this.kichCo = kichCo; }

        public double getGiaBan() { return giaBan; }
        public void setGiaBan(double giaBan) { this.giaBan = giaBan; }

        public int getSoLuongTon() { return soLuongTon; }
        public void setSoLuongTon(int soLuongTon) { this.soLuongTon = soLuongTon; }

        public String getTrangThai() { return trangThai; }
        public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

        public String getTenSP() { return tenSP; }
        public void setTenSP(String tenSP) { this.tenSP = tenSP; }

        public String getMaKho() { return maKho; }
        public void setMaKho(String maKho) { this.maKho = maKho; }

        public String getHinhAnh() { return hinhAnh; }
        public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

        @Override
        public String toString() {
            return "SanPham{" + "maSP='" + maSP + '\'' + ", tenSP='" + tenSP + '\'' + 
                ", giaBan=" + giaBan + ", soLuongTon=" + soLuongTon + '}';
        }
    }