package src.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.*;

/**
 * Quản lý Nhà Cung Cấp - BEAUTY SHOP
 * Sinh viên: ĐOÀN XUÂN CHIẾN - MSSV: 24520217
 */
public class QuanLyNhaCungCap extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private JButton btnAdd;

    private final Color BRAND_GOLD = new Color(212, 175, 55);
    private final Color ACTION_BLUE = new Color(51, 122, 183);
    private final Color ACTION_RED = new Color(217, 83, 79);

    private final String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private final String DB_USER = "FASHION_ADMIN";
    private final String DB_PASS = "123456";

    public QuanLyNhaCungCap() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        initHeader();
        initTable();
        loadData();
    }

    private void initHeader() {
        // hgap = 20 giúp đẩy phần WEST và EAST ra xa nhau
        JPanel pnlHeader = new JPanel(new BorderLayout(20, 0));
        pnlHeader.setOpaque(false);

        // Tiêu đề
        JPanel pnlTitle = new JPanel(new GridLayout(2, 1));
        pnlTitle.setOpaque(false);
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÀ CUNG CẤP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.BLACK);
        
        JLabel lblSubTitle = new JLabel("Danh sách toàn bộ nhà cung cấp tại cửa hàng");
        lblSubTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubTitle.setForeground(Color.GRAY);
        pnlTitle.add(lblTitle);
        pnlTitle.add(lblSubTitle);

        // Actions: Dịch sang phải và thu nhỏ ô tìm kiếm
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlActions.setOpaque(false);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(180, 35)); // Thu nhỏ ô tìm kiếm
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        btnAdd = new JButton("+ Thêm nhà cung cấp");
        btnAdd.setBackground(BRAND_GOLD);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setPreferredSize(new Dimension(180, 40)); // Thu nhỏ nút bấm một chút
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);

        pnlActions.add(lblSearch);
        pnlActions.add(txtSearch);
        pnlActions.add(btnAdd);

        pnlHeader.add(pnlTitle, BorderLayout.WEST);
        pnlHeader.add(pnlActions, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        txtSearch.addActionListener(e -> loadData());
        btnAdd.addActionListener(e -> openSupplierDialog(null));
    }

    private void initTable() {
        String[] columns = {"Mã NCC", "Tên nhà cung cấp", "Địa chỉ", "Số điện thoại", "Email", "Thao tác"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; 
            }
        };

        table = new JTable(model);
        table.setRowHeight(60);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(Color.BLACK); 
        table.setSelectionForeground(Color.BLACK);
        table.setSelectionBackground(new Color(235, 235, 235));
        table.setGridColor(new Color(230, 230, 230));
        table.setShowVerticalLines(false);

        // Tắt tự động co giãn để tránh dấu "..." và dùng thanh cuộn ngang
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setForeground(BRAND_GOLD);
        header.setPreferredSize(new Dimension(0, 45));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        // Thiết lập độ rộng cố định cho từng cột
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);  // Mã NCC
        columnModel.getColumn(1).setPreferredWidth(250);  // Tên NCC
        columnModel.getColumn(2).setPreferredWidth(300);  // Địa chỉ
        columnModel.getColumn(3).setPreferredWidth(150);  // SĐT
        columnModel.getColumn(4).setPreferredWidth(200);  // Email
        columnModel.getColumn(5).setPreferredWidth(180);  // Thao tác
        columnModel.getColumn(5).setMinWidth(180);

        table.getColumnModel().getColumn(5).setCellRenderer(new ActionButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ActionButtonEditor());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void loadData() {
        model.setRowCount(0);
        String keyword = txtSearch.getText().trim().toLowerCase();
        String sql = "SELECT MANCC, TENNCC, DIACHI, SDT, EMAIL FROM NHACUNGCAP " +
                     "WHERE LOWER(TENNCC) LIKE ? OR LOWER(MANCC) LIKE ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MANCC"), rs.getString("TENNCC"), 
                    rs.getString("DIACHI"), rs.getString("SDT"), 
                    rs.getString("EMAIL"), ""
                });
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void openSupplierDialog(Object[] data) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                data == null ? "Thêm nhà cung cấp mới" : "Cập nhật thông tin NCC", true);
        dialog.setSize(450, 550);
        dialog.setLayout(new GridBagLayout());
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 20, 10, 20);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField m = new JTextField(data != null ? (data[0] != null ? data[0].toString() : "") : "");
        if (data != null) m.setEditable(false);
        
        JTextField t = new JTextField(data != null ? (data[1] != null ? data[1].toString() : "") : "");
        JTextField d = new JTextField(data != null ? (data[2] != null ? data[2].toString() : "") : "");
        JTextField s = new JTextField(data != null ? (data[3] != null ? data[3].toString() : "") : "");
        JTextField e = new JTextField(data != null ? (data[4] != null ? data[4].toString() : "") : "");

        addField(dialog, "Mã NCC:", m, g, 0);
        addField(dialog, "Tên nhà cung cấp:", t, g, 1);
        addField(dialog, "Địa chỉ:", d, g, 2);
        addField(dialog, "Số điện thoại:", s, g, 3);
        addField(dialog, "Email:", e, g, 4);

        JButton btnSave = new JButton("LƯU THÔNG TIN");
        btnSave.setBackground(BRAND_GOLD);
        btnSave.setForeground(Color.BLACK);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setPreferredSize(new Dimension(0, 45));
        btnSave.setOpaque(true);
        btnSave.setBorderPainted(false);
        
        g.gridx = 0; g.gridy = 5; g.gridwidth = 2; g.insets = new Insets(30, 20, 20, 20);
        dialog.add(btnSave, g);

        btnSave.addActionListener(ev -> {
            if (t.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Tên NCC không được bỏ trống!");
                return;
            }
            if (executeDBUpdate(data == null ? "INSERT" : "UPDATE", m, t, d, s, e)) {
                dialog.dispose();
                loadData();
            }
        });
        dialog.setVisible(true);
    }

    private void addField(JDialog d, String lbl, JTextField tf, GridBagConstraints g, int row) {
        g.gridwidth = 1; g.gridx = 0; g.gridy = row;
        d.add(new JLabel(lbl), g);
        g.gridx = 1; tf.setPreferredSize(new Dimension(200, 35));
        d.add(tf, g);
    }

    private boolean executeDBUpdate(String type, JTextField m, JTextField t, JTextField d, JTextField s, JTextField e) {
        String sql = type.equals("INSERT") ? 
            "INSERT INTO NHACUNGCAP (MANCC, TENNCC, DIACHI, SDT, EMAIL) VALUES(?,?,?,?,?)" :
            "UPDATE NHACUNGCAP SET TENNCC=?, DIACHI=?, SDT=?, EMAIL=? WHERE MANCC=?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (type.equals("INSERT")) {
                pstmt.setString(1, m.getText()); pstmt.setString(2, t.getText());
                pstmt.setString(3, d.getText()); pstmt.setString(4, s.getText());
                pstmt.setString(5, e.getText());
            } else {
                pstmt.setString(1, t.getText()); pstmt.setString(2, d.getText());
                pstmt.setString(3, s.getText()); pstmt.setString(4, e.getText());
                pstmt.setString(5, m.getText());
            }
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi Database: " + ex.getMessage());
            return false;
        }
    }

    // Các Renderer và Editor cho nút bấm
    class ActionButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        public ActionButtonRenderer() {
            setOpaque(false);
            setLayout(new FlowLayout(FlowLayout.CENTER, 8, 15));
            add(createBtn("Sửa", ACTION_BLUE));
            add(createBtn("Xóa", ACTION_RED));
        }
        private JButton createBtn(String txt, Color bg) {
            JButton b = new JButton(txt);
            b.setFont(new Font("Segoe UI", Font.BOLD, 12));
            b.setBackground(bg); b.setForeground(Color.WHITE);
            b.setPreferredSize(new Dimension(65, 30));
            b.setOpaque(true); b.setBorderPainted(false);
            return b;
        }
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            return this;
        }
    }

    class ActionButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        public ActionButtonEditor() {
            super(new JCheckBox());
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
            panel.setOpaque(true); panel.setBackground(new Color(235, 235, 235));
            
            JButton btnEdit = new JButton("Sửa");
            btnEdit.setBackground(ACTION_BLUE); btnEdit.setForeground(Color.WHITE);
            btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 12));
            
            JButton btnDelete = new JButton("Xóa");
            btnDelete.setBackground(ACTION_RED); btnDelete.setForeground(Color.WHITE);
            btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 12));

            btnEdit.addActionListener(e -> {
                int row = table.getEditingRow();
                if (row != -1) {
                    Object[] data = {model.getValueAt(row, 0), model.getValueAt(row, 1), 
                                    model.getValueAt(row, 2), model.getValueAt(row, 3), model.getValueAt(row, 4)};
                    fireEditingStopped();
                    openSupplierDialog(data);
                }
            });

            btnDelete.addActionListener(e -> {
                int row = table.getEditingRow();
                if (row != -1) {
                    String id = model.getValueAt(row, 0).toString();
                    if (JOptionPane.showConfirmDialog(null, "Xóa nhà cung cấp " + id + "?", 
                            "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM NHACUNGCAP WHERE MANCC=?")) {
                            pstmt.setString(1, id);
                            pstmt.executeUpdate();
                            fireEditingStopped();
                            loadData();
                        } catch (SQLException ex) { ex.printStackTrace(); }
                    } else { fireEditingStopped(); }
                }
            });

            panel.add(btnEdit); panel.add(btnDelete);
        }
        @Override public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            return panel;
        }
        @Override public Object getCellEditorValue() { return ""; }
    }
}