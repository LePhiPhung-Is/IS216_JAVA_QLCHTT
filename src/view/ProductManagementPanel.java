package src.view;

import src.dao.SanPhamDAO;
import src.model.SanPham;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductManagementPanel extends JPanel {

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
    private List<SanPham> products = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private final DecimalFormat df = new DecimalFormat("#,###đ");

    // ===== CỘT BẢNG =====
    private static final String[] COLUMNS  = {"Ảnh", "Mã SP", "Tên sản phẩm", "Danh mục", "Giá bán", "Tồn kho", "Thao tác"};
    private static final int      COL_ACTION = 6;

    // =====================================================================
    public ProductManagementPanel() {
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
        products = new SanPhamDAO().getAllSanPham();
        filterTable();
    }

    // =====================================================================
    //  HEADER
    // =====================================================================

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAIN_BG);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("QUẢN LÝ SẢN PHẨM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(SIDEBAR_BG);

        JLabel subtitle = new JLabel("Danh sách toàn bộ sản phẩm tại cửa hàng");
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
        rightPanel.add(buildSearchField());
        rightPanel.add(buildAddButton());

        header.add(titleBox,   BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        return header;
    }

    private JTextField buildSearchField() {
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
        searchField.putClientProperty("JTextField.placeholderText", "🔍  Tìm kiếm...");
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filterTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });
        return searchField;
    }

    private JButton buildAddButton() {
        JButton btn = new JButton("+ Thêm sản phẩm") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? BRAND_GOLD.darker() : BRAND_GOLD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(9, 20, 9, 20));
        btn.addActionListener(e -> openProductDialog(null, -1));
        return btn;
    }

    // =====================================================================
    //  BẢNG SẢN PHẨM
    // =====================================================================

    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == COL_ACTION; }
            @Override public Class<?> getColumnClass(int c) {
                return c == 0 ? ImageIcon.class : super.getColumnClass(c);
            }
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
        table.setRowHeight(70);
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
        DefaultTableCellRenderer rightR = new DefaultTableCellRenderer();
        rightR.setHorizontalAlignment(SwingConstants.RIGHT);
        rightR.setBorder(new EmptyBorder(0, 0, 0, 20));

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setCellRenderer(centerR);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setCellRenderer(centerR);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setCellRenderer(rightR);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setCellRenderer(centerR);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);
        table.getColumnModel().getColumn(COL_ACTION).setPreferredWidth(140);

        table.getColumnModel().getColumn(COL_ACTION).setCellRenderer(new ActionCellRenderer());
        table.getColumnModel().getColumn(COL_ACTION).setCellEditor(new ActionCellEditor());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scroll.getViewport().setBackground(ROW_WHITE);
        return scroll;
    }

    private void filterTable() {
        String kw = searchField.getText().trim().toLowerCase();
        List<SanPham> filtered = new ArrayList<>();
        for (SanPham p : products) {
            if (p.getMaSP().toLowerCase().contains(kw) ||
                p.getTenSP().toLowerCase().contains(kw))
                filtered.add(p);
        }
        refreshTableModel(filtered);
    }

    private void refreshTableModel(List<SanPham> data) {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        for (SanPham sp : data) {
            String tenAnh = sp.getHinhAnh() != null && !sp.getHinhAnh().trim().isEmpty()
                    ? sp.getHinhAnh().trim() : "no_image.png";
            tableModel.addRow(new Object[]{
                loadImage("src/product_images/" + tenAnh, 60, 60),
                sp.getMaSP(),
                sp.getTenSP(),
                sp.getMaDM(),
                df.format(sp.getGiaBan()),
                sp.getSoLuongTon(),
                ""
            });
        }
    }

    private ImageIcon loadImage(String path, int w, int h) {
        try {
            File f = new File(path);
            if (f.exists()) {
                Image img = new ImageIcon(f.getAbsolutePath()).getImage()
                        .getScaledInstance(w, h, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ImageIcon();
    }

    // =====================================================================
    //  DIALOG — THÊM / SỬA SẢN PHẨM
    //  existing = null → thêm mới | existing != null → sửa (form có sẵn dữ liệu)
    // =====================================================================

    private void openProductDialog(SanPham existing, int productIndex) {
        boolean isEdit = (existing != null);

        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner instanceof Frame ? (Frame) owner : null,
                isEdit ? "Sửa sản phẩm" : "Thêm sản phẩm mới", true);
        dialog.setSize(500, 480);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(MAIN_BG);

        // ----- Title bar -----
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleBar.setBackground(HEADER_BG);
        titleBar.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel lbTitle = new JLabel(isEdit ? "SỬA SẢN PHẨM" : "THÊM SẢN PHẨM MỚI");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbTitle.setForeground(BRAND_GOLD);
        titleBar.add(lbTitle);

        // ----- Form -----
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(MAIN_BG);
        form.setBorder(new EmptyBorder(20, 28, 10, 28));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 4, 8, 4);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // Các field — điền sẵn nếu đang sửa
        JTextField fieldMa     = makeField(isEdit ? existing.getMaSP()               : "");
        JTextField fieldTen    = makeField(isEdit ? existing.getTenSP()              : "");
        JTextField fieldDM     = makeField(isEdit ? existing.getMaDM()               : "");
        JTextField fieldGia    = makeField(isEdit ? String.valueOf((int) existing.getGiaBan()) : "");
        JTextField fieldTon    = makeField(isEdit ? String.valueOf(existing.getSoLuongTon())   : "");
        JTextField fieldMauSac = makeField(isEdit ? existing.getMauSac()             : "");
        JTextField fieldKichCo = makeField(isEdit ? existing.getKichCo()             : "");
        JTextField fieldAnh    = makeField(isEdit ? existing.getHinhAnh()            : "");

        fieldMa.setEditable(!isEdit); // Không cho sửa mã khi đang edit

        addRow(form, gbc, 0, "Mã sản phẩm:",  fieldMa);
        addRow(form, gbc, 1, "Tên sản phẩm:", fieldTen);
        addRow(form, gbc, 2, "Mã danh mục:",  fieldDM);
        addRow(form, gbc, 3, "Giá bán (đ):",  fieldGia);
        addRow(form, gbc, 4, "Tồn kho:",       fieldTon);
        addRow(form, gbc, 5, "Màu sắc:",       fieldMauSac);
        addRow(form, gbc, 6, "Kích cỡ:",       fieldKichCo);
        addRow(form, gbc, 7, "Tên file ảnh:",  fieldAnh);

        // ----- Nút Lưu / Hủy -----
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(MAIN_BG);
        btnRow.setBorder(new EmptyBorder(0, 28, 20, 28));

        JButton cancelBtn = makePlainButton("Hủy");
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton saveBtn = makeGoldButton(isEdit ? "Lưu thay đổi" : "Thêm mới");
        saveBtn.addActionListener(e -> {
            // Validate bắt buộc
            if (fieldMa.getText().trim().isEmpty() || fieldTen.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Vui lòng điền đầy đủ Mã và Tên sản phẩm.",
                        "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double gia;
            int    ton;
            try {
                gia = Double.parseDouble(fieldGia.getText().trim());
                ton = Integer.parseInt(fieldTon.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Giá bán và Tồn kho phải là số.",
                        "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (isEdit) {
                // Cập nhật thông tin sản phẩm đang sửa
                existing.setTenSP(fieldTen.getText().trim());
                existing.setMaDM(fieldDM.getText().trim());
                existing.setGiaBan(gia);
                existing.setSoLuongTon(ton);
                existing.setMauSac(fieldMauSac.getText().trim());
                existing.setKichCo(fieldKichCo.getText().trim());
                existing.setHinhAnh(fieldAnh.getText().trim());
                // TODO: gọi DAO cập nhật xuống database
            } else {
                // Tạo sản phẩm mới
                SanPham sp = new SanPham(
                        fieldMa.getText().trim(),
                        fieldDM.getText().trim(),
                        fieldMauSac.getText().trim(),
                        fieldKichCo.getText().trim(),
                        gia,
                        ton,
                        "DANG_BAN",
                        fieldTen.getText().trim(),
                        "",           // maKho — để trống hoặc thêm field nếu cần
                        fieldAnh.getText().trim()
                );
                products.add(sp);
                // TODO: gọi DAO lưu xuống database
            }

            filterTable();
            dialog.dispose();
        });

        btnRow.add(cancelBtn);
        btnRow.add(saveBtn);

        dialog.add(titleBar, BorderLayout.NORTH);
        dialog.add(new JScrollPane(form), BorderLayout.CENTER);
        dialog.add(btnRow,   BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // =====================================================================
    //  XÓA SẢN PHẨM
    // =====================================================================

    private void deleteProduct(int viewRow) {
        String name = (String) tableModel.getValueAt(viewRow, 2);
        String code = (String) tableModel.getValueAt(viewRow, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa sản phẩm \"" + name + "\" không?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            products.removeIf(p -> p.getMaSP().equals(code));
            // TODO: gọi DAO xóa xuống database
            filterTable();
        }
    }

    // =====================================================================
    //  RENDERER & EDITOR — CỘT THAO TÁC
    // =====================================================================

    class ActionCellRenderer implements TableCellRenderer {
        private final JPanel  panel   = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 15));
        private final JButton editBtn = makeBtn("✏", EDIT_BLUE);
        private final JButton delBtn  = makeBtn("🗑", DELETE_RED);

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

        private JButton makeBtn(String text, Color bg) {
            JButton b = new JButton(text);
            b.setBackground(bg);
            b.setForeground(Color.WHITE);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            return b;
        }
    }

    class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 15));
        private int currentRow;

        ActionCellEditor() {
            JButton editBtn = new JButton("✏");
            editBtn.setBackground(EDIT_BLUE);
            editBtn.setForeground(Color.WHITE);
            editBtn.setBorderPainted(false);
            editBtn.setFocusPainted(false);
            editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            editBtn.addActionListener(e -> {
                fireEditingStopped();
                String code = (String) tableModel.getValueAt(currentRow, 1);
                for (int i = 0; i < products.size(); i++) {
                    if (products.get(i).getMaSP().equals(code)) {
                        openProductDialog(products.get(i), i);
                        break;
                    }
                }
            });

            JButton delBtn = new JButton("🗑");
            delBtn.setBackground(DELETE_RED);
            delBtn.setForeground(Color.WHITE);
            delBtn.setBorderPainted(false);
            delBtn.setFocusPainted(false);
            delBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            delBtn.addActionListener(e -> {
                fireEditingStopped();
                deleteProduct(currentRow);
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
    //  HELPER — FORM & STYLE
    // =====================================================================

    private JTextField makeField(String value) {
        JTextField f = new JTextField(value, 22);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(6, 10, 6, 10)));
        return f;
    }

    private void addRow(JPanel form, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(60, 60, 60));
        form.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        form.add(field, gbc);
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
}