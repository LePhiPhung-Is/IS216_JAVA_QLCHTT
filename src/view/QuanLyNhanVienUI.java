package src.view;

import src.dao.NhanVienDAO;
import src.model.NhanVien;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuanLyNhanVienUI extends JPanel {

    // ===== MÀU GIAO DIỆN =====
    private final Color MAIN_BG      = new Color(244, 247, 246);
    private final Color BRAND_GOLD   = new Color(212, 175, 55);
    private final Color SIDEBAR_BG   = new Color(5, 5, 5);
    private final Color ROW_WHITE    = Color.WHITE;
    private final Color ROW_STRIPE   = new Color(250, 252, 251);
    private final Color BORDER_COLOR = new Color(220, 225, 222);
    private final Color DELETE_RED   = new Color(220, 60, 60);
    private final Color EDIT_BLUE    = new Color(50, 120, 200);
    private final Color HEADER_BG    = new Color(30, 30, 30);

    // ===== DỮ LIỆU =====
    private List<NhanVien> nhanViens = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;

    private static final String[] COLUMNS  = {"Mã NV", "Họ tên", "SĐT", "Chức vụ", "Trạng thái", "Thao tác"};
    private static final int      COL_ACTION = 5;

    // =====================================================================
    public QuanLyNhanVienUI() {
        setLayout(new BorderLayout());
        setBackground(MAIN_BG);
        setBorder(new EmptyBorder(28, 32, 28, 32));

        add(buildHeader(),     BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);

        loadDataFromDatabase();
    }

    // =====================================================================
    //  LOAD DATA
    // =====================================================================
    private void loadDataFromDatabase() {
        nhanViens = new NhanVienDAO().getAllNhanVien();
        filterTable();
    }

    // =====================================================================
    //  HEADER
    // =====================================================================
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAIN_BG);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("QUẢN LÝ NHÂN VIÊN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(SIDEBAR_BG);

        JLabel subtitle = new JLabel("Danh sách toàn bộ nhân sự tại cửa hàng");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(130, 130, 130));

        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setBackground(MAIN_BG);
        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(subtitle);

        // Tìm kiếm
        searchField = buildSearchField();

        JButton btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setBackground(BRAND_GOLD);
        btnTimKiem.setForeground(Color.BLACK);
        btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.addActionListener(e -> filterTable());

        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchBox.setBackground(MAIN_BG);
        searchBox.add(searchField);
        searchBox.add(btnTimKiem);

        JButton addBtn = makeGoldButton("+ Thêm nhân viên");
        addBtn.addActionListener(e -> openNhanVienDialog(null));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(MAIN_BG);
        rightPanel.add(searchBox);
        rightPanel.add(addBtn);

        header.add(titleBox,   BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        return header;
    }

    private JTextField buildSearchField() {
        final String PLACEHOLDER      = "Nhập mã hoặc tên nhân viên...";
        final Color  PLACEHOLDER_COLOR = new Color(160, 160, 160);

        JTextField field = new JTextField(20) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);

                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g3 = (Graphics2D) getGraphics();
                    g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g3.setFont(getFont().deriveFont(Font.ITALIC));
                    g3.setColor(PLACEHOLDER_COLOR);
                    Insets ins = getInsets();
                    int y = (getHeight() + g3.getFontMetrics().getAscent() - g3.getFontMetrics().getDescent()) / 2;
                    g3.drawString(PLACEHOLDER, ins.left, y);
                    g3.dispose();
                }
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(7, 12, 7, 12)));
        field.setOpaque(false);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) { field.repaint(); }
            @Override public void focusLost(java.awt.event.FocusEvent e)   { field.repaint(); }
        });
        field.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filterTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });
        return field;
    }

    // =====================================================================
    //  BẢNG NHÂN VIÊN
    // =====================================================================
    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == COL_ACTION; }
        };

        table = new JTable(tableModel) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (isRowSelected(row)) {
                    c.setBackground(new Color(212, 175, 55, 50));
                    c.setForeground(SIDEBAR_BG);
                } else {
                    c.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
                    c.setForeground(SIDEBAR_BG);
                }
                return c;
            }
        };
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(60);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(new Color(212, 175, 55, 50));
        table.setSelectionForeground(SIDEBAR_BG);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFocusable(false);

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(HEADER_BG);
        th.setForeground(BRAND_GOLD);
        th.setPreferredSize(new Dimension(0, 44));
        th.setBorder(BorderFactory.createEmptyBorder());
        th.setReorderingAllowed(false);

        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
        centerR.setHorizontalAlignment(SwingConstants.CENTER);

        table.getColumnModel().getColumn(0).setCellRenderer(centerR);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setCellRenderer(centerR);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setCellRenderer(centerR);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setCellRenderer(centerR);
        table.getColumnModel().getColumn(4).setPreferredWidth(130);
        table.getColumnModel().getColumn(COL_ACTION).setPreferredWidth(180);

        table.getColumnModel().getColumn(COL_ACTION).setCellRenderer(new ActionCellRenderer());
        table.getColumnModel().getColumn(COL_ACTION).setCellEditor(new ActionCellEditor());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scroll.getViewport().setBackground(ROW_WHITE);
        return scroll;
    }

    private void filterTable() {
        String kw = searchField.getText().trim().toLowerCase();
        List<NhanVien> filtered = new ArrayList<>();
        for (NhanVien nv : nhanViens) {
            if (nv.getMaNV().toLowerCase().contains(kw) ||
                nv.getTenNV().toLowerCase().contains(kw))
                filtered.add(nv);
        }
        refreshTableModel(filtered);
    }

    private void refreshTableModel(List<NhanVien> data) {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        for (NhanVien nv : data) {
            String trangThaiStr = nv.getTrangThai() == 1 ? "Đang làm việc" : "Đã nghỉ";
            tableModel.addRow(new Object[]{
                nv.getMaNV(), nv.getTenNV(), nv.getSdt(),
                nv.getChucVu(), trangThaiStr, ""
            });
        }
    }

    // =====================================================================
    //  DIALOG THÊM / SỬA
    //  ✅ Thêm mới: ẩn hoàn toàn field Mã NV (DB tự sinh qua FC_TAO_MA_NV)
    //  ✅ Sửa:      hiện Mã NV ở chế độ read-only để người dùng biết đang sửa ai
    // =====================================================================
    private void openNhanVienDialog(NhanVien existing) {
        boolean isEdit = (existing != null);

        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner instanceof Frame ? (Frame) owner : null,
                isEdit ? "Sửa thông tin nhân viên" : "Thêm nhân viên mới", true);
        dialog.setSize(750, isEdit ? 460 : 420); // thêm mới nhỏ hơn vì bỏ 1 dòng
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(MAIN_BG);

        // ----- Title bar -----
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleBar.setBackground(HEADER_BG);
        titleBar.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel dlgTitle = new JLabel(isEdit ? "SỬA THÔNG TIN NHÂN VIÊN" : "THÊM NHÂN VIÊN MỚI");
        dlgTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dlgTitle.setForeground(BRAND_GOLD);
        titleBar.add(dlgTitle);

        // ----- Form -----
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(MAIN_BG);
        form.setBorder(new EmptyBorder(20, 30, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // --- Khởi tạo các field ---
        JTextField txtMaNV = makeFormField(isEdit ? existing.getMaNV() : "");
        txtMaNV.setEditable(false);
        txtMaNV.setBackground(new Color(220, 220, 220));

        JTextField txtTenNV   = makeFormField(isEdit ? existing.getTenNV() : "");
        JTextField txtSDT     = makeFormField(isEdit ? existing.getSdt()   : "");
        JTextField txtDiaChi  = makeFormField(isEdit ? existing.getDiaChi(): "");
        JTextField txtLogin   = makeFormField(isEdit ? existing.getTenDangNhap() : "");

        JDateChooser dcNgaySinh = new JDateChooser();
        dcNgaySinh.setDateFormatString("yyyy-MM-dd");
        dcNgaySinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (isEdit && existing.getNgaySinh() != null)
            dcNgaySinh.setDate(existing.getNgaySinh());

        JDateChooser dcNgayVaoLam = new JDateChooser();
        dcNgayVaoLam.setDateFormatString("yyyy-MM-dd");
        dcNgayVaoLam.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (isEdit && existing.getNgayVaoLam() != null)
            dcNgayVaoLam.setDate(existing.getNgayVaoLam());
        else
            dcNgayVaoLam.setDate(new java.util.Date());

        JComboBox<String> cbGioiTinh  = makeCombo("Nam", "Nữ");
        JComboBox<String> cbChucVu    = makeCombo("Nhân viên bán hàng", "Quản lý", "Nhân viên kho");
        JComboBox<String> cbTrangThai = makeCombo("Đang làm việc", "Đã nghỉ");

        if (isEdit) {
            if (existing.getGioiTinh()  != null) cbGioiTinh.setSelectedItem(existing.getGioiTinh());
            if (existing.getChucVu()    != null) cbChucVu.setSelectedItem(existing.getChucVu());
            cbTrangThai.setSelectedIndex(existing.getTrangThai() == 1 ? 0 : 1);
        }

        // --- Bố trí form ---
        int row = 0;

        // Dòng Mã NV — chỉ hiện khi đang SỬA
        if (isEdit) {
            addFormRow(form, gbc, row, "Mã NV:", txtMaNV, "Tên NV:", txtTenNV);
            row++;
        } else {
            // Thêm mới: chỉ hiện Tên NV ở hàng đầu, chiếm cả 2 cột
            gbc.gridy = row; gbc.gridx = 0; gbc.weightx = 0.15;
            form.add(makeLabel("Tên NV:"), gbc);
            gbc.gridx = 1; gbc.weightx = 0.35;
            form.add(txtTenNV, gbc);
            // Bỏ trống 2 cột còn lại hoặc đặt field khác
            gbc.gridx = 2; gbc.weightx = 0.15; form.add(makeLabel("SĐT:"), gbc);
            gbc.gridx = 3; gbc.weightx = 0.35; form.add(txtSDT, gbc);
            row++;
        }

        if (isEdit) {
            addFormRow(form, gbc, row++, "SĐT:",      txtSDT,     "Địa chỉ:",  txtDiaChi);
        } else {
            addFormRow(form, gbc, row++, "Địa chỉ:", txtDiaChi, "User Login:", txtLogin);
        }

        addFormRow(form, gbc, row++, "Ngày sinh:",   dcNgaySinh,  "Giới tính:", cbGioiTinh);
        addFormRow(form, gbc, row++, "Chức vụ:",     cbChucVu,    "Ngày vào làm:", dcNgayVaoLam);
        addFormRow(form, gbc, row++, "Trạng thái:",  cbTrangThai, "User Login:", txtLogin);

        // ----- Nút Lưu / Hủy -----
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(MAIN_BG);
        btnRow.setBorder(new EmptyBorder(0, 30, 20, 30));

        JButton cancelBtn = makePlainButton("Hủy");
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton saveBtn = makeGoldButton(isEdit ? "Lưu thay đổi" : "Thêm mới");
        saveBtn.addActionListener(e -> {
            // Validate
            if (txtTenNV.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập Tên nhân viên.",
                        "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (txtSDT.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập Số điện thoại.",
                        "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (dcNgaySinh.getDate() == null || dcNgayVaoLam.getDate() == null) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn Ngày sinh và Ngày vào làm.",
                        "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Build object
            NhanVien nv = new NhanVien();
            if (isEdit) nv.setMaNV(existing.getMaNV()); // chỉ set khi sửa
            nv.setTenNV(txtTenNV.getText().trim());
            nv.setNgaySinh(dcNgaySinh.getDate());
            nv.setGioiTinh(cbGioiTinh.getSelectedItem().toString());
            nv.setSdt(txtSDT.getText().trim());
            nv.setDiaChi(txtDiaChi.getText().trim());
            nv.setChucVu(cbChucVu.getSelectedItem().toString());
            nv.setNgayVaoLam(dcNgayVaoLam.getDate());
            nv.setTrangThai(cbTrangThai.getSelectedIndex() == 0 ? 1 : 0);
            String login = txtLogin.getText().trim();
            nv.setTenDangNhap(login.isEmpty() ? null : login);

            // Gọi DAO
            NhanVienDAO dao = new NhanVienDAO();
            boolean ok = isEdit ? dao.updateNhanVien(nv) : dao.insertNhanVien(nv);

            if (ok) {
                JOptionPane.showMessageDialog(dialog,
                        isEdit ? "Cập nhật nhân viên thành công!" : "Thêm nhân viên thành công!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                loadDataFromDatabase();
            } else {
                // DAO đã log lỗi chi tiết ra console
                // Hiện thông báo thân thiện cho người dùng tùy loại thao tác
                String msg = isEdit
                        ? "Cập nhật thất bại! Số điện thoại có thể đã tồn tại."
                        : "Thêm nhân viên thất bại! Số điện thoại có thể đã tồn tại.";
                JOptionPane.showMessageDialog(dialog, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRow.add(cancelBtn);
        btnRow.add(saveBtn);

        dialog.add(titleBar, BorderLayout.NORTH);
        dialog.add(new JScrollPane(form), BorderLayout.CENTER);
        dialog.add(btnRow,   BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // =====================================================================
    //  XÓA NHÂN VIÊN
    // =====================================================================
    private void deleteNhanVien(String maNV, String tenNV) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xóa nhân viên \"" + tenNV + "\" (" + maNV + ")?\nHành động này không thể hoàn tác.",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = new NhanVienDAO().deleteNhanVien(maNV);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Đã xóa nhân viên thành công!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadDataFromDatabase();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Không thể xóa! Nhân viên này có thể đang có dữ liệu liên quan (đơn hàng, phiếu nhập...).",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // =====================================================================
    //  RENDERER & EDITOR — CỘT THAO TÁC
    // =====================================================================
    class ActionCellRenderer implements TableCellRenderer {
        private final JPanel  panel   = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 15));
        private final JButton editBtn = makeActionBtn("Sửa", EDIT_BLUE);
        private final JButton delBtn  = makeActionBtn("Xóa", DELETE_RED);

        ActionCellRenderer() {
            panel.setOpaque(true);
            panel.add(editBtn);
            panel.add(delBtn);
        }

        @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean focus, int row, int col) {
            panel.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
            return panel;
        }
    }

    class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 15));
        private int currentRow;

        ActionCellEditor() {
            JButton editBtn = makeActionBtn("✏ Sửa", EDIT_BLUE);
            editBtn.addActionListener(e -> {
                fireEditingStopped();
                String maNV = (String) tableModel.getValueAt(currentRow, 0);
                for (NhanVien nv : nhanViens) {
                    if (nv.getMaNV().equals(maNV)) {
                        openNhanVienDialog(nv);
                        break;
                    }
                }
            });

            JButton delBtn = makeActionBtn("Xóa", DELETE_RED);
            delBtn.addActionListener(e -> {
                fireEditingStopped();
                String maNV  = (String) tableModel.getValueAt(currentRow, 0);
                String tenNV = (String) tableModel.getValueAt(currentRow, 1);
                deleteNhanVien(maNV, tenNV);
            });

            panel.add(editBtn);
            panel.add(delBtn);
        }

        @Override public Component getTableCellEditorComponent(
                JTable t, Object v, boolean sel, int row, int col) {
            currentRow = row;
            panel.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
            return panel;
        }

        @Override public Object getCellEditorValue() { return ""; }
    }

    // =====================================================================
    //  HELPER — UI COMPONENTS
    // =====================================================================
    private JTextField makeFormField(String value) {
        JTextField f = new JTextField(value, 18);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(6, 10, 6, 10)));
        return f;
    }

    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(60, 60, 60));
        return lbl;
    }

    private JComboBox<String> makeCombo(String... items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBackground(Color.WHITE);
        return cb;
    }

    // Thêm 1 hàng form gồm: label + field + label + field
    private void addFormRow(JPanel form, GridBagConstraints gbc, int row,
                            String lbl1, JComponent comp1,
                            String lbl2, JComponent comp2) {
        gbc.gridy = row;
        gbc.gridx = 0; gbc.weightx = 0.15; form.add(makeLabel(lbl1), gbc);
        gbc.gridx = 1; gbc.weightx = 0.35; form.add(comp1, gbc);
        gbc.gridx = 2; gbc.weightx = 0.15; form.add(makeLabel(lbl2), gbc);
        gbc.gridx = 3; gbc.weightx = 0.35; form.add(comp2, gbc);
    }

    private JButton makeGoldButton(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? BRAND_GOLD.darker() : BRAND_GOLD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(9, 20, 9, 20));
        return b;
    }

    private JButton makePlainButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setForeground(new Color(80, 80, 80));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(8, 20, 8, 20)));
        return b;
    }

    private JButton makeActionBtn(String text, Color bg) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(5, 12, 5, 12));
        return b;
    }
}