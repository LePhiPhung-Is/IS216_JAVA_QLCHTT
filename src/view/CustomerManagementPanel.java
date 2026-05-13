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
    // ===== MÀU SẮC & BIẾN ĐẶC BIỆT =====
    public final Color MAIN_BG = new Color(244, 247, 246);
    public final Color BRAND_GOLD = new Color(212, 175, 55);
    public final Color SIDEBAR_BG = new Color(5, 5, 5);
    public final Color DELETE_RED = new Color(220, 60, 60);
    public final Color EDIT_BLUE = new Color(50, 120, 200);
    public final Color HEADER_BG = new Color(30, 30, 30);
    
    public ArrayList<Integer> baibao = new ArrayList<>(); // Biến yêu cầu riêng
    private List<KhachHang> customers = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private KhachHangDAO dao = new KhachHangDAO();

    public CustomerManagementPanel() {
        setLayout(new BorderLayout());
        setBackground(MAIN_BG);
        setBorder(new EmptyBorder(28, 32, 28, 32));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        loadData();
    }

    public void loadData() {
        customers = dao.getAllKhachHang();
        refreshTable(customers);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAIN_BG);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("QUẢN LÝ KHÁCH HÀNG");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setBackground(MAIN_BG);
        
        searchField = new JTextField(18);
        searchField.setPreferredSize(new Dimension(200, 35));
        
        JButton btnSearch = makeGoldBtn("Tìm Kiếm");
        btnSearch.addActionListener(e -> {
            String kw = searchField.getText().toLowerCase();
            List<KhachHang> res = new ArrayList<>();
            for(KhachHang k : customers) {
                // Đã cập nhật thành getSdt() theo Model mới
                if(k.getTenKH().toLowerCase().contains(kw) || k.getSdt().contains(kw)) res.add(k);
            }
            refreshTable(res);
        });

        JButton btnAdd = makeGoldBtn("+ Thêm khách hàng");
        btnAdd.addActionListener(e -> openDialog(null));

        right.add(searchField); right.add(btnSearch); right.add(btnAdd);
        header.add(title, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JScrollPane buildTablePanel() {
        // Đã cập nhật lại các Cột theo Model mới
        String[] cols = {"Mã KH", "Tên khách hàng", "Số điện thoại", "Email", "Điểm", "Tên ĐN", "Thao tác"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 6; } // Cột thao tác dời sang vị trí số 6
        };
        table = new JTable(tableModel);
        table.setRowHeight(60);
        
        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(HEADER_BG);
        th.setForeground(BRAND_GOLD);
        th.setPreferredSize(new Dimension(0, 44));

        table.getColumnModel().getColumn(6).setPreferredWidth(160);
        table.getColumnModel().getColumn(6).setCellRenderer(new ActionRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ActionEditor());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 222)));
        return scroll;
    }

    public void refreshTable(List<KhachHang> data) {
        tableModel.setRowCount(0);
        for (KhachHang k : data) {
            // Đổ dữ liệu mới vào bảng (thêm Email, DiemTichLuy, TenDangNhap)
            tableModel.addRow(new Object[]{
                k.getMaKH(), 
                k.getTenKH(), 
                k.getSdt(), 
                k.getEmail(), 
                k.getDiemTichLuy(), 
                k.getTenDangNhap(), 
                ""
            });
        }
    }

    private String autoMaKH() {
        int max = 0;
        for(KhachHang k : customers) {
            try { 
                String numPart = k.getMaKH().replaceAll("[^0-9]", "");
                max = Math.max(max, Integer.parseInt(numPart)); 
            } catch(Exception e){}
        }
        return String.format("KH%03d", max + 1);
    }

    private void openDialog(KhachHang target) {
        boolean isEdit = (target != null);
        JDialog d = new JDialog((Frame)null, isEdit ? "SỬA THÔNG TIN" : "THÊM KHÁCH HÀNG", true);
        d.setSize(450, 450); // Tăng chiều cao để chứa thêm ô nhập liệu
        d.setLayout(new BorderLayout());
        d.setLocationRelativeTo(this);

        // Chuyển sang 6 hàng 2 cột
        JPanel pForm = new JPanel(new GridLayout(6, 2, 10, 20));
        pForm.setBorder(new EmptyBorder(25, 25, 25, 25));

        JTextField fMa = new JTextField(isEdit ? target.getMaKH() : autoMaKH());
        fMa.setEditable(false);
        fMa.setBackground(new Color(235, 235, 235));
        
        JTextField fTen = new JTextField(isEdit ? target.getTenKH() : "");
        JTextField fSDT = new JTextField(isEdit ? target.getSdt() : "");
        JTextField fEmail = new JTextField(isEdit ? target.getEmail() : "");
        // Điểm tích lũy là int nên cần bọc lại bằng String, nếu mới thì mặc định là 0
        JTextField fDiem = new JTextField(isEdit ? String.valueOf(target.getDiemTichLuy()) : "0"); 
        JTextField fTenDN = new JTextField(isEdit ? target.getTenDangNhap() : "");

        pForm.add(new JLabel("Mã KH:")); pForm.add(fMa);
        pForm.add(new JLabel("Họ Tên:")); pForm.add(fTen);
        pForm.add(new JLabel("Số ĐT:")); pForm.add(fSDT);
        pForm.add(new JLabel("Email:")); pForm.add(fEmail);
        pForm.add(new JLabel("Điểm tích lũy:")); pForm.add(fDiem);
        pForm.add(new JLabel("Tên đăng nhập:")); pForm.add(fTenDN);

        JButton btnSave = new JButton(isEdit ? "CẬP NHẬT" : "THÊM MỚI");
        btnSave.setBackground(BRAND_GOLD);
        btnSave.setPreferredSize(new Dimension(0, 45));
        btnSave.addActionListener(e -> {
            if(fTen.getText().isEmpty() || fSDT.getText().isEmpty()) {
                JOptionPane.showMessageDialog(d, "Vui lòng nhập Tên và SĐT!");
                return;
            }
            
            // Xử lý riêng lỗi người dùng nhập sai kiểu số ở ô Điểm tích lũy
            int diemTichLuy = 0;
            try {
                diemTichLuy = Integer.parseInt(fDiem.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(d, "Điểm tích lũy phải là số nguyên!");
                return;
            }

            if(isEdit) {
                target.setTenKH(fTen.getText()); 
                target.setSdt(fSDT.getText()); 
                target.setEmail(fEmail.getText());
                target.setDiemTichLuy(diemTichLuy);
                target.setTenDangNhap(fTenDN.getText());
                
                dao.suaKhachHang(target);
            } else {
                KhachHang newK = new KhachHang(
                    fMa.getText(), 
                    fTen.getText(), 
                    fSDT.getText(), 
                    diemTichLuy, 
                    fEmail.getText(), 
                    fTenDN.getText()
                );
                dao.themKhachHang(newK);
            }
            loadData(); 
            d.dispose();
        });
        
        d.add(pForm, BorderLayout.CENTER);
        d.add(btnSave, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    class ActionRenderer implements TableCellRenderer {
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 12));
            p.setBackground(r % 2 == 0 ? Color.WHITE : new Color(250, 252, 251));
            p.add(makeSmallBtn("Sửa", EDIT_BLUE)); p.add(makeSmallBtn("Xóa", DELETE_RED));
            return p;
        }
    }

    class ActionEditor extends AbstractCellEditor implements TableCellEditor {
        private int row;
        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            this.row = r;
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 12));
            p.setBackground(Color.WHITE);
            JButton btnE = makeSmallBtn("✏", EDIT_BLUE);
            JButton btnD = makeSmallBtn("🗑", DELETE_RED);
            btnE.addActionListener(e -> { fireEditingStopped(); openDialog(customers.get(row)); });
            btnD.addActionListener(e -> {
                fireEditingStopped();
                int conf = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa khách hàng " + customers.get(row).getTenKH() + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if(conf == JOptionPane.YES_OPTION) { 
                    dao.xoaKhachHang(customers.get(row).getMaKH()); 
                    loadData(); 
                    JOptionPane.showMessageDialog(null, "Đã xóa thành công!");
                }
            });
            p.add(btnE); p.add(btnD);
            return p;
        }
        public Object getCellEditorValue() { return ""; }
    }

    private JButton makeGoldBtn(String txt) {
        JButton b = new JButton(txt); b.setBackground(BRAND_GOLD); b.setOpaque(true); b.setBorderPainted(false); 
        b.setCursor(new Cursor(Cursor.HAND_CURSOR)); return b;
    }
    
    private JButton makeSmallBtn(String txt, Color bg) {
        JButton b = new JButton(txt); b.setBackground(bg); b.setForeground(Color.WHITE); b.setOpaque(true); b.setBorderPainted(false); 
        b.setCursor(new Cursor(Cursor.HAND_CURSOR)); return b;
    }
}