package src.view;

import src.dao.NhanVienDAO;
import src.model.NhanVien;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
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

    private List<NhanVien> nhanViens = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 

    private static final String[] COLUMNS = {"Mã NV", "Họ tên", "SĐT", "Chức vụ", "Trạng thái", "Thao tác"};
    private static final int COL_ACTION = 5;

    public QuanLyNhanVienUI() {
        setLayout(new BorderLayout());
        setBackground(MAIN_BG);
        setBorder(new EmptyBorder(28, 32, 28, 32));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);

        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        NhanVienDAO dao = new NhanVienDAO();
        nhanViens = dao.getAllNhanVien(); 
        filterTable(); 
    }

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

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(MAIN_BG);

        searchField = new JTextField(20) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(7, 12, 7, 12)));
        searchField.setOpaque(false);
        searchField.putClientProperty("JTextField.placeholderText", "🔍 Mã hoặc Tên NV...");
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filterTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });

        JButton btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setBackground(BRAND_GOLD);
        btnTimKiem.setForeground(Color.BLACK);
        btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.addActionListener(e -> filterTable());

        JPanel searchBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchBoxPanel.setBackground(MAIN_BG);
        searchBoxPanel.add(searchField);
        searchBoxPanel.add(btnTimKiem);

        // ĐỔI MÀU NÚT THÊM THÀNH BRAND_GOLD
        JButton addBtn = new JButton("+ Thêm nhân viên") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? BRAND_GOLD.darker() : BRAND_GOLD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBtn.setForeground(Color.WHITE);
        addBtn.setContentAreaFilled(false);
        addBtn.setBorderPainted(false);
        addBtn.setFocusPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.setBorder(new EmptyBorder(9, 20, 9, 20));
        addBtn.addActionListener(e -> openNhanVienDialog(null));

        rightPanel.add(searchBoxPanel);
        rightPanel.add(addBtn);
        header.add(titleBox, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        return header;
    }

    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == COL_ACTION; }
        };

        table = new JTable(tableModel) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row))
                    c.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
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
        table.getColumnModel().getColumn(3).setCellRenderer(centerR);
        table.getColumnModel().getColumn(4).setCellRenderer(centerR);
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
                nv.getTenNV().toLowerCase().contains(kw)) {
                filtered.add(nv);
            }
        }
        refreshTableModel(filtered);
    }

    private void refreshTableModel(List<NhanVien> data) {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        for (NhanVien nv : data) {
            String trangThaiStr = (nv.getTrangThai() == 1) ? "Đang làm việc" : "Đã nghỉ";
            tableModel.addRow(new Object[]{
                nv.getMaNV(), nv.getTenNV(), nv.getSdt(), nv.getChucVu(), trangThaiStr, "" 
            });
        }
    }

    // =======================================================
    // TẠO TEXTFIELD VÀ COMBOBOX ĐÚNG CHUẨN DESIGN
    // =======================================================
    private JTextField createStyledTextField(String text) {
        JTextField txt = new JTextField(text);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(6, 10, 6, 10)));
        return txt;
    }

    private JLabel createStyledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(60, 60, 60));
        return lbl;
    }

    // =======================================================
    // FORM DIALOG THÊM/SỬA (DÙNG GRIDBAGLAYOUT)
    // =======================================================
    private void openNhanVienDialog(NhanVien existing) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner instanceof Frame ? (Frame) owner : null,
                existing == null ? "Thêm nhân viên mới" : "Sửa thông tin nhân viên", true);
        dialog.setSize(750, 450); // Tăng chiều ngang để 2 cột thoải mái hơn
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(MAIN_BG);

        // Tiêu đề dialog
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleBar.setBackground(HEADER_BG);
        titleBar.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel dlgTitle = new JLabel(existing == null ? "THÊM NHÂN VIÊN MỚI" : "SỬA THÔNG TIN NHÂN VIÊN");
        dlgTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dlgTitle.setForeground(BRAND_GOLD);
        titleBar.add(dlgTitle);

        // Form fields (SỬ DỤNG GRIDBAGLAYOUT TRÁNH BỊ KÉO DÃN)
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(MAIN_BG);
        form.setBorder(new EmptyBorder(20, 30, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Khởi tạo các Component ---
        JTextField txtMaNV = createStyledTextField(existing != null ? existing.getMaNV() : "");
        if (existing != null) txtMaNV.setEditable(false); 
        
        JTextField txtTenNV = createStyledTextField(existing != null ? existing.getTenNV() : "");
        
        JDateChooser dcNgaySinh = new JDateChooser();
        dcNgaySinh.setDateFormatString("yyyy-MM-dd");
        dcNgaySinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if(existing != null && existing.getNgaySinh() != null) dcNgaySinh.setDate(existing.getNgaySinh());
        
        JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cbGioiTinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbGioiTinh.setBackground(Color.WHITE);
        if(existing != null && existing.getGioiTinh() != null) cbGioiTinh.setSelectedItem(existing.getGioiTinh());
        
        JTextField txtSDT = createStyledTextField(existing != null ? existing.getSdt() : "");
        JTextField txtDiaChi = createStyledTextField(existing != null ? existing.getDiaChi() : "");
        
        JComboBox<String> cbChucVu = new JComboBox<>(new String[]{"Nhân viên bán hàng", "Quản lý", "Nhân viên kho"});
        cbChucVu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbChucVu.setBackground(Color.WHITE);
        if(existing != null && existing.getChucVu() != null) cbChucVu.setSelectedItem(existing.getChucVu());
        
        JDateChooser dcNgayVaoLam = new JDateChooser();
        dcNgayVaoLam.setDateFormatString("yyyy-MM-dd");
        dcNgayVaoLam.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if(existing != null && existing.getNgayVaoLam() != null) dcNgayVaoLam.setDate(existing.getNgayVaoLam());
        else dcNgayVaoLam.setDate(new java.util.Date()); 
        
        JComboBox<String> cbTrangThai = new JComboBox<>(new String[]{"Đang làm việc", "Đã nghỉ"});
        cbTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbTrangThai.setBackground(Color.WHITE);
        if(existing != null) cbTrangThai.setSelectedIndex(existing.getTrangThai() == 1 ? 0 : 1);
        
        JTextField txtTenDangNhap = createStyledTextField(existing != null ? existing.getTenDangNhap() : "");

        // --- Bố trí vào GridBagLayout (2 Cột) ---
        // Dòng 1
        gbc.gridy = 0;
        gbc.gridx = 0; gbc.weightx = 0.15; form.add(createStyledLabel("Mã NV:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.35; form.add(txtMaNV, gbc);
        gbc.gridx = 2; gbc.weightx = 0.15; form.add(createStyledLabel("Tên NV:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.35; form.add(txtTenNV, gbc);

        // Dòng 2
        gbc.gridy = 1;
        gbc.gridx = 0; form.add(createStyledLabel("Ngày Sinh:"), gbc);
        gbc.gridx = 1; form.add(dcNgaySinh, gbc);
        gbc.gridx = 2; form.add(createStyledLabel("Giới Tính:"), gbc);
        gbc.gridx = 3; form.add(cbGioiTinh, gbc);

        // Dòng 3
        gbc.gridy = 2;
        gbc.gridx = 0; form.add(createStyledLabel("SĐT:"), gbc);
        gbc.gridx = 1; form.add(txtSDT, gbc);
        gbc.gridx = 2; form.add(createStyledLabel("Địa Chỉ:"), gbc);
        gbc.gridx = 3; form.add(txtDiaChi, gbc);

        // Dòng 4
        gbc.gridy = 3;
        gbc.gridx = 0; form.add(createStyledLabel("Chức vụ:"), gbc);
        gbc.gridx = 1; form.add(cbChucVu, gbc);
        gbc.gridx = 2; form.add(createStyledLabel("Ngày Vào:"), gbc);
        gbc.gridx = 3; form.add(dcNgayVaoLam, gbc);

        // Dòng 5
        gbc.gridy = 4;
        gbc.gridx = 0; form.add(createStyledLabel("Trạng thái:"), gbc);
        gbc.gridx = 1; form.add(cbTrangThai, gbc);
        gbc.gridx = 2; form.add(createStyledLabel("User Login:"), gbc);
        gbc.gridx = 3; form.add(txtTenDangNhap, gbc);

        // Nút lưu / hủy
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(MAIN_BG);
        btnRow.setBorder(new EmptyBorder(0, 30, 20, 30));

        JButton cancelBtn = new JButton("Hủy");
        cancelBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancelBtn.setForeground(new Color(80, 80, 80));
        cancelBtn.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.setBorder(new EmptyBorder(8, 20, 8, 20));
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton saveBtn = new JButton("Lưu") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BRAND_GOLD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setContentAreaFilled(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.setBorder(new EmptyBorder(8, 28, 8, 28));

        saveBtn.addActionListener(e -> {
            try {
                if(txtMaNV.getText().trim().isEmpty() || txtTenNV.getText().trim().isEmpty() || dcNgaySinh.getDate() == null || dcNgayVaoLam.getDate() == null) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ Mã, Tên và chọn Ngày tháng!");
                    return;
                }

                NhanVien nv = new NhanVien();
                nv.setMaNV(txtMaNV.getText().trim());
                nv.setTenNV(txtTenNV.getText().trim());
                nv.setNgaySinh(dcNgaySinh.getDate()); 
                nv.setGioiTinh(cbGioiTinh.getSelectedItem().toString());
                nv.setSdt(txtSDT.getText().trim());
                nv.setDiaChi(txtDiaChi.getText().trim());
                nv.setChucVu(cbChucVu.getSelectedItem().toString());
                nv.setNgayVaoLam(dcNgayVaoLam.getDate()); 
                nv.setTrangThai(cbTrangThai.getSelectedIndex() == 0 ? 1 : 0);
                nv.setTenDangNhap(txtTenDangNhap.getText().trim());

                NhanVienDAO dao = new NhanVienDAO();
                boolean success = (existing == null) ? dao.insertNhanVien(nv) : dao.updateNhanVien(nv);

                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Lưu dữ liệu thành công!");
                    dialog.dispose();
                    loadDataFromDatabase(); 
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lưu thất bại. Kiểm tra lại thông tin (Mã NV có thể đã tồn tại)!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Có lỗi xảy ra, vui lòng kiểm tra lại thông tin nhập!");
                ex.printStackTrace();
            }
        });

        btnRow.add(cancelBtn);
        btnRow.add(saveBtn);

        dialog.add(titleBar, BorderLayout.NORTH);
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnRow, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteNhanVien(String maNV, String tenNV) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa nhân viên \"" + tenNV + "\" (" + maNV + ") không?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            NhanVienDAO dao = new NhanVienDAO();
            if(dao.deleteNhanVien(maNV)) {
                JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                loadDataFromDatabase(); 
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi: Không thể xóa nhân viên này!");
            }
        }
    }

    class ActionCellRenderer implements TableCellRenderer {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 15)); 
        private final JButton editBtn  = makeBtn(" Sửa",  EDIT_BLUE);
        private final JButton delBtn   = makeBtn(" Xóa", DELETE_RED);

        ActionCellRenderer() {
            panel.setOpaque(true);
            panel.add(editBtn);
            panel.add(delBtn);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean focus, int row, int col) {
            panel.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
            return panel;
        }

        private JButton makeBtn(String text, Color bg) {
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
            b.setBorder(new EmptyBorder(5, 12, 5, 12));
            return b;
        }
    }

    class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 15)); 
        private int currentRow;

        ActionCellEditor() {
            panel.setBackground(ROW_WHITE);

            JButton editBtn = makeBtn(" Sửa", EDIT_BLUE);
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

            JButton delBtn = makeBtn(" Xóa", DELETE_RED);
            delBtn.addActionListener(e -> {
                fireEditingStopped();
                String maNV = (String) tableModel.getValueAt(currentRow, 0);
                String tenNV = (String) tableModel.getValueAt(currentRow, 1);
                deleteNhanVien(maNV, tenNV);
            });

            panel.add(editBtn);
            panel.add(delBtn);
        }

        @Override
        public Component getTableCellEditorComponent(JTable t, Object v, boolean sel, int row, int col) {
            currentRow = row;
            panel.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
            return panel;
        }

        @Override public Object getCellEditorValue() { return ""; }

        private JButton makeBtn(String text, Color bg) {
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
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setBorder(new EmptyBorder(5, 12, 5, 12));
            return b;
        }
    }
}