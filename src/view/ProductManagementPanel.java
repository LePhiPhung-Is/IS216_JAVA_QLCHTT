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

    // ===== DỮ LIỆU TỪ DATABASE =====
    private List<SanPham> products = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private final DecimalFormat df = new DecimalFormat("#,###đ");

    // ===== CỘT BẢNG =====
    private static final String[] COLUMNS = {"Ảnh", "Mã SP", "Tên sản phẩm", "Danh mục", "Giá bán", "Tồn kho", "Thao tác"};
    private static final int COL_ACTION = 6;

    public ProductManagementPanel() {
        setLayout(new BorderLayout());
        setBackground(MAIN_BG);
        setBorder(new EmptyBorder(28, 32, 28, 32));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);

        // Load dữ liệu thật từ Oracle
        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        SanPhamDAO dao = new SanPhamDAO();
        products = dao.getAllSanPham(); 
        filterTable(); 
    }

    // ===== HEADER: tiêu đề + tìm kiếm + nút thêm =====
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

        // --- Ô NHẬP TÌM KIẾM ---
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
        searchField.putClientProperty("JTextField.placeholderText", "🔍  Mã hoặc Tên SP...");
        
        // Vẫn giữ tính năng tự động lọc khi gõ chữ (rất tiện lợi)
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filterTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });

        // --- BỔ SUNG NÚT TÌM KIẾM ---
        JButton btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setBackground(BRAND_GOLD);
        btnTimKiem.setForeground(Color.BLACK); // Chữ đen cho dễ nhìn
        btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTimKiem.setFocusPainted(false);
        // Gắn sự kiện click nút cũng gọi hàm tìm kiếm
        btnTimKiem.addActionListener(e -> filterTable());

        // Bọc ô nhập và nút tìm kiếm vào chung 1 nhóm để nó đứng cạnh nhau
        JPanel searchBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchBoxPanel.setBackground(MAIN_BG);
        searchBoxPanel.add(searchField);
        searchBoxPanel.add(btnTimKiem);

        // --- NÚT THÊM SẢN PHẨM ---
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

        // Thêm các nhóm vào góc phải
        rightPanel.add(searchBoxPanel); 
        rightPanel.add(addBtn);
        
        header.add(titleBox, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        return header;
    }

    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == COL_ACTION; }
            @Override public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return ImageIcon.class; // Cột ảnh
                return super.getColumnClass(columnIndex);
            }
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
        table.setRowHeight(70); // Tăng chiều cao chứa ảnh
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
                p.getTenSP().toLowerCase().contains(kw)) {
                filtered.add(p);
            }
        }
        refreshTableModel(filtered);
    }

    private void refreshTableModel(List<SanPham> data) {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        for (SanPham sp : data) {
            // Lấy tên ảnh và cắt bỏ khoảng trắng thừa nếu có
            String tenFileAnh = sp.getHinhAnh() != null ? sp.getHinhAnh().trim() : "no_image.png";
            if (tenFileAnh.isEmpty()) tenFileAnh = "no_image.png";

        System.out.println("Đang load ảnh SP: " + sp.getMaSP() + " | Tên file DB trả về: [" + tenFileAnh + "]");
            ImageIcon imageIcon = loadImage("src/assets/product_images/" + tenFileAnh, 60, 60);
            
            tableModel.addRow(new Object[]{
                imageIcon, 
                sp.getMaSP(), 
                sp.getTenSP(), 
                sp.getMaDM(), 
                df.format(sp.getGiaBan()), 
                sp.getSoLuongTon(), 
                "" 
            });
        }
    }

  // ===== HÀM TEST LỖI ẢNH (Tạm thời) =====
    // ===== HÀM LOAD ẢNH CHUẨN =====
    private ImageIcon loadImage(String filePath, int width, int height) {
        try {
            // Dùng trực tiếp filePath được truyền vào từ refreshTableModel
            File f = new File(filePath); 
            
            if (f.exists()) {
                ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            } else {
                // Trả về ảnh mặc định nếu không tìm thấy file
                File defaultImg = new File("src/assets/product_images/no_image.png");
                if(defaultImg.exists()) {
                     ImageIcon icon = new ImageIcon(defaultImg.getAbsolutePath());
                     Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                     return new ImageIcon(img);
                }
                return new ImageIcon(); 
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ImageIcon();
        }
    }
    private void openProductDialog(SanPham existing, int productIndex) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner instanceof Frame ? (Frame) owner : null,
                existing == null ? "Thêm sản phẩm mới" : "Sửa sản phẩm", true);
        dialog.setSize(460, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(MAIN_BG);
        dialog.setVisible(true);
    }

    private void deleteProduct(int viewRow) {
        String name = (String) tableModel.getValueAt(viewRow, 2);
        String code = (String) tableModel.getValueAt(viewRow, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa sản phẩm \"" + name + "\" không?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            products.removeIf(p -> p.getMaSP().equals(code));
            filterTable();
        }
    }

    class ActionCellRenderer implements TableCellRenderer {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 15));
        private final JButton editBtn  = makeBtn("Sửa", EDIT_BLUE);
        private final JButton delBtn   = makeBtn("Xóa", DELETE_RED);

        ActionCellRenderer() {
            panel.setOpaque(true);
            panel.add(editBtn); panel.add(delBtn);
        }
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean focus, int row, int col) {
            panel.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
            return panel;
        }
        private JButton makeBtn(String text, Color bg) {
            JButton b = new JButton(text); 
            b.setBackground(bg); b.setForeground(Color.WHITE); b.setBorderPainted(false);
            return b;
        }
    }

    class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 15));
        private int currentRow;

        ActionCellEditor() {
            panel.setBackground(ROW_WHITE);
            JButton editBtn = new JButton("Sửa"); editBtn.setBackground(EDIT_BLUE); editBtn.setForeground(Color.WHITE);
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

            JButton delBtn = new JButton("Xóa"); delBtn.setBackground(DELETE_RED); delBtn.setForeground(Color.WHITE);
            delBtn.addActionListener(e -> {
                fireEditingStopped();
                deleteProduct(currentRow);
            });

            panel.add(editBtn); panel.add(delBtn);
        }
        @Override public Component getTableCellEditorComponent(JTable t, Object v, boolean sel, int row, int col) {
            currentRow = row; panel.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE); return panel;
        }
        @Override public Object getCellEditorValue() { return ""; }
    }
}