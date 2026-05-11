package src.view;
 
import src.dao.DanhMucDAO;
import src.dao.SanPhamDAO;
import src.model.DanhMuc;
import src.model.SanPham;
 
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
 
public class CategoryManagementPanel extends JPanel {
 
    // ===== MÀU GIAO DIỆN =====
    private final Color MAIN_BG      = new Color(244, 247, 246);
    private final Color BRAND_GOLD   = new Color(212, 175, 55);
    private final Color SIDEBAR_BG   = new Color(5, 5, 5);
    private final Color ROW_WHITE    = Color.WHITE;
    private final Color ROW_STRIPE   = new Color(250, 252, 251);
    private final Color BORDER_COLOR = new Color(220, 225, 222);
    private final Color DELETE_RED   = new Color(220, 60, 60);
    private final Color HEADER_BG    = new Color(30, 30, 30);
    private final Color BACK_BTN_BG  = new Color(90, 90, 90);
 
    // ===== DỮ LIỆU =====
    private List<DanhMuc> danhSachDM  = new ArrayList<>();
    private List<SanPham> tatCaSanPham = new ArrayList<>();
 
    // ===== UI — CARD LAYOUT =====
    // Dùng CardLayout để chuyển giữa 2 màn hình:
    //   "list"   → danh sách danh mục
    //   "detail" → chi tiết sản phẩm trong danh mục đang chọn
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     cardPanel  = new JPanel(cardLayout);
 
    // ===== STATE =====
    private DanhMuc dangXemDM = null; // danh mục đang được xem chi tiết
 
    // ===== BẢNG DANH MỤC =====
    private DefaultTableModel catTableModel;
 
    // ===== BẢNG CHI TIẾT =====
    private DefaultTableModel detailTableModel;
    private JLabel            detailTitleLabel;
    private JLabel            detailCountLabel;
 
    // ===== CỘT BẢNG =====
    private static final String[] CAT_COLS    = {"Mã DM", "Tên danh mục", "Số sản phẩm"};
    private static final String[] DETAIL_COLS = {"Mã SP", "Tên sản phẩm", "Giá bán", "Hiển thị"};
    private static final int DETAIL_COL_TOGGLE = 3;
 
    // =====================================================================
    public CategoryManagementPanel() {
        setLayout(new BorderLayout());
        setBackground(MAIN_BG);
 
        cardPanel.setBackground(MAIN_BG);
        cardPanel.add(buildListView(),   "list");
        cardPanel.add(buildDetailView(), "detail");
        add(cardPanel, BorderLayout.CENTER);
 
        loadData();
    }
 
    // =====================================================================
    //  LOAD DATA
    // =====================================================================
 
    private void loadData() {
        DanhMucDAO dmDAO  = new DanhMucDAO();
        SanPhamDAO  spDAO = new SanPhamDAO();
 
        danhSachDM   = dmDAO.getAllDanhMuc();
        tatCaSanPham = spDAO.getAllSanPham();
 
        refreshCatTable();
    }
 
    // =====================================================================
    //  MÀN HÌNH 1 — DANH SÁCH DANH MỤC
    // =====================================================================
 
