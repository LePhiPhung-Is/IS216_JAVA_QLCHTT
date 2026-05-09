package src.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class CategoryManagementPanel extends JPanel {

    // ===== MÀU (khớp ProductManagementPanel) =====
    private final Color MAIN_BG      = new Color(244, 247, 246);
    private final Color BRAND_GOLD   = new Color(212, 175, 55);
    private final Color SIDEBAR_BG   = new Color(5, 5, 5);
    private final Color ROW_WHITE    = Color.WHITE;
    private final Color ROW_STRIPE   = new Color(250, 252, 251);
    private final Color BORDER_COLOR = new Color(220, 225, 222);
    private final Color DELETE_RED   = new Color(220, 60, 60);
    private final Color EDIT_BLUE    = new Color(50, 120, 200);
    private final Color HEADER_BG    = new Color(30, 30, 30);
    private final Color BACK_BTN     = new Color(90, 90, 90);

    // ===== DU LIEU =====
    private final List<String[]> categories = new ArrayList<>();
    private final List<String[]> allProducts = new ArrayList<>();
    private final Map<String, List<String[]>> catProducts = new LinkedHashMap<>();

    // ===== UI =====
    private CardLayout cardLayout;
    private JPanel cardPanel;

    // ===== BANG DANH MUC =====
    private DefaultTableModel catModel;
    private JTable catTable;

    private static final String[] CAT_COLS = {"Ma DM", "Ten danh muc", "Mo ta", "So SP", "Thao tac"};
    private static final int CAT_COL_ACTION = 4;

    public CategoryManagementPanel() {
        initData();
        setLayout(new BorderLayout());
        setBackground(MAIN_BG);

        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.setBackground(MAIN_BG);

        cardPanel.add(buildCategoryListView(), "list");
        add(cardPanel, BorderLayout.CENTER);
    }

    private void initData() {
        categories.add(new String[]{"DM001", "Son moi",   "Cac loai son duong va son mau"});
        categories.add(new String[]{"DM002", "Kem nen",   "Kem lot, cushion, BB cream"});
        categories.add(new String[]{"DM003", "Phan phu",  "Phan bot va phan nen"});
        categories.add(new String[]{"DM004", "Mat",       "Mascara, ke mat, phan mat"});
        categories.add(new String[]{"DM005", "Cham soc",  "Kem duong, tay trang, xit khoang"});

        allProducts.add(new String[]{"SP001", "Son moi do Ruby",       "DM001", "185.000", "42"});
        allProducts.add(new String[]{"SP002", "Son duong mat ong",     "DM001", "95.000",  "60"});
        allProducts.add(new String[]{"SP003", "Son li nhung den",      "DM001", "220.000", "28"});
        allProducts.add(new String[]{"SP004", "Kem nen CC Cream",      "DM002", "320.000", "18"});
        allProducts.add(new String[]{"SP005", "Cushion kiem dau",      "DM002", "380.000", "15"});
        allProducts.add(new String[]{"SP006", "Phan phu kiem dau",     "DM003", "210.000", "35"});
        allProducts.add(new String[]{"SP007", "Phan bot trong suot",   "DM003", "175.000", "40"});
        allProducts.add(new String[]{"SP008", "Mascara day mi",        "DM004", "155.000", "60"});
        allProducts.add(new String[]{"SP009", "Ke mat nuoc den",       "DM004", "125.000", "55"});
        allProducts.add(new String[]{"SP010", "Tay trang diu nhe",     "DM005", "98.000",  "27"});
        allProducts.add(new String[]{"SP011", "Kem duong am ban dem",  "DM005", "450.000", "12"});
        allProducts.add(new String[]{"SP012", "Xit khoang duong",      "DM005", "199.000", "33"});

        for (String[] cat : categories) {
            List<String[]> list = new ArrayList<>();
            for (String[] p : allProducts)
                if (p[2].equals(cat[0]))
                    list.add(new String[]{p[0], p[1], p[3], "1"});
            catProducts.put(cat[0], list);
        }
    }

    // =========================================================
    //  VIEW 1: DANH SACH DANH MUC
    // =========================================================
    private JPanel buildCategoryListView() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(MAIN_BG);
        root.setBorder(new EmptyBorder(28, 32, 28, 32));
        root.add(buildCatHeader(), BorderLayout.NORTH);
        root.add(buildCatTable(), BorderLayout.CENTER);
        return root;
    }

    private JPanel buildCatHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAIN_BG);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("QUAN LY DANH MUC");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(SIDEBAR_BG);
        JLabel subtitle = new JLabel("Danh sach toan bo danh muc san pham");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(130, 130, 130));

        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setBackground(MAIN_BG);
        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(subtitle);

        JButton addBtn = makeGoldBtn("+ Them danh muc");
        addBtn.addActionListener(e -> openCategoryDialog(null, -1));

        header.add(titleBox, BorderLayout.WEST);
        header.add(addBtn, BorderLayout.EAST);
        return header;
    }

    private JScrollPane buildCatTable() {
        catModel = new DefaultTableModel(CAT_COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == CAT_COL_ACTION; }
        };
        refreshCatModel();

        catTable = new JTable(catModel) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row))
                    c.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
                return c;
            }
        };
        catTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        catTable.setRowHeight(50);
        catTable.setShowVerticalLines(false);
        catTable.setShowHorizontalLines(true);
        catTable.setGridColor(BORDER_COLOR);
        catTable.setSelectionBackground(new Color(212, 175, 55, 40));
        catTable.setIntercellSpacing(new Dimension(0, 0));
        catTable.setFocusable(false);

        JTableHeader th = catTable.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(HEADER_BG);
        th.setForeground(BRAND_GOLD);
        th.setPreferredSize(new Dimension(0, 44));
        th.setBorder(BorderFactory.createEmptyBorder());
        th.setReorderingAllowed(false);

        catTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        catTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        catTable.getColumnModel().getColumn(2).setPreferredWidth(350);
        catTable.getColumnModel().getColumn(3).setPreferredWidth(70);
        catTable.getColumnModel().getColumn(4).setPreferredWidth(160);

        catTable.getColumnModel().getColumn(0).setCellRenderer(styledRenderer(BRAND_GOLD, Font.BOLD, SwingConstants.CENTER));
        catTable.getColumnModel().getColumn(1).setCellRenderer(styledRenderer(SIDEBAR_BG, Font.BOLD, SwingConstants.LEFT));
        catTable.getColumnModel().getColumn(2).setCellRenderer(styledRenderer(new Color(110, 110, 110), Font.PLAIN, SwingConstants.LEFT));
        catTable.getColumnModel().getColumn(3).setCellRenderer(styledRenderer(new Color(80, 80, 80), Font.PLAIN, SwingConstants.CENTER));

        catTable.getColumnModel().getColumn(CAT_COL_ACTION).setCellRenderer(new CatActionRenderer());
        catTable.getColumnModel().getColumn(CAT_COL_ACTION).setCellEditor(new CatActionEditor());

        catTable.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = catTable.rowAtPoint(e.getPoint());
                int col = catTable.columnAtPoint(e.getPoint());
                if (row >= 0 && col != CAT_COL_ACTION && row < categories.size())
                    openDetailView(categories.get(row));
            }
        });
        catTable.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JScrollPane scroll = new JScrollPane(catTable);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scroll.getViewport().setBackground(ROW_WHITE);
        return scroll;
    }

    private void refreshCatModel() {
        if (catModel == null) return;
        catModel.setRowCount(0);
        for (String[] cat : categories) {
            int count = catProducts.getOrDefault(cat[0], new ArrayList<>()).size();
            catModel.addRow(new Object[]{cat[0], cat[1], cat[2], count + " SP", ""});
        }
    }

    // =========================================================
    //  RENDERER & EDITOR COT THAO TAC DANH MUC
    // =========================================================
    class CatActionRenderer implements TableCellRenderer {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        CatActionRenderer() {
            panel.setOpaque(true);
            panel.add(makeActionBtn("Edit Sua", EDIT_BLUE));
            panel.add(makeActionBtn("Del Xoa", DELETE_RED));
        }
        @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int row, int col) {
            panel.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
            return panel;
        }
    }

    class CatActionEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        private int currentRow;

        CatActionEditor() {
            JButton editBtn = makeActionBtn("Edit Sua", EDIT_BLUE);
            editBtn.addActionListener(e -> {
                fireEditingStopped();
                if (currentRow < categories.size())
                    openCategoryDialog(categories.get(currentRow), currentRow);
            });
            JButton delBtn = makeActionBtn("Del Xoa", DELETE_RED);
            delBtn.addActionListener(e -> {
                fireEditingStopped();
                if (currentRow >= categories.size()) return;
                String[] cat = categories.get(currentRow);
                int confirm = JOptionPane.showConfirmDialog(
                        CategoryManagementPanel.this,
                        "Xoa danh muc \"" + cat[1] + "\"?",
                        "Xac nhan xoa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    catProducts.remove(cat[0]);
                    categories.remove(currentRow);
                    refreshCatModel();
                }
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

    // =========================================================
    //  VIEW 2: CHI TIET DANH MUC
    // =========================================================
    private void openDetailView(String[] cat) {
        String key = "detail_" + cat[0];
        cardPanel.add(buildDetailView(cat), key);
        cardLayout.show(cardPanel, key);
    }

    private JPanel buildDetailView(String[] cat) {
        List<String[]> products = catProducts.computeIfAbsent(cat[0], k -> new ArrayList<>());

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(MAIN_BG);
        root.setBorder(new EmptyBorder(28, 32, 28, 32));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAIN_BG);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel leftSide = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftSide.setBackground(MAIN_BG);

        JButton backBtn = new JButton("<- Quay lai") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? BACK_BTN.darker() : BACK_BTN);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backBtn.setForeground(Color.WHITE);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setBorder(new EmptyBorder(8, 16, 8, 16));
        backBtn.addActionListener(e -> {
            refreshCatModel();
            cardLayout.show(cardPanel, "list");
        });

        JLabel countLabel = new JLabel(products.size() + " san pham");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        countLabel.setForeground(new Color(130, 130, 130));

        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setBackground(MAIN_BG);
        titleBox.setBorder(new EmptyBorder(0, 16, 0, 0));
        JLabel title = new JLabel(cat[1].toUpperCase());
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(SIDEBAR_BG);
        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(3));
        titleBox.add(countLabel);

        leftSide.add(backBtn);
        leftSide.add(titleBox);

        JButton addProductBtn = makeGoldBtn("+ Them san pham");
        addProductBtn.addActionListener(e -> openAddProductDialog(cat[0], products, countLabel));

        header.add(leftSide, BorderLayout.WEST);
        header.add(addProductBtn, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);
        root.add(buildDetailTable(cat[0], products, countLabel), BorderLayout.CENTER);
        return root;
    }

    private JScrollPane buildDetailTable(String catCode, List<String[]> products, JLabel countLabel) {
        String[] cols = {"Ma SP", "Ten san pham", "Gia (VND)", "Hien thi", "Thao tac"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 3 || c == 4; }
            @Override public Class<?> getColumnClass(int c) { return c == 3 ? Boolean.class : Object.class; }
        };
        for (String[] p : products)
            model.addRow(new Object[]{p[0], p[1], p[2], "1".equals(p[3]), ""});

        JTable table = new JTable(model) {
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
        table.setSelectionBackground(new Color(212, 175, 55, 40));
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

        table.getColumnModel().getColumn(0).setCellRenderer(centerR);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setCellRenderer(rightR);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);

        model.addTableModelListener(e -> {
            if (e.getColumn() == 3) {
                int row = e.getFirstRow();
                if (row < products.size())
                    products.get(row)[3] = (Boolean) model.getValueAt(row, 3) ? "1" : "0";
            }
        });

        table.getColumnModel().getColumn(4).setCellRenderer((t, v, sel, foc, row, col) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 9));
            p.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
            p.add(makeActionBtn("X Xoa khoi DM", DELETE_RED));
            return p;
        });

        table.getColumnModel().getColumn(4).setCellEditor(new AbstractCellEditor() {
            int curRow;
            @Override public Object getCellEditorValue() { return ""; }
            @Override public Component getTableCellEditorComponent(
                    JTable t, Object v, boolean sel, int row, int col) {
                curRow = row;
                JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 9));
                p.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
                JButton b = makeActionBtn("X Xoa khoi DM", DELETE_RED);
                b.addActionListener(ev -> {
                    fireEditingStopped();
                    if (curRow >= products.size()) return;
                    int confirm = JOptionPane.showConfirmDialog(
                            CategoryManagementPanel.this,
                            "Xoa \"" + products.get(curRow)[1] + "\" khoi danh muc?",
                            "Xac nhan", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        products.remove(curRow);
                        model.removeRow(curRow);
                        countLabel.setText(products.size() + " san pham");
                    }
                });
                p.add(b);
                return p;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scroll.getViewport().setBackground(ROW_WHITE);
        return scroll;
    }

    // =========================================================
    //  DIALOG: CHON SAN PHAM THEM VAO DANH MUC
    // =========================================================
    private void openAddProductDialog(String catCode, List<String[]> currentProducts, JLabel countLabel) {
        Set<String> existing = new HashSet<>();
        for (String[] p : currentProducts) existing.add(p[0]);
        List<String[]> available = new ArrayList<>();
        for (String[] p : allProducts)
            if (!existing.contains(p[0])) available.add(p);

        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner instanceof Frame ? (Frame) owner : null,
                "Chon san pham them vao danh muc", true);
        dialog.setSize(580, 480);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(MAIN_BG);

        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleBar.setBackground(HEADER_BG);
        titleBar.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel dlgTitle = new JLabel("CHON SAN PHAM");
        dlgTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dlgTitle.setForeground(BRAND_GOLD);
        JLabel dlgSub = new JLabel("  -  Tick chon san pham muon them vao danh muc");
        dlgSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dlgSub.setForeground(new Color(180, 180, 180));
        titleBar.add(dlgTitle);
        titleBar.add(dlgSub);

        DefaultTableModel pickModel = new DefaultTableModel(
                new String[]{"", "Ma SP", "Ten san pham", "Gia (VND)"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 0; }
            @Override public Class<?> getColumnClass(int c) { return c == 0 ? Boolean.class : String.class; }
        };
        for (String[] p : available)
            pickModel.addRow(new Object[]{false, p[0], p[1], p[3]});

        JTable pickTable = new JTable(pickModel) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row))
                    c.setBackground(row % 2 == 0 ? ROW_WHITE : ROW_STRIPE);
                return c;
            }
        };
        pickTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pickTable.setRowHeight(44);
        pickTable.setShowVerticalLines(false);
        pickTable.setGridColor(BORDER_COLOR);
        pickTable.setIntercellSpacing(new Dimension(0, 0));
        pickTable.setFocusable(false);

        JTableHeader ph = pickTable.getTableHeader();
        ph.setFont(new Font("Segoe UI", Font.BOLD, 13));
        ph.setBackground(HEADER_BG);
        ph.setForeground(BRAND_GOLD);
        ph.setPreferredSize(new Dimension(0, 40));
        ph.setBorder(BorderFactory.createEmptyBorder());
        ph.setReorderingAllowed(false);

        pickTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        pickTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        pickTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        pickTable.getColumnModel().getColumn(3).setPreferredWidth(100);

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(MAIN_BG);
        tableWrapper.setBorder(new EmptyBorder(16, 20, 8, 20));

        if (available.isEmpty()) {
            JLabel empty = new JLabel("Tat ca san pham da co trong danh muc nay.", SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            empty.setForeground(new Color(150, 150, 150));
            tableWrapper.add(empty, BorderLayout.CENTER);
        } else {
            JScrollPane pickScroll = new JScrollPane(pickTable);
            pickScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
            pickScroll.getViewport().setBackground(ROW_WHITE);
            tableWrapper.add(pickScroll, BorderLayout.CENTER);
        }

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        btnRow.setBackground(MAIN_BG);
        btnRow.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        JButton cancelBtn = new JButton("Huy");
        cancelBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancelBtn.setForeground(new Color(80, 80, 80));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(8, 20, 8, 20)));
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton confirmBtn = makeGoldBtn("Xac nhan");
        confirmBtn.addActionListener(e -> {
            int added = 0;
            for (int i = 0; i < pickModel.getRowCount(); i++) {
                if ((Boolean) pickModel.getValueAt(i, 0)) {
                    String[] p = available.get(i);
                    currentProducts.add(new String[]{p[0], p[1], p[3], "1"});
                    added++;
                }
            }
            if (added == 0) {
                JOptionPane.showMessageDialog(dialog,
                        "Chua chon san pham nao.", "Thong bao", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            countLabel.setText(currentProducts.size() + " san pham");
            dialog.dispose();
            for (String[] cat : categories) {
                if (cat[0].equals(catCode)) {
                    openDetailView(cat);
                    break;
                }
            }
        });

        btnRow.add(cancelBtn);
        btnRow.add(confirmBtn);

        dialog.add(titleBar, BorderLayout.NORTH);
        dialog.add(tableWrapper, BorderLayout.CENTER);
        dialog.add(btnRow, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // =========================================================
    //  DIALOG: THEM / SUA DANH MUC
    // =========================================================
    private void openCategoryDialog(String[] existing, int idx) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner instanceof Frame ? (Frame) owner : null,
                existing == null ? "Them danh muc moi" : "Sua danh muc", true);
        dialog.setSize(420, 290);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(MAIN_BG);

        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleBar.setBackground(HEADER_BG);
        titleBar.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel dlgTitle = new JLabel(existing == null ? "THEM DANH MUC MOI" : "SUA DANH MUC");
        dlgTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dlgTitle.setForeground(BRAND_GOLD);
        titleBar.add(dlgTitle);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(MAIN_BG);
        form.setBorder(new EmptyBorder(20, 30, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 4, 10, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels   = {"Ma danh muc:", "Ten danh muc:", "Mo ta:"};
        String[] defaults = existing != null
                ? new String[]{existing[0], existing[1], existing[2]}
                : new String[]{"", "", ""};
        JTextField[] fields = new JTextField[3];

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

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(MAIN_BG);
        btnRow.setBorder(new EmptyBorder(0, 30, 20, 30));

        JButton cancelBtn = new JButton("Huy");
        cancelBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancelBtn.setForeground(new Color(80, 80, 80));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(8, 20, 8, 20)));
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton saveBtn = makeGoldBtn("Luu");
        saveBtn.addActionListener(e -> {
            String code = fields[0].getText().trim();
            String name = fields[1].getText().trim();
            String desc = fields[2].getText().trim();
            if (code.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Vui long nhap Ma va Ten danh muc.", "Loi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (idx < 0) {
                categories.add(new String[]{code, name, desc});
                catProducts.put(code, new ArrayList<>());
            } else {
                categories.set(idx, new String[]{code, name, desc});
            }
            refreshCatModel();
            dialog.dispose();
        });

        btnRow.add(cancelBtn);
        btnRow.add(saveBtn);

        dialog.add(titleBar, BorderLayout.NORTH);
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnRow, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // =========================================================
    //  HELPERS
    // =========================================================
    private DefaultTableCellRenderer styledRenderer(Color fg, int fontStyle, int align) {
        return new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setForeground(fg);
                setFont(new Font("Segoe UI", fontStyle, 13));
                setHorizontalAlignment(align);
                setBorder(new EmptyBorder(0, align == SwingConstants.CENTER ? 0 : 10, 0, 0));
                return this;
            }
        };
    }

    private JButton makeGoldBtn(String text) {
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