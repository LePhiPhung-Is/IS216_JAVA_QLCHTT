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
 * Sinh viên thực hiện: ĐOÀN XUÂN CHIẾN - MSSV: 24520217
 */
public class QuanLyNhaCungCap extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private JButton btnAdd;
    private JButton btnSearch;

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
        JPanel pnlHeader = new JPanel(new BorderLayout(20, 0));
        pnlHeader.setOpaque(false);

        JPanel pnlTitle = new JPanel(new GridLayout(2, 1));
        pnlTitle.setOpaque(false);
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÀ CUNG CẤP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.BLACK);
        
        JLabel lblSubTitle = new JLabel("Danh sách toàn bộ đối tác cung ứng của cửa hàng");
        lblSubTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubTitle.setForeground(Color.GRAY);
        pnlTitle.add(lblTitle);
        pnlTitle.add(lblSubTitle);

        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlActions.setOpaque(false);

        JPanel pnlSearchGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlSearchGroup.setOpaque(false);
        
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(150, 35)); 
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBackground(new Color(240, 240, 240));
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSearch.setPreferredSize(new Dimension(100, 35));
        btnSearch.setFocusPainted(false);

        pnlSearchGroup.add(txtSearch);
        pnlSearchGroup.add(btnSearch);
        
        btnAdd = new JButton("+ Thêm nhà cung cấp");
        btnAdd.setBackground(BRAND_GOLD);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setPreferredSize(new Dimension(200, 40));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);

        pnlActions.add(pnlSearchGroup);
        pnlActions.add(btnAdd);

        pnlHeader.add(pnlTitle, BorderLayout.WEST);
        pnlHeader.add(pnlActions, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        btnSearch.addActionListener(e -> loadData());
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
        table.setShowVerticalLines(false);
        
        table.setSelectionBackground(new Color(235, 235, 235)); 
        table.setSelectionForeground(Color.BLACK); 
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setForeground(BRAND_GOLD);
        header.setPreferredSize(new Dimension(0, 45));

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(250);
        columnModel.getColumn(2).setPreferredWidth(300);
        columnModel.getColumn(3).setPreferredWidth(150);
        columnModel.getColumn(4).setPreferredWidth(200);
        columnModel.getColumn(5).setPreferredWidth(180);

        table.getColumnModel().getColumn(5).setCellRenderer(new ActionButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ActionButtonEditor());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
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
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối: " + ex.getMessage());
        }
    }

    private void openSupplierDialog(Object[] data) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                data == null ? "Thêm nhà cung cấp" : "Cập nhật NCC", true);
        dialog.setSize(450, 500);
        dialog.setLayout(new GridBagLayout());
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 20, 8, 20);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField m = new JTextField(data != null ? data[0].toString() : "");
        if (data != null) m.setEditable(false);
        
        JTextField t = new JTextField(data != null ? data[1].toString() : "");
        JTextField d = new JTextField(data != null ? data[2].toString() : "");
        JTextField s = new JTextField(data != null ? data[3].toString() : "");
        JTextField e = new JTextField(data != null ? data[4].toString() : "");

        addField(dialog, "Mã NCC:", m, g, 0);
        addField(dialog, "Tên đối tác:", t, g, 1);
        addField(dialog, "Địa chỉ:", d, g, 2);
        addField(dialog, "Số điện thoại:", s, g, 3);
        addField(dialog, "Email:", e, g, 4);

        JButton btnSave = new JButton("LƯU DỮ LIỆU");
        btnSave.setBackground(BRAND_GOLD);
        btnSave.setForeground(Color.BLACK);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setPreferredSize(new Dimension(0, 45));
        
        g.gridx = 0; g.gridy = 5; g.gridwidth = 2; g.insets = new Insets(25, 20, 20, 20);
        dialog.add(btnSave, g);

        btnSave.addActionListener(ev -> {
            if (t.getText().trim().isEmpty() || m.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đủ Mã và Tên!");
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
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            return false;
        }
    }

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
            panel.setBackground(new Color(245, 245, 245));
            
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
                            int rowsAffected = pstmt.executeUpdate();
                            
                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(null, "Đã xóa nhà cung cấp thành công!");
                            }
                            
                            fireEditingStopped();
                            loadData();
                        } catch (SQLException ex) { 
                            // Xử lý lỗi ràng buộc khóa ngoại (ORA-02292)
                            if (ex.getErrorCode() == 2292) {
                                JOptionPane.showMessageDialog(null, "Không thể xóa! Nhà cung cấp này đã có dữ liệu liên quan trong các phiếu nhập.");
                            } else {
                                JOptionPane.showMessageDialog(null, "Lỗi Database: " + ex.getMessage());
                            }
                            fireEditingStopped();
                        }
                    } else { 
                        fireEditingStopped(); 
                    }
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