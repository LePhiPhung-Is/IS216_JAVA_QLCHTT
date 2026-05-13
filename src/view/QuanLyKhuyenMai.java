    package src.view;

    import src.dao.KhuyenMaiDAO;
    import src.model.KhuyenMai;

    import javax.swing.*;
    import javax.swing.border.EmptyBorder;
    import javax.swing.table.DefaultTableModel;
    import java.awt.*;
    import java.util.Date;
    import java.util.List;

    import com.toedter.calendar.JDateChooser;

    public class QuanLyKhuyenMai extends JPanel {

        // ===================== COMPONENTS =====================
        private JTextField txtTimKiem;
        private JComboBox<String> cbLocTrangThai;
        private JButton btnTimKiem, btnThem, btnSua, btnXoa;
        private JTable table;
        private DefaultTableModel tableModel;
        private JLabel lblTongKhuyenMai;

        private final KhuyenMaiDAO dao = new KhuyenMaiDAO();

        // ===================== CONSTRUCTOR =====================
        public QuanLyKhuyenMai() {
            setLayout(new BorderLayout(15, 15));
            setBorder(new EmptyBorder(20, 20, 20, 20));
            setBackground(new Color(244, 247, 246));
            initComponents();
            loadData();
            setupTableColor();
        }

        // ===================== INIT UI =====================
        private void initComponents() {

            // --- HEADER (TÌM KIẾM + NÚT) ---
            JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            pnlHeader.setOpaque(false);

            txtTimKiem = new JTextField(16);
            txtTimKiem.setPreferredSize(new Dimension(180, 30));
            txtTimKiem.setToolTipText("Tìm theo mã hoặc tên khuyến mãi");

            cbLocTrangThai = new JComboBox<>(new String[]{"Tất cả", "Đang áp dụng", "Hết hạn", "Tạm ngưng"});
            cbLocTrangThai.setPreferredSize(new Dimension(110, 30));

            btnTimKiem = taoNut("Tìm kiếm", Color.BLACK, Color.WHITE);
            btnThem    = taoNut("+ Thêm",   new Color(34, 139, 34), Color.WHITE);
            btnSua     = taoNut("Sửa",      new Color(212, 175, 55), Color.BLACK);
            btnXoa     = taoNut("Xóa",      new Color(180, 30, 30), Color.WHITE);

            pnlHeader.add(new JLabel("Tìm kiếm:"));
            pnlHeader.add(txtTimKiem);
            pnlHeader.add(new JLabel("Trạng thái:"));
            pnlHeader.add(cbLocTrangThai);
            pnlHeader.add(btnTimKiem);
            pnlHeader.add(Box.createHorizontalStrut(20));
            pnlHeader.add(btnThem);
            pnlHeader.add(btnSua);
            pnlHeader.add(btnXoa);

            add(pnlHeader, BorderLayout.NORTH);

            // --- BẢNG ---
            String[] cols = {"STT", "Mã KM", "Tên Khuyến Mãi", "% Giảm", "GT Tối Thiểu (đ)",
                            "Giảm Tối Đa (đ)", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Lượt Dùng", "Trạng Thái"};
            tableModel = new DefaultTableModel(cols, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

            table = new JTable(tableModel);
            table.setRowHeight(30);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
            table.getTableHeader().setBackground(new Color(230, 230, 230));
            table.setSelectionBackground(new Color(212, 175, 55, 80));
            table.setSelectionForeground(Color.BLACK);
            table.setGridColor(new Color(220, 220, 220));

            // Căn cột
            table.getColumnModel().getColumn(0).setPreferredWidth(40);
            table.getColumnModel().getColumn(1).setPreferredWidth(80);
            table.getColumnModel().getColumn(2).setPreferredWidth(180);
            table.getColumnModel().getColumn(3).setPreferredWidth(60);
            table.getColumnModel().getColumn(9).setPreferredWidth(90);

            add(new JScrollPane(table), BorderLayout.CENTER);

            // --- FOOTER ---
            lblTongKhuyenMai = new JLabel("Tổng số khuyến mãi: 0");
            lblTongKhuyenMai.setFont(new Font("Segoe UI", Font.BOLD, 15));
            add(lblTongKhuyenMai, BorderLayout.SOUTH);

            // --- SỰ KIỆN ---
            btnTimKiem.addActionListener(e -> timKiem());
            cbLocTrangThai.addActionListener(e -> timKiem());
            btnThem.addActionListener(e -> moFormThem());
            btnSua.addActionListener(e -> moFormSua());
            btnXoa.addActionListener(e -> xoaKhuyenMai());
        }

        // ===================== HELPER: TẠO NÚT =====================
        private JButton taoNut(String text, Color bgColor, Color fgColor) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setBackground(bgColor);
            btn.setForeground(fgColor);
            btn.setPreferredSize(new Dimension(100, 30));
            btn.setContentAreaFilled(true);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            return btn;
        }

        // ===================== LOAD DỮ LIỆU =====================
        private void loadData() {
            tableModel.setRowCount(0);
            List<KhuyenMai> list = dao.getAllKhuyenMai();
            hienThiDanhSach(list);
        }

        private void hienThiDanhSach(List<KhuyenMai> list) {
            tableModel.setRowCount(0);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            int stt = 1;
            for (KhuyenMai km : list) {
                tableModel.addRow(new Object[]{
                    stt++,
                    km.getMaKM(),
                    km.getTenKM(),
                    km.getPhanTramGiam() + "%",
                    String.format("%,.0f", km.getGiaTriToiThieu()),
                    String.format("%,.0f", km.getGiamToiDa()),
                    km.getNgayBatDau() != null ? sdf.format(km.getNgayBatDau()) : "",
                    km.getNgayKetThuc() != null ? sdf.format(km.getNgayKetThuc()) : "",
                    km.getSoLuotDung(),
                    km.getTrangThai()
                });
            }
            lblTongKhuyenMai.setText("Tổng số khuyến mãi: " + list.size());
        }

        // ===================== TÌM KIẾM / LỌC =====================
        private void timKiem() {
            String keyword = txtTimKiem.getText().trim().toLowerCase();
            String trangThai = (String) cbLocTrangThai.getSelectedItem();

            List<KhuyenMai> all = dao.getAllKhuyenMai();
            List<KhuyenMai> filtered = new java.util.ArrayList<>();

            for (KhuyenMai km : all) {
                boolean matchKW = keyword.isEmpty()
                        || km.getMaKM().toLowerCase().contains(keyword)
                        || km.getTenKM().toLowerCase().contains(keyword);
                boolean matchTT = trangThai.equals("Tất cả")
                        || km.getTrangThai().equalsIgnoreCase(trangThai);
                if (matchKW && matchTT) filtered.add(km);
            }
            hienThiDanhSach(filtered);
        }

        // ===================== LẤY DÒNG ĐANG CHỌN =====================
        private KhuyenMai layDongDangChon() {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một khuyến mãi!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            String maKM = tableModel.getValueAt(row, 1).toString();
            return dao.getById(maKM);
        }

        // ===================== FORM THÊM =====================
        private void moFormThem() {
            KhuyenMaiForm form = new KhuyenMaiForm(null, null);
            form.setVisible(true);
            KhuyenMai km = form.getKetQua();
            if (km != null) {
                if (dao.insertKhuyenMai(km)) {
                    JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công!");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại! Kiểm tra lại dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // ===================== FORM SỬA =====================
        private void moFormSua() {
            KhuyenMai cu = layDongDangChon();
            if (cu == null) return;

            KhuyenMaiForm form = new KhuyenMaiForm(null, cu);
            form.setVisible(true);
            KhuyenMai km = form.getKetQua();
            if (km != null) {
                if (dao.updateKhuyenMai(km)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // ===================== XÓA =====================
        private void xoaKhuyenMai() {
            KhuyenMai km = layDongDangChon();
            if (km == null) return;

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa khuyến mãi \"" + km.getTenKM() + "\"?",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.deleteKhuyenMai(km.getMaKM())) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // =========================================================
        // ===================== INNER CLASS: FORM =================
        // =========================================================
        static class KhuyenMaiForm extends JDialog {

            private KhuyenMai ketQua = null;

            // Input fields
            private JTextField txtMaKM, txtTenKM;
            private JSpinner spnPhanTram, spnGiaTriToiThieu, spnGiamToiDa, spnSoLuot;
            private JDateChooser dcBatDau, dcKetThuc;
            private JComboBox<String> cbTrangThai;

            public KhuyenMaiForm(Frame owner, KhuyenMai km) {
                super(owner, km == null ? "Thêm Khuyến Mãi" : "Sửa Khuyến Mãi", true);
                setSize(480, 520);
                setLocationRelativeTo(owner);
                setResizable(false);
                initForm(km);
            }

            private void initForm(KhuyenMai km) {
                boolean isSua = (km != null);

                JPanel pnlMain = new JPanel(new GridBagLayout());
                pnlMain.setBorder(new EmptyBorder(20, 25, 10, 25));
                pnlMain.setBackground(Color.WHITE);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(8, 5, 8, 5);
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.HORIZONTAL;

                Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
                Font inputFont = new Font("Segoe UI", Font.PLAIN, 13);

                // --- Mã KM ---
                gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
                pnlMain.add(label("Mã KM:", labelFont), gbc);
                txtMaKM = new JTextField(isSua ? km.getMaKM() : "");
                txtMaKM.setFont(inputFont);
                txtMaKM.setEnabled(!isSua); // Không cho sửa mã khi update
                if (isSua) txtMaKM.setBackground(new Color(240, 240, 240));
                gbc.gridx = 1; gbc.weightx = 0.7;
                pnlMain.add(txtMaKM, gbc);

                // --- Tên KM ---
                gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
                pnlMain.add(label("Tên Khuyến Mãi:", labelFont), gbc);
                txtTenKM = new JTextField(isSua ? km.getTenKM() : "");
                txtTenKM.setFont(inputFont);
                gbc.gridx = 1; gbc.weightx = 0.7;
                pnlMain.add(txtTenKM, gbc);

                // --- % Giảm (Spinner 0–100) ---
                gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
                pnlMain.add(label("% Giảm (0–100):", labelFont), gbc);
                spnPhanTram = new JSpinner(new SpinnerNumberModel(
                        isSua ? km.getPhanTramGiam() : 10.0, 0.0, 100.0, 0.5));
                spnPhanTram.setFont(inputFont);
                gbc.gridx = 1; gbc.weightx = 0.7;
                pnlMain.add(spnPhanTram, gbc);

                // --- Giá trị tối thiểu ---
                gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
                pnlMain.add(label("GT Đơn Tối Thiểu (đ):", labelFont), gbc);
                spnGiaTriToiThieu = new JSpinner(new SpinnerNumberModel(
                        isSua ? km.getGiaTriToiThieu() : 100000.0, 0.0, 99999999.0, 10000.0));
                spnGiaTriToiThieu.setFont(inputFont);
                gbc.gridx = 1; gbc.weightx = 0.7;
                pnlMain.add(spnGiaTriToiThieu, gbc);

                // --- Giảm tối đa ---
                gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
                pnlMain.add(label("Giảm Tối Đa (đ):", labelFont), gbc);
                spnGiamToiDa = new JSpinner(new SpinnerNumberModel(
                        isSua ? km.getGiamToiDa() : 50000.0, 0.0, 99999999.0, 10000.0));
                spnGiamToiDa.setFont(inputFont);
                gbc.gridx = 1; gbc.weightx = 0.7;
                pnlMain.add(spnGiamToiDa, gbc);

                // --- Ngày bắt đầu ---
                gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.3;
                pnlMain.add(label("Ngày Bắt Đầu:", labelFont), gbc);
                dcBatDau = new JDateChooser();
                dcBatDau.setDateFormatString("dd/MM/yyyy");
                dcBatDau.setFont(inputFont);
                if (isSua && km.getNgayBatDau() != null) dcBatDau.setDate(km.getNgayBatDau());
                gbc.gridx = 1; gbc.weightx = 0.7;
                pnlMain.add(dcBatDau, gbc);

                // --- Ngày kết thúc ---
                gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0.3;
                pnlMain.add(label("Ngày Kết Thúc:", labelFont), gbc);
                dcKetThuc = new JDateChooser();
                dcKetThuc.setDateFormatString("dd/MM/yyyy");
                dcKetThuc.setFont(inputFont);
                if (isSua && km.getNgayKetThuc() != null) dcKetThuc.setDate(km.getNgayKetThuc());
                gbc.gridx = 1; gbc.weightx = 0.7;
                pnlMain.add(dcKetThuc, gbc);

                // --- Số lượt dùng ---
                gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0.3;
                pnlMain.add(label("Số Lượt Dùng:", labelFont), gbc);
                spnSoLuot = new JSpinner(new SpinnerNumberModel(
                        isSua ? (int) km.getSoLuotDung() : 100, 0, 999999, 10));
                spnSoLuot.setFont(inputFont);
                gbc.gridx = 1; gbc.weightx = 0.7;
                pnlMain.add(spnSoLuot, gbc);

                // --- Trạng thái (ComboBox, không phải TextField) ---
                gbc.gridx = 0; gbc.gridy = 8; gbc.weightx = 0.3;
                pnlMain.add(label("Trạng Thái:", labelFont), gbc);
                cbTrangThai = new JComboBox<>(new String[]{"Đang áp dụng", "Ngừng áp dụng", "Tạm ngưng"});
                cbTrangThai.setFont(inputFont);
                if (isSua) cbTrangThai.setSelectedItem(km.getTrangThai());
                gbc.gridx = 1; gbc.weightx = 0.7;
                pnlMain.add(cbTrangThai, gbc);

                // --- BUTTONS ---
                JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
                pnlBtn.setBackground(Color.WHITE);

                JButton btnHuy = new JButton("Hủy");
                btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 13));
                btnHuy.setPreferredSize(new Dimension(90, 32));

                JButton btnLuu = new JButton(isSua ? "Cập nhật" : "Thêm mới");
                btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 13));
                btnLuu.setBackground(Color.BLACK);
                btnLuu.setForeground(Color.WHITE);
                btnLuu.setOpaque(true);
                btnLuu.setContentAreaFilled(true);
                btnLuu.setBorderPainted(false);
                btnLuu.setPreferredSize(new Dimension(100, 32));

                pnlBtn.add(btnHuy);
                pnlBtn.add(btnLuu);

                btnHuy.addActionListener(e -> dispose());
                btnLuu.addActionListener(e -> luuDuLieu());

                // --- LAYOUT DIALOG ---
                setLayout(new BorderLayout());
                getContentPane().setBackground(Color.WHITE);

                // Tiêu đề nhỏ bên trong form
                JLabel lblTitle = new JLabel(isSua ? "  Chỉnh sửa thông tin khuyến mãi" : "  Nhập thông tin khuyến mãi mới");
                lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
                lblTitle.setForeground(new Color(212, 175, 55));
                lblTitle.setBorder(new EmptyBorder(12, 10, 5, 0));

                add(lblTitle, BorderLayout.NORTH);
                add(pnlMain, BorderLayout.CENTER);
                add(pnlBtn, BorderLayout.SOUTH);
            }

            // Helper tạo label
            private JLabel label(String text, Font font) {
                JLabel lbl = new JLabel(text);
                lbl.setFont(font);
                return lbl;
            }

            // Validate & lưu kết quả
            private void luuDuLieu() {
                String maKM   = txtMaKM.getText().trim();
                String tenKM  = txtTenKM.getText().trim();
                Date batDau   = dcBatDau.getDate();
                Date ketThuc  = dcKetThuc.getDate();

                if (maKM.isEmpty() || tenKM.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Mã KM và Tên KM không được để trống!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (batDau == null || ketThuc == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ ngày bắt đầu và kết thúc!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!ketThuc.after(batDau)) {
                    JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double phanTram       = (Double) spnPhanTram.getValue();
                double giaTriToiThieu = (Double) spnGiaTriToiThieu.getValue();
                double giamToiDa      = (Double) spnGiamToiDa.getValue();
                long   soLuot         = ((Integer) spnSoLuot.getValue()).longValue();
                String trangThai      = (String) cbTrangThai.getSelectedItem();

                ketQua = new KhuyenMai(maKM, tenKM, phanTram, giaTriToiThieu, giamToiDa,
                                    batDau, ketThuc, soLuot, trangThai);
                dispose();
            }

            public KhuyenMai getKetQua() { return ketQua; }
        }
        private void setupTableColor() {

    table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            String trangThai = table.getValueAt(row, 9).toString();

            java.util.Date now = new java.util.Date();
            String ngayKetThucStr = table.getValueAt(row, 7).toString();

            boolean hetHan = false;

            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                java.util.Date ngayKT = sdf.parse(ngayKetThucStr);
                hetHan = ngayKT.before(now);
            } catch (Exception e) {
                hetHan = false;
            }

            if (isSelected) {
                c.setBackground(new Color(212, 175, 55, 80));
                c.setForeground(Color.BLACK);
                return c;
            }

            // ===== LOGIC MÀU =====
            if (hetHan) {
                c.setForeground(new Color(180, 30, 30)); // đỏ
            }
            else if ("Đang áp dụng".equalsIgnoreCase(trangThai)) {
                c.setForeground(new Color(34, 139, 34)); // xanh
            }
            else if ("Tạm ngưng".equalsIgnoreCase(trangThai)) {
                c.setForeground(new Color(212, 175, 55)); // vàng
            }
            else {
                c.setForeground(Color.GRAY); // inactive
            }

            c.setBackground(Color.WHITE);
            return c;
        }
    });
}
    }