package src.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ProductManagementPanel extends JPanel {

    // ===== MÀU GIAO DIỆN (khớp NV_BanHang_UI) =====
    private final Color MAIN_BG      = new Color(244, 247, 246);
    private final Color BRAND_GOLD   = new Color(212, 175, 55);
    private final Color SIDEBAR_BG   = new Color(5, 5, 5);
    private final Color ROW_WHITE    = Color.WHITE;
    private final Color ROW_STRIPE   = new Color(250, 252, 251);
    private final Color BORDER_COLOR = new Color(220, 225, 222);
    private final Color DELETE_RED   = new Color(220, 60, 60);
    private final Color EDIT_BLUE    = new Color(50, 120, 200);
    private final Color HEADER_BG    = new Color(30, 30, 30);

    // ===== DỮ LIỆU MẪU =====
    private final List<String[]> products = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;

    // ===== CỘT BẢNG =====
    private static final String[] COLUMNS = {"Mã SP", "Tên sản phẩm", "Danh mục", "Giá (VNĐ)", "Tồn kho", "Thao tác"};
    private static final int COL_ACTION = 5;

    public ProductManagementPanel() {
        // Dữ liệu mẫu
        products.add(new String[]{"SP001", "Son môi đỏ Ruby", "Son môi",   "185.000",  "42"});
        products.add(new String[]{"SP002", "Kem nền CC Cream", "Kem nền",  "320.000",  "18"});
        products.add(new String[]{"SP003", "Phấn phủ kiềm dầu","Phấn phủ","210.000",  "35"});
        products.add(new String[]{"SP004", "Mascara dày mi",   "Mắt",      "155.000",  "60"});
        products.add(new String[]{"SP005", "Tẩy trang dịu nhẹ","Chăm sóc","98.000",   "27"});
        products.add(new String[]{"SP006", "Kem dưỡng ẩm ban đêm","Chăm sóc","450.000","12"});
        products.add(new String[]{"SP007", "Kẻ mắt nước đen",  "Mắt",      "125.000",  "55"});
        products.add(new String[]{"SP008", "Xịt khoáng dưỡng", "Chăm sóc","199.000",  "33"});

        setLayout(new BorderLayout());
        setBackground(MAIN_BG);
        setBorder(new EmptyBorder(28, 32, 28, 32));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
    }

    // ===== HEADER: tiêu đề + tìm kiếm + nút thêm =====
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAIN_BG);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Tiêu đề
        JLabel title = new JLabel("QUẢN LÝ SẢN PHẨM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(SIDEBAR_BG);

        JLabel subtitle = new JLabel("Danh sách toàn bộ sản phẩm trong hệ thống");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(130, 130, 130));

        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setBackground(MAIN_BG);
        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(subtitle);

        // Bên phải: ô tìm kiếm + nút thêm
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
        searchField.putClientProperty("JTextField.placeholderText", "🔍  Tìm kiếm sản phẩm...");
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filterTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });

        JButton addBtn = new JButton("+ Thêm sản phẩm") {
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
        addBtn.addActionListener(e -> openProductDialog(null, -1));

        rightPanel.add(searchField);
        rightPanel.add(addBtn);

        header.add(titleBox, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        return header;
    }

    // ===== BẢNG SẢN PHẨM =====
    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        refreshTableModel(products);

        table = new JTable(tableModel) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row))
                    c.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
                return c;
            }
        };

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(48);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(new Color(212, 175, 55, 50));
        table.setSelectionForeground(SIDEBAR_BG);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFocusable(false);

        // Header bảng
        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(HEADER_BG);
        th.setForeground(BRAND_GOLD);
        th.setPreferredSize(new Dimension(0, 44));
        th.setBorder(BorderFactory.createEmptyBorder());
        th.setReorderingAllowed(false);

        // Căn chỉnh cột
        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
        centerR.setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer rightR = new DefaultTableCellRenderer();
        rightR.setHorizontalAlignment(SwingConstants.RIGHT);
        rightR.setBorder(new EmptyBorder(0, 0, 0, 20));

        table.getColumnModel().getColumn(0).setCellRenderer(centerR);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(240);
        table.getColumnModel().getColumn(2).setCellRenderer(centerR);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setCellRenderer(rightR);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setCellRenderer(centerR);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(COL_ACTION).setPreferredWidth(160);

        // Renderer & Editor cho cột "Thao tác"
        table.getColumnModel().getColumn(COL_ACTION).setCellRenderer(new ActionCellRenderer());
        table.getColumnModel().getColumn(COL_ACTION).setCellEditor(new ActionCellEditor());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scroll.getViewport().setBackground(ROW_WHITE);
        return scroll;
    }

    // ===== LỌC BẢNG THEO TỪ KHÓA =====
    private void filterTable() {
        String kw = searchField.getText().trim().toLowerCase();
        List<String[]> filtered = new ArrayList<>();
        for (String[] p : products) {
            if (p[0].toLowerCase().contains(kw) ||
                p[1].toLowerCase().contains(kw) ||
                p[2].toLowerCase().contains(kw)) {
                filtered.add(p);
            }
        }
        refreshTableModel(filtered);
    }

    private void refreshTableModel(List<String[]> data) {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        for (String[] row : data)
            tableModel.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], ""});
    }

    // ===== DIALOG THÊM / SỬA =====
    private void openProductDialog(String[] existing, int productIndex) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner instanceof Frame ? (Frame) owner : null,
                existing == null ? "Thêm sản phẩm mới" : "Sửa sản phẩm", true);
        dialog.setSize(460, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(MAIN_BG);

        // Tiêu đề dialog
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleBar.setBackground(HEADER_BG);
        titleBar.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel dlgTitle = new JLabel(existing == null ? "THÊM SẢN PHẨM MỚI" : "SỬA SẢN PHẨM");
        dlgTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dlgTitle.setForeground(BRAND_GOLD);
        titleBar.add(dlgTitle);

        // Form fields
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(MAIN_BG);
        form.setBorder(new EmptyBorder(20, 30, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 4, 8, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Mã sản phẩm:", "Tên sản phẩm:", "Danh mục:", "Giá (VNĐ):", "Tồn kho:"};
        JTextField[] fields = new JTextField[5];
        String[] defaults = existing != null ? existing : new String[]{"", "", "", "", ""};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.3;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lbl.setForeground(new Color(60, 60, 60));
            form.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 0.7;
            fields[i] = new JTextField(defaults[i], 18);
            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            fields[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR),
                    new EmptyBorder(6, 10, 6, 10)));
            form.add(fields[i], gbc);
        }

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
            String[] vals = new String[5];
            for (int i = 0; i < 5; i++) vals[i] = fields[i].getText().trim();
            if (vals[0].isEmpty() || vals[1].isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập Mã và Tên sản phẩm.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (productIndex < 0) {
                products.add(vals);
            } else {
                products.set(productIndex, vals);
            }
            filterTable();
            dialog.dispose();
        });

        btnRow.add(cancelBtn);
        btnRow.add(saveBtn);

        dialog.add(titleBar, BorderLayout.NORTH);
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnRow, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ===== XÓA SẢN PHẨM =====
    private void deleteProduct(int viewRow) {
        String name = (String) tableModel.getValueAt(viewRow, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa sản phẩm \"" + name + "\" không?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        String code = (String) tableModel.getValueAt(viewRow, 0);
        products.removeIf(p -> p[0].equals(code));
        filterTable();
    }

    // =========================================================
    // RENDERER: hiển thị nút Sửa / Xóa trong cột Thao tác
    // =========================================================
    class ActionCellRenderer implements TableCellRenderer {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        private final JButton editBtn  = makeBtn("✏ Sửa",  EDIT_BLUE);
        private final JButton delBtn   = makeBtn("🗑 Xóa", DELETE_RED);

        ActionCellRenderer() {
            panel.setOpaque(true);
            panel.add(editBtn);
            panel.add(delBtn);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v,
                boolean sel, boolean focus, int row, int col) {
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
            b.setFocusPainted(false);
            b.setBorder(new EmptyBorder(5, 12, 5, 12));
            return b;
        }
    }

    // =========================================================
    // EDITOR: xử lý click nút Sửa / Xóa
    // =========================================================
    class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        private int currentRow;

        ActionCellEditor() {
            panel.setBackground(ROW_WHITE);

            JButton editBtn = makeBtn("✏ Sửa", EDIT_BLUE);
            editBtn.addActionListener(e -> {
                fireEditingStopped();
                // Tìm index thực trong danh sách products theo mã
                String code = (String) tableModel.getValueAt(currentRow, 0);
                for (int i = 0; i < products.size(); i++) {
                    if (products.get(i)[0].equals(code)) {
                        openProductDialog(products.get(i), i);
                        break;
                    }
                }
            });

            JButton delBtn = makeBtn("🗑 Xóa", DELETE_RED);
            delBtn.addActionListener(e -> {
                fireEditingStopped();
                deleteProduct(currentRow);
            });

            panel.add(editBtn);
            panel.add(delBtn);
        }

        @Override
        public Component getTableCellEditorComponent(JTable t, Object v,
                boolean sel, int row, int col) {
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
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setBorder(new EmptyBorder(5, 12, 5, 12));
            return b;
        }
    }
}
