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
    private final Color EDIT_BLUE    = new Color(50, 120, 200);
    private final Color HIDE_GRAY    = new Color(140, 140, 140);
    private final Color HEADER_BG    = new Color(30, 30, 30);
    private final Color BACK_BTN_BG  = new Color(90, 90, 90);

    // ===== DỮ LIỆU =====
    private List<DanhMuc> danhSachDM   = new ArrayList<>();
    private List<SanPham> tatCaSanPham = new ArrayList<>();

    // ===== UI =====
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     cardPanel  = new JPanel(cardLayout);

    // ===== STATE =====
    private DanhMuc dangXemDM = null;

    // ===== BẢNG DANH MỤC =====
    private DefaultTableModel catTableModel;
    private static final String[] CAT_COLS  = {"Mã DM", "Tên danh mục", "Số sản phẩm", "Trạng thái", "Thao tác"};
    private static final int CAT_COL_ACTION = 4;

    // ===== BẢNG CHI TIẾT =====
    private DefaultTableModel detailTableModel;
    private JLabel            detailTitleLabel;
    private JLabel            detailCountLabel;
    private static final String[] DETAIL_COLS       = {"Mã SP", "Tên sản phẩm", "Giá bán", "Hiển thị", "Thao tác"};
    private static final int      DETAIL_COL_TOGGLE = 3;
    private static final int      DETAIL_COL_DELETE = 4;

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
        danhSachDM   = new DanhMucDAO().getAllDanhMuc();
        tatCaSanPham = new SanPhamDAO().getAllSanPham();
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

    private JPanel buildListHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAIN_BG);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Bên trái: tiêu đề
        JLabel title = new JLabel("QUẢN LÝ DANH MỤC");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(SIDEBAR_BG);

        JLabel subtitle = new JLabel("Nhấn vào tên danh mục để xem sản phẩm bên trong");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(130, 130, 130));

        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setBackground(MAIN_BG);
        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(subtitle);

        // Bên phải: nút thêm danh mục
        JButton addBtn = makeGoldButton("+ Thêm danh mục");
        addBtn.addActionListener(e -> openCategoryDialog(null));

        header.add(titleBox, BorderLayout.WEST);
        header.add(addBtn,   BorderLayout.EAST);
        return header;
    }

    private JScrollPane buildCatTable() {
        catTableModel = new DefaultTableModel(CAT_COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == CAT_COL_ACTION; }
        };

        JTable catTable = new JTable(catTableModel) {
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
        catTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        catTable.setRowHeight(56);
        catTable.setShowVerticalLines(false);
        catTable.setShowHorizontalLines(true);
        catTable.setGridColor(BORDER_COLOR);
        catTable.setSelectionBackground(new Color(212, 175, 55, 50));
        catTable.setIntercellSpacing(new Dimension(0, 0));
        catTable.setFocusable(false);

        styleTableHeader(catTable);

        // Căn giữa cột Mã, Số SP, Trạng thái
        DefaultTableCellRenderer centerR = centerRenderer();
        catTable.getColumnModel().getColumn(0).setCellRenderer(centerR);
        catTable.getColumnModel().getColumn(0).setPreferredWidth(90);
        catTable.getColumnModel().getColumn(1).setPreferredWidth(340);
        catTable.getColumnModel().getColumn(2).setCellRenderer(centerR);
        catTable.getColumnModel().getColumn(2).setPreferredWidth(110);
        catTable.getColumnModel().getColumn(3).setCellRenderer(centerR);
        catTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        catTable.getColumnModel().getColumn(4).setPreferredWidth(160);

        catTable.getColumnModel().getColumn(CAT_COL_ACTION).setCellRenderer(new CatActionRenderer());
        catTable.getColumnModel().getColumn(CAT_COL_ACTION).setCellEditor(new CatActionEditor(catTable));

        // Click vào cột tên (col 1) → mở chi tiết
        catTable.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = catTable.rowAtPoint(e.getPoint());
                int col = catTable.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 1 && row < danhSachDM.size()) {
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
            String trangThai = "AN".equalsIgnoreCase(dm.getTenDM()) ? "Đang ẩn" : "Hiển thị";
            // TODO: dùng field trangThai thực nếu model DanhMuc có thêm field đó
            catTableModel.addRow(new Object[]{
                dm.getMaDM(),
                dm.getTenDM(),
                dm.getDanhSachSanPham().size() + " SP",
                "Hiển thị",
                ""
            });
        }
    }

    // =====================================================================
    //  RENDERER & EDITOR — CỘT THAO TÁC DANH MỤC
    // =====================================================================

    class CatActionRenderer implements TableCellRenderer {
        private final JPanel  panel  = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 11));
        private final JButton edit   = makeBtn("✏", EDIT_BLUE);
        private final JButton delete = makeBtn("🗑", DELETE_RED);

        CatActionRenderer() {
            panel.setOpaque(true);
            panel.add(edit);
            panel.add(delete);
        }

        @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int row, int col) {
            panel.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
            return panel;
        }

        private JButton makeBtn(String text, Color bg) {
            JButton b = new JButton(text);
            b.setBackground(bg);
            b.setForeground(Color.WHITE);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            return b;
        }
    }

    class CatActionEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel  panel  = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 11));
        private int currentRow;

        CatActionEditor(JTable catTable) {
            JButton editBtn   = makeBtn("✏", EDIT_BLUE);
            JButton deleteBtn = makeBtn("🗑", DELETE_RED);

            // Sửa danh mục
            editBtn.addActionListener(e -> {
                fireEditingStopped();
                if (currentRow < danhSachDM.size())
                    openCategoryDialog(danhSachDM.get(currentRow));
            });

            // Xóa danh mục
            deleteBtn.addActionListener(e -> {
                fireEditingStopped();
                if (currentRow >= danhSachDM.size()) return;
                DanhMuc dm = danhSachDM.get(currentRow);
                int confirm = JOptionPane.showConfirmDialog(
                        CategoryManagementPanel.this,
                        "Xóa danh mục \"" + dm.getTenDM() + "\"?\nHành động này không thể hoàn tác.",
                        "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    danhSachDM.remove(currentRow);
                    // TODO: gọi DAO xóa xuống database
                    refreshCatTable();
                }
            });

            panel.add(editBtn);
            panel.add(deleteBtn);
        }

        @Override public Component getTableCellEditorComponent(
                JTable t, Object v, boolean sel, int row, int col) {
            currentRow = row;
            panel.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
            return panel;
        }
        @Override public Object getCellEditorValue() { return ""; }

        private JButton makeBtn(String text, Color bg) {
            JButton b = new JButton(text);
            b.setBackground(bg);
            b.setForeground(Color.WHITE);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            return b;
        }
    }

    // =====================================================================
    //  DIALOG — THÊM / SỬA DANH MỤC
    //  existing = null → thêm mới | existing != null → sửa (form có sẵn dữ liệu)
    // =====================================================================

    private void openCategoryDialog(DanhMuc existing) {
        boolean isEdit = (existing != null);

        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner instanceof Frame ? (Frame) owner : null,
                isEdit ? "Sửa danh mục" : "Thêm danh mục mới", true);
        dialog.setSize(420, 260);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(MAIN_BG);

        // ----- Title bar -----
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleBar.setBackground(HEADER_BG);
        titleBar.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel lbTitle = new JLabel(isEdit ? "SỬA DANH MỤC" : "THÊM DANH MỤC MỚI");
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

        JTextField fieldMa  = makeFormField(isEdit ? existing.getMaDM()  : "");
        JTextField fieldTen = makeFormField(isEdit ? existing.getTenDM() : "");

        fieldMa.setEditable(!isEdit); // Mã DM không cho sửa khi edit

        addFormRow(form, gbc, 0, "Mã danh mục:", fieldMa);
        addFormRow(form, gbc, 1, "Tên danh mục:", fieldTen);

        // ----- Nút Lưu / Hủy -----
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(MAIN_BG);
        btnRow.setBorder(new EmptyBorder(0, 28, 20, 28));

        JButton cancelBtn = makePlainButton("Hủy");
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton saveBtn = makeGoldButton(isEdit ? "Lưu thay đổi" : "Thêm mới");
        saveBtn.addActionListener(e -> {
            String maDM  = fieldMa.getText().trim();
            String tenDM = fieldTen.getText().trim();

            if (maDM.isEmpty() || tenDM.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Vui lòng điền đầy đủ Mã và Tên danh mục.",
                        "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (isEdit) {
                existing.setTenDM(tenDM);
                // TODO: gọi DAO cập nhật xuống database
            } else {
                DanhMuc newDM = new DanhMuc(maDM, tenDM);
                danhSachDM.add(newDM);
                // TODO: gọi DAO lưu xuống database
            }

            refreshCatTable();
            dialog.dispose();
        });

        btnRow.add(cancelBtn);
        btnRow.add(saveBtn);

        dialog.add(titleBar, BorderLayout.NORTH);
        dialog.add(form,     BorderLayout.CENTER);
        dialog.add(btnRow,   BorderLayout.SOUTH);
        dialog.setVisible(true);
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

    private JPanel buildDetailHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAIN_BG);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Bên trái: nút quay lại + tên danh mục
        JPanel leftSide = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftSide.setBackground(MAIN_BG);

        JButton backBtn = makeBackButton("← Quay lại");
        backBtn.addActionListener(e -> {
            refreshCatTable();
            cardLayout.show(cardPanel, "list");
        });

        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setBackground(MAIN_BG);
        titleBox.setBorder(new EmptyBorder(0, 16, 0, 0));

        detailTitleLabel = new JLabel("");
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

    private JScrollPane buildDetailTable() {
        detailTableModel = new DefaultTableModel(DETAIL_COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) {
                return c == DETAIL_COL_TOGGLE || c == DETAIL_COL_DELETE;
            }
            @Override public Class<?> getColumnClass(int c) {
                return c == DETAIL_COL_TOGGLE ? Boolean.class : String.class;
            }
        };

        JTable detailTable = new JTable(detailTableModel) {
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
        detailTable.getColumnModel().getColumn(1).setPreferredWidth(280);
        detailTable.getColumnModel().getColumn(2).setPreferredWidth(130);
        detailTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        detailTable.getColumnModel().getColumn(4).setPreferredWidth(110);

        // Renderer cột Hiển thị — nút toggle xanh/xám
        detailTable.getColumnModel().getColumn(DETAIL_COL_TOGGLE).setCellRenderer(
            (t, value, sel, foc, row, col) -> {
                JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
                wrap.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
                wrap.add(makeToggleButton(Boolean.TRUE.equals(value)));
                return wrap;
            }
        );
        detailTable.getColumnModel().getColumn(DETAIL_COL_TOGGLE).setCellEditor(
                new ToggleButtonEditor()
        );

        // Renderer cột Thao tác — nút Xóa đỏ
        detailTable.getColumnModel().getColumn(DETAIL_COL_DELETE).setCellRenderer(
            (t, value, sel, foc, row, col) -> {
                JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
                wrap.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
                wrap.add(makeDeleteButton());
                return wrap;
            }
        );
        detailTable.getColumnModel().getColumn(DETAIL_COL_DELETE).setCellEditor(
                new DeleteProductEditor(detailTable)
        );

        JScrollPane scroll = new JScrollPane(detailTable);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scroll.getViewport().setBackground(ROW_WHITE);
        return scroll;
    }

    // Vẽ nút toggle dựa vào trạng thái hiện tại
    private JButton makeToggleButton(boolean hienThi) {
        String label = hienThi ? "👁  Đang hiện" : "🙈  Đang ẩn";
        Color  bg    = hienThi ? new Color(40, 167, 80) : HIDE_GRAY;

        JButton btn = new JButton(label);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(5, 12, 5, 12));
        return btn;
    }

    // Editor riêng cho nút toggle ẩn/hiện sản phẩm
    class ToggleButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private int     curRow;
        private boolean curValue;
        private final JPanel  wrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        private final JButton btn  = new JButton();

        ToggleButtonEditor() {
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(new EmptyBorder(5, 12, 5, 12));

            btn.addActionListener(e -> {
                // Đảo trạng thái rồi commit
                curValue = !curValue;
                applyStyle();

                if (dangXemDM != null && curRow < dangXemDM.getDanhSachSanPham().size()) {
                    SanPham sp = dangXemDM.getDanhSachSanPham().get(curRow);
                    sp.setTrangThai(curValue ? "DANG_BAN" : "AN");
                    // TODO: gọi DAO cập nhật trạng thái xuống database
                }

                detailTableModel.setValueAt(curValue, curRow, DETAIL_COL_TOGGLE);
                updateDetailCount();
                fireEditingStopped();
            });

            wrap.setOpaque(true);
            wrap.add(btn);
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable t, Object value, boolean sel, int row, int col) {
            curRow   = row;
            curValue = Boolean.TRUE.equals(value);
            applyStyle();
            wrap.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
            return wrap;
        }

        @Override
        public Object getCellEditorValue() { return curValue; }

        private void applyStyle() {
            btn.setText(curValue ? "👁  Đang hiện" : "🙈  Đang ẩn");
            btn.setBackground(curValue ? new Color(40, 167, 80) : HIDE_GRAY);
        }
    }

    // Nút xóa SP khỏi danh mục
    private JButton makeDeleteButton() {
        JButton btn = new JButton("🗑 Xóa");
        btn.setBackground(DELETE_RED);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(5, 12, 5, 12));
        return btn;
    }

    // Editor xóa sản phẩm khỏi danh mục
    class DeleteProductEditor extends AbstractCellEditor implements TableCellEditor {
        private int           curRow;
        private final JPanel  wrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        private final JButton btn  = makeDeleteButton();

        DeleteProductEditor(JTable table) {
            wrap.setOpaque(true);
            wrap.add(btn);

            btn.addActionListener(e -> {
                fireEditingStopped();
                if (dangXemDM == null || curRow >= dangXemDM.getDanhSachSanPham().size()) return;

                SanPham sp = dangXemDM.getDanhSachSanPham().get(curRow);
                int confirm = JOptionPane.showConfirmDialog(
                        CategoryManagementPanel.this,
                        "Bạn có chắc muốn xóa sản phẩm \"" + sp.getTenSP() + "\" khỏi danh mục này?",
                        "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    dangXemDM.xoaSanPham(sp); // dùng business logic của DanhMuc model
                    // TODO: gọi DAO cập nhật xuống database
                    detailTableModel.removeRow(curRow);
                    updateDetailCount();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable t, Object value, boolean sel, int row, int col) {
            curRow = row;
            wrap.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
            return wrap;
        }

        @Override public Object getCellEditorValue() { return ""; }
    }

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
                hienThi,
                ""
            });
        }
        updateDetailCount();
    }

    private void updateDetailCount() {
        if (dangXemDM == null) return;
        int total   = dangXemDM.getDanhSachSanPham().size();
        int hienThi = dangXemDM.getSanPhamDangBan().size();
        detailCountLabel.setText(hienThi + " đang hiển thị  •  " + total + " sản phẩm");
    }

    // =====================================================================
    //  DIALOG — CHỌN SẢN PHẨM THÊM VÀO DANH MỤC
    // =====================================================================

    private void openPickProductDialog() {
        if (dangXemDM == null) return;

        List<SanPham> available = new ArrayList<>();
        for (SanPham sp : tatCaSanPham)
            if (!dangXemDM.getDanhSachSanPham().contains(sp))
                available.add(sp);

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

        // Bảng chọn SP
        String[] cols = {"", "Mã SP", "Tên sản phẩm", "Giá bán"};
        DefaultTableModel pickModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 0; }
            @Override public Class<?> getColumnClass(int c) { return c == 0 ? Boolean.class : String.class; }
        };
        for (SanPham sp : available)
            pickModel.addRow(new Object[]{false, sp.getMaSP(), sp.getTenSP(),
                    String.format("%,.0fđ", sp.getGiaBan())});

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
                    dangXemDM.themSanPham(available.get(i));
                    added++;
                }
            }
            if (added == 0) {
                JOptionPane.showMessageDialog(dialog,
                        "Chưa chọn sản phẩm nào.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // TODO: gọi DAO lưu xuống database
            refreshDetailTable();
            dialog.dispose();
        });

        btnRow.add(cancelBtn);
        btnRow.add(confirmBtn);

        dialog.add(titleBar,     BorderLayout.NORTH);
        dialog.add(tableWrapper, BorderLayout.CENTER);
        dialog.add(btnRow,       BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // =====================================================================
    //  HELPER — FORM
    // =====================================================================

    private JTextField makeFormField(String value) {
        JTextField f = new JTextField(value, 20);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(6, 10, 6, 10)));
        return f;
    }

    private void addFormRow(JPanel form, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(60, 60, 60));
        form.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        form.add(field, gbc);
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