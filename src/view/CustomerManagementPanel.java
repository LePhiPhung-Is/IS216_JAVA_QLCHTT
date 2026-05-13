package src.view;

import src.model.KhachHang;
import src.dao.KhachHangDAO; 

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerManagementPanel extends JPanel {

    // ===== MÀU GIAO DIỆN =====
    public final Color MAIN_BG      = new Color(244, 247, 246);
    public final Color BRAND_GOLD   = new Color(212, 175, 55);
    public final Color SIDEBAR_BG   = new Color(5, 5, 5);
    public final Color ROW_WHITE    = Color.WHITE;
    public final Color ROW_STRIPE   = new Color(250, 252, 251);
    public final Color BORDER_COLOR = new Color(220, 225, 222);
    public final Color DELETE_RED   = new Color(220, 60, 60);
    public final Color EDIT_BLUE    = new Color(50, 120, 200);
    public final Color HEADER_BG    = new Color(30, 30, 30);

    // ===== DỮ LIỆU =====
    public List<KhachHang> customers = new ArrayList<>();
    public DefaultTableModel tableModel;
    public JTable table;
    public JTextField searchField;
    public KhachHangDAO dao = new KhachHangDAO();
    
    // Yêu cầu đặc biệt
    public ArrayList<Integer> baibao = new ArrayList<>();

    // ===== CỘT BẢNG =====
    public static final String[] COLUMNS  = {
        "Mã KH", "Tên khách hàng", "Số điện thoại", "Địa chỉ", "Thao tác"
    };
    public static final int COL_ACTION = 4;

    public CustomerManagementPanel() {
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
    public void loadDataFromDatabase() {
        customers = dao.getAllKhachHang();
        if (customers == null) {
            customers = new ArrayList<>();
        }
        filterTable();
    }

    public String generateNewCustomerID() {
        if (customers == null || customers.isEmpty()) {
            return "KH001";
        }
        int maxId = 0;
        for (KhachHang kh : customers) {
            try {
                int currentId = Integer.parseInt(kh.getMaKH().substring(2));
                if (currentId > maxId) {
                    maxId = currentId;
                }
            } catch (NumberFormatException e) {
                // Bỏ qua mã lỗi không hợp lệ
            }
        }
        return String.format("KH%03d", maxId + 1);
    }

    // =====================================================================
    //  HEADER
    // =====================================================================
    public JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAIN_BG);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("QUẢN LÝ KHÁCH HÀNG");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(SIDEBAR_BG);

        JLabel subtitle = new JLabel("Danh sách toàn bộ khách hàng tại cửa hàng");
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

    public JTextField buildSearchField() {
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

    public JButton buildAddButton() {
        JButton btn = new JButton("+ Thêm khách hàng") {
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
        btn.addActionListener(e -> openCustomerDialog(null));
        return btn;
    }

    // =====================================================================
    //  BẢNG KHÁCH HÀNG
    // =====================================================================
    public JScrollPane buildTablePanel() {
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
        table.setRowHeight(70); // Match ProductManagementPanel
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
        table.getColumnModel().getColumn(1).setPreferredWidth(250); 
        table.getColumnModel().getColumn(2).setCellRenderer(centerR);
        table.getColumnModel().getColumn(2).setPreferredWidth(150); 
        table.getColumnModel().getColumn(3).setPreferredWidth(350); 

        table.getColumnModel().getColumn(4).setPreferredWidth(140); 
        table.getColumnModel().getColumn(COL_ACTION).setCellRenderer(new ActionCellRenderer());
        table.getColumnModel().getColumn(COL_ACTION).setCellEditor(new ActionCellEditor());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scroll.getViewport().setBackground(ROW_WHITE);
        return scroll;
    }

    public void filterTable() {
        String kw = searchField.getText().trim().toLowerCase();
        List<KhachHang> filtered = new ArrayList<>();
        for (KhachHang kh : customers) {
            if (kh.getMaKH().toLowerCase().contains(kw) ||
                kh.getTenKH().toLowerCase().contains(kw) ||
                kh.getSoDienThoai().toLowerCase().contains(kw)) { 
                filtered.add(kh);
            }
        }
        refreshTableModel(filtered);
    }

    public void refreshTableModel(List<KhachHang> data) {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        for (KhachHang kh : data) {
            tableModel.addRow(new Object[]{
                kh.getMaKH(), kh.getTenKH(), kh.getSoDienThoai(), kh.getDiaChi(), ""
            });
        }
    }

    // =====================================================================
    //  DIALOG — THÊM / SỬA KHÁCH HÀNG
    // =====================================================================
    public void openCustomerDialog(KhachHang existing) {
        boolean isEdit = (existing != null);

        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner instanceof Frame ? (Frame) owner : null,
                isEdit ? "Sửa thông tin khách hàng" : "Thêm khách hàng mới", true);
        dialog.setSize(450, 360);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(MAIN_BG);

        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleBar.setBackground(HEADER_BG);
        titleBar.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel lbTitle = new JLabel(isEdit ? "SỬA THÔNG TIN KHÁCH HÀNG" : "THÊM KHÁCH HÀNG MỚI");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbTitle.setForeground(BRAND_GOLD);
        titleBar.add(lbTitle);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(MAIN_BG);
        form.setBorder(new EmptyBorder(20, 28, 10, 28));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 4, 8, 4);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        String maKHHienTai = isEdit ? existing.getMaKH() : generateNewCustomerID();
        JTextField fieldMa     = makeField(maKHHienTai);
        JTextField fieldTen    = makeField(isEdit ? existing.getTenKH() : "");
        JTextField fieldSDT    = makeField(isEdit ? existing.getSoDienThoai() : "");
        JTextField fieldDiaChi = makeField(isEdit ? existing.getDiaChi(): "");

        fieldMa.setEditable(false); 

        addRow(form, gbc, 0, "Mã khách hàng:", fieldMa);
        addRow(form, gbc, 1, "Tên khách hàng:", fieldTen);
        addRow(form, gbc, 2, "Số điện thoại:", fieldSDT);
        addRow(form, gbc, 3, "Địa chỉ:", fieldDiaChi);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(MAIN_BG);
        btnRow.setBorder(new EmptyBorder(0, 28, 20, 28));

        JButton cancelBtn = makePlainButton("Hủy");
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton saveBtn = makeGoldButton(isEdit ? "Lưu thay đổi" : "Thêm mới");
        saveBtn.addActionListener(e -> {
            if (fieldTen.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập tên khách hàng.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (isEdit) {
                existing.setTenKH(fieldTen.getText().trim());
                existing.setSoDienThoai(fieldSDT.getText().trim());
                existing.setDiaChi(fieldDiaChi.getText().trim());
                
                if (dao.suaKhachHang(existing)) { 
                    JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi cập nhật CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                KhachHang kh = new KhachHang(
                    fieldMa.getText().trim(),
                    fieldTen.getText().trim(),
                    fieldSDT.getText().trim(),
                    fieldDiaChi.getText().trim()
                );
                
                if (dao.themKhachHang(kh)) { 
                    JOptionPane.showMessageDialog(dialog, "Thêm mới thành công!");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi thêm mới CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            loadDataFromDatabase(); 
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
    //  XÓA KHÁCH HÀNG 
    // =====================================================================
    public void deleteCustomer(int viewRow) {
        String name = (String) tableModel.getValueAt(viewRow, 1);
        String code = (String) tableModel.getValueAt(viewRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa khách hàng:\n" + code + " - " + name + "?",
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);
                
        if (confirm == JOptionPane.YES_OPTION) {
             if (dao.xoaKhachHang(code)) {
                JOptionPane.showMessageDialog(this, "Đã xóa khách hàng thành công!");
                loadDataFromDatabase();
             } else {
                JOptionPane.showMessageDialog(this, "Lỗi xóa dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
             }
        }
    }

    // =====================================================================
    //  RENDERER & EDITOR
    // =====================================================================
    class ActionCellRenderer implements TableCellRenderer {
        public final JPanel  panel   = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 15));
        public final JButton editBtn = makeBtn("Sửa", EDIT_BLUE);
        public final JButton delBtn  = makeBtn("Xóa", DELETE_RED);

        ActionCellRenderer() {
            panel.setOpaque(true);
            panel.add(editBtn);
            panel.add(delBtn);
        }

        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean focus, int row, int col) {
            panel.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
            return panel;
        }

        public JButton makeBtn(String text, Color bg) {
            JButton b = new JButton(text);
            b.setBackground(bg);
            b.setForeground(Color.WHITE);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            return b;
        }
    }

    class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
        public final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 15));
        public int currentRow;

        ActionCellEditor() {
            JButton editBtn = new JButton("✏");
            editBtn.setBackground(EDIT_BLUE);
            editBtn.setForeground(Color.WHITE);
            editBtn.setBorderPainted(false);
            editBtn.setFocusPainted(false);
            editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            editBtn.addActionListener(e -> {
                fireEditingStopped(); 
                String code = (String) tableModel.getValueAt(currentRow, 0);
                for (KhachHang kh : customers) {
                    if (kh.getMaKH().equals(code)) {
                        openCustomerDialog(kh);
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
                deleteCustomer(currentRow);
            });

            panel.add(editBtn);
            panel.add(delBtn);
        }

        @Override public Component getTableCellEditorComponent(JTable t, Object v, boolean sel, int row, int col) {
            currentRow = row;
            panel.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
            return panel;
        }

        @Override public Object getCellEditorValue() { return ""; }
    }

    // =====================================================================
    //  HELPER — FORM & STYLE
    // =====================================================================
    public JTextField makeField(String value) {
        JTextField f = new JTextField(value, 22);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(6, 10, 6, 10)));
        return f;
    }

    public void addRow(JPanel form, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(60, 60, 60));
        form.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        form.add(field, gbc);
    }

    public JButton makeGoldButton(String text) {
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

    public JButton makePlainButton(String text) {
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