    private JPanel buildListView() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(MAIN_BG);
        panel.setBorder(new EmptyBorder(28, 32, 28, 32));
        panel.add(buildListHeader(), BorderLayout.NORTH);
        panel.add(buildCatTable(),   BorderLayout.CENTER);
        return panel;
    }
 
    // ----- Header -----
    private JPanel buildListHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAIN_BG);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
 
        JLabel title    = new JLabel("QUẢN LÝ DANH MỤC");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(SIDEBAR_BG);
 
        JLabel subtitle = new JLabel("Nhấn vào một danh mục để xem sản phẩm bên trong");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(130, 130, 130));
 
        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setBackground(MAIN_BG);
        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(subtitle);
 
        header.add(titleBox, BorderLayout.WEST);
        return header;
    }
 
    // ----- Bảng danh mục -----
    private JScrollPane buildCatTable() {
        catTableModel = new DefaultTableModel(CAT_COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
 
        JTable catTable = new JTable(catTableModel) {
            @Override
public Component prepareRenderer(TableCellRenderer r, int row, int col) {
    Component c = super.prepareRenderer(r, row, col);

        if (isRowSelected(row)) {
            c.setBackground(getSelectionBackground());
            c.setForeground(Color.BLACK); // QUAN TRỌNG
        } else {
            c.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
            c.setForeground(Color.BLACK);
        }

        return c;
    }
        };
 
        catTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        catTable.setRowHeight(52);
        catTable.setShowVerticalLines(false);
        catTable.setShowHorizontalLines(true);
        catTable.setGridColor(BORDER_COLOR);
        catTable.setSelectionBackground(new Color(212, 175, 55, 50));
        catTable.setIntercellSpacing(new Dimension(0, 0));
        catTable.setFocusable(false);
        catTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
 
        styleTableHeader(catTable);
 
        DefaultTableCellRenderer centerR = centerRenderer();
        catTable.getColumnModel().getColumn(0).setCellRenderer(centerR);
        catTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        catTable.getColumnModel().getColumn(1).setPreferredWidth(400);
        catTable.getColumnModel().getColumn(2).setCellRenderer(centerR);
        catTable.getColumnModel().getColumn(2).setPreferredWidth(150);
 
        // Click → mở màn hình chi tiết
        catTable.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = catTable.rowAtPoint(e.getPoint());
                if (row >= 0 && row < danhSachDM.size()) {
                    dangXemDM = danhSachDM.get(row);
                    showDetailView(dangXemDM);
                }
            }
        });
 
        JScrollPane scroll = new JScrollPane(catTable);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scroll.getViewport().setBackground(ROW_WHITE);
        return scroll;
    }
 
    private void refreshCatTable() {
        catTableModel.setRowCount(0);
        for (DanhMuc dm : danhSachDM) {
            catTableModel.addRow(new Object[]{
                dm.getMaDM(),
                dm.getTenDM(),
                dm.getDanhSachSanPham().size() + " sản phẩm"
            });
        }
    }
 
    // =====================================================================
    //  MÀN HÌNH 2 — CHI TIẾT DANH MỤC
    // =====================================================================
 
    private JPanel buildDetailView() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(MAIN_BG);
        panel.setBorder(new EmptyBorder(28, 32, 28, 32));
        panel.add(buildDetailHeader(), BorderLayout.NORTH);
        panel.add(buildDetailTable(),  BorderLayout.CENTER);
        return panel;
    }
 
    // ----- Header chi tiết -----
    private JPanel buildDetailHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAIN_BG);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
 
        // Bên trái: nút quay lại + tên danh mục
        JPanel leftSide = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftSide.setBackground(MAIN_BG);
 
        JButton backBtn = makeBackButton("← Quay lại");
        backBtn.addActionListener(e -> {
            refreshCatTable(); // cập nhật số SP nếu thay đổi
            cardLayout.show(cardPanel, "list");
        });
 
        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setBackground(MAIN_BG);
        titleBox.setBorder(new EmptyBorder(0, 16, 0, 0));
 
        detailTitleLabel = new JLabel(""); // được set lại khi mở
        detailTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        detailTitleLabel.setForeground(SIDEBAR_BG);
 
        detailCountLabel = new JLabel("");
        detailCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailCountLabel.setForeground(new Color(130, 130, 130));
 
        titleBox.add(detailTitleLabel);
        titleBox.add(Box.createVerticalStrut(3));
        titleBox.add(detailCountLabel);
 
        leftSide.add(backBtn);
        leftSide.add(titleBox);
 
        // Bên phải: nút thêm sản phẩm vào danh mục
        JButton addProductBtn = makeGoldButton("+ Thêm sản phẩm");
        addProductBtn.addActionListener(e -> openPickProductDialog());
 
        header.add(leftSide,      BorderLayout.WEST);
        header.add(addProductBtn, BorderLayout.EAST);
        return header;
    }
 
    // ----- Bảng sản phẩm trong danh mục -----
    private JScrollPane buildDetailTable() {
        detailTableModel = new DefaultTableModel(DETAIL_COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == DETAIL_COL_TOGGLE; }
            @Override public Class<?> getColumnClass(int c) {
                return c == DETAIL_COL_TOGGLE ? Boolean.class : String.class;
            }
        };
 
        // Khi người dùng toggle checkbox → gọi DanhMuc.anSanPham() hoặc bật lại
        detailTableModel.addTableModelListener(e -> {
            if (e.getColumn() != DETAIL_COL_TOGGLE || dangXemDM == null) return;
            int row         = e.getFirstRow();
            boolean hienThi = (Boolean) detailTableModel.getValueAt(row, DETAIL_COL_TOGGLE);
            SanPham sp      = dangXemDM.getDanhSachSanPham().get(row);
 
            sp.setTrangThai(hienThi ? "DANG_BAN" : "AN");
            // TODO: gọi DAO cập nhật trạng thái xuống database nếu cần
            updateDetailCount();
        });
 
        JTable detailTable = new JTable(detailTableModel) {
    @Override 
    public Component prepareRenderer(TableCellRenderer r, int row, int col) {
        Component c = super.prepareRenderer(r, row, col);

        if (isRowSelected(row)) {
            c.setBackground(getSelectionBackground());
            c.setForeground(Color.BLACK); // 🔥 QUAN TRỌNG
        } else {
            c.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
            c.setForeground(Color.BLACK);
        }

        return c;
    }
};
        detailTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailTable.setRowHeight(52);
        detailTable.setShowVerticalLines(false);
        detailTable.setShowHorizontalLines(true);
        detailTable.setGridColor(BORDER_COLOR);
        detailTable.setSelectionBackground(new Color(212, 175, 55, 50));
        detailTable.setIntercellSpacing(new Dimension(0, 0));
        detailTable.setFocusable(false);
 
        styleTableHeader(detailTable);
 
        DefaultTableCellRenderer centerR = centerRenderer();
        detailTable.getColumnModel().getColumn(0).setCellRenderer(centerR);
        detailTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        detailTable.getColumnModel().getColumn(1).setPreferredWidth(380);
        detailTable.getColumnModel().getColumn(2).setPreferredWidth(140);
        detailTable.getColumnModel().getColumn(3).setPreferredWidth(90);
 
        JScrollPane scroll = new JScrollPane(detailTable);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scroll.getViewport().setBackground(ROW_WHITE);
        return scroll;
    }
 
    // ----- Hiển thị chi tiết một danh mục -----
    private void showDetailView(DanhMuc dm) {
        detailTitleLabel.setText(dm.getTenDM().toUpperCase());
        refreshDetailTable();
        cardLayout.show(cardPanel, "detail");
    }
 
    private void refreshDetailTable() {
        detailTableModel.setRowCount(0);
        if (dangXemDM == null) return;
 
        for (SanPham sp : dangXemDM.getDanhSachSanPham()) {
            boolean hienThi = !"AN".equalsIgnoreCase(sp.getTrangThai());
            detailTableModel.addRow(new Object[]{
                sp.getMaSP(),
                sp.getTenSP(),
                String.format("%,.0fđ", sp.getGiaBan()),
                hienThi
            });
        }
        updateDetailCount();
    }
 
    private void updateDetailCount() {
        if (dangXemDM == null) return;
        int total    = dangXemDM.getDanhSachSanPham().size();
        int hienThi  = dangXemDM.getSanPhamDangBan().size();
        detailCountLabel.setText(hienThi + " đang hiển thị  •  " + total + " sản phẩm");
    }
 
    // =====================================================================
    //  DIALOG — CHỌN SẢN PHẨM THÊM VÀO DANH MỤC
    // =====================================================================
 
    private void openPickProductDialog() {
        if (dangXemDM == null) return;
 
        // Lọc SP chưa thuộc danh mục này
        List<SanPham> available = new ArrayList<>();
        for (SanPham sp : tatCaSanPham) {
            if (!dangXemDM.getDanhSachSanPham().contains(sp))
                available.add(sp);
        }
 
        // ---- Tạo dialog ----
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner instanceof Frame ? (Frame) owner : null,
                "Thêm sản phẩm vào danh mục", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(MAIN_BG);
 
        // Title bar
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleBar.setBackground(HEADER_BG);
        titleBar.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel lbTitle = new JLabel("CHỌN SẢN PHẨM  —  " + dangXemDM.getTenDM());
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbTitle.setForeground(BRAND_GOLD);
        titleBar.add(lbTitle);
 
        // Bảng sản phẩm có thể chọn (checkbox ở cột 0)
        String[] cols = {"", "Mã SP", "Tên sản phẩm", "Giá bán"};
        DefaultTableModel pickModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 0; }
            @Override public Class<?> getColumnClass(int c) { return c == 0 ? Boolean.class : String.class; }
        };
        for (SanPham sp : available) {
            pickModel.addRow(new Object[]{
                false,
                sp.getMaSP(),
                sp.getTenSP(),
                String.format("%,.0fđ", sp.getGiaBan())
            });
        }
 
        JTable pickTable = new JTable(pickModel) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row))
                    c.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
                return c;
            }
        };
        pickTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pickTable.setRowHeight(48);
        pickTable.setShowVerticalLines(false);
        pickTable.setGridColor(BORDER_COLOR);
        pickTable.setIntercellSpacing(new Dimension(0, 0));
        pickTable.setFocusable(false);
 
        styleTableHeader(pickTable);
 
        pickTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        pickTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        pickTable.getColumnModel().getColumn(2).setPreferredWidth(320);
        pickTable.getColumnModel().getColumn(3).setPreferredWidth(120);
 
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(MAIN_BG);
        tableWrapper.setBorder(new EmptyBorder(16, 20, 8, 20));
 
        if (available.isEmpty()) {
            JLabel empty = new JLabel("Tất cả sản phẩm đã có trong danh mục này.", SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            empty.setForeground(new Color(150, 150, 150));
            tableWrapper.add(empty, BorderLayout.CENTER);
        } else {
            JScrollPane pickScroll = new JScrollPane(pickTable);
            pickScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
            pickScroll.getViewport().setBackground(ROW_WHITE);
            tableWrapper.add(pickScroll, BorderLayout.CENTER);
        }
 
        // Nút Xác nhận / Hủy
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        btnRow.setBackground(MAIN_BG);
        btnRow.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
 
        JButton cancelBtn = makePlainButton("Hủy");
        cancelBtn.addActionListener(e -> dialog.dispose());
 
        JButton confirmBtn = makeGoldButton("✔ Xác nhận");
        confirmBtn.addActionListener(e -> {
            int added = 0;
            for (int i = 0; i < pickModel.getRowCount(); i++) {
                if ((Boolean) pickModel.getValueAt(i, 0)) {
                    dangXemDM.themSanPham(available.get(i)); // dùng business logic của model
                    added++;
                }
            }
            if (added == 0) {
                JOptionPane.showMessageDialog(dialog,
                        "Chưa chọn sản phẩm nào.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // TODO: gọi DAO lưu xuống database nếu cần
            refreshDetailTable();
            dialog.dispose();
        });
 
        btnRow.add(cancelBtn);
        btnRow.add(confirmBtn);
 
        dialog.add(titleBar,      BorderLayout.NORTH);
        dialog.add(tableWrapper,  BorderLayout.CENTER);
        dialog.add(btnRow,        BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
 
    // =====================================================================
    //  HELPER — STYLE CHUNG
    // =====================================================================
 
    private void styleTableHeader(JTable table) {
        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(HEADER_BG);
        th.setForeground(BRAND_GOLD);
        th.setPreferredSize(new Dimension(0, 44));
        th.setBorder(BorderFactory.createEmptyBorder());
        th.setReorderingAllowed(false);
    }
 
    private DefaultTableCellRenderer centerRenderer() {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        return r;
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
 
    private JButton makeBackButton(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? BACK_BTN_BG.darker() : BACK_BTN_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
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