package src.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThongKeHangTon extends JPanel {
    private JComboBox<String> cbDanhMuc;
    private JButton btnThongKe, btnXuatBaoCao;
    private JTable tableKetQua;
    private DefaultTableModel model;
    private JLabel lblTongSoLuong;

    private final Color BRAND_GOLD = new Color(212, 175, 55);
    private final String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private final String DB_USER = "FASHION_ADMIN";
    private final String DB_PASS = "123456"; 

    public ThongKeHangTon() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(new Color(244, 247, 246));
        initComponents();
    }

    private void initComponents() {
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlHeader.setOpaque(false);

        pnlHeader.add(new JLabel("Lọc theo danh mục:"));
        cbDanhMuc = new JComboBox<>(new String[]{"Tất cả", "Áo", "Quần", "Váy", "Giày", "Mũ", "Túi"});
        cbDanhMuc.setPreferredSize(new Dimension(150, 30));
        pnlHeader.add(cbDanhMuc);

        btnThongKe = new JButton("Cập nhật dữ liệu");
        btnThongKe.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnThongKe.setBackground(Color.BLACK);
        btnThongKe.setForeground(Color.WHITE);
        btnThongKe.setPreferredSize(new Dimension(150, 30));
        btnThongKe.setOpaque(true);
        btnThongKe.setContentAreaFilled(true);
        btnThongKe.setBorderPainted(false);
        pnlHeader.add(btnThongKe);

        btnXuatBaoCao = new JButton("Xuất báo cáo");
        btnXuatBaoCao.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXuatBaoCao.setBackground(BRAND_GOLD);
        btnXuatBaoCao.setForeground(Color.BLACK); 
        btnXuatBaoCao.setPreferredSize(new Dimension(120, 30));
        btnXuatBaoCao.setOpaque(true);
        btnXuatBaoCao.setContentAreaFilled(true);
        btnXuatBaoCao.setBorderPainted(false);
        pnlHeader.add(btnXuatBaoCao);

        add(pnlHeader, BorderLayout.NORTH);

        String[] columns = {"STT", "Mã Sản Phẩm", "Tên Sản Phẩm", "Số Lượng Tồn Kho"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tableKetQua = new JTable(model);
        tableKetQua.setRowHeight(30);
        add(new JScrollPane(tableKetQua), BorderLayout.CENTER);

        lblTongSoLuong = new JLabel("Tổng số lượng tồn kho: 0");
        lblTongSoLuong.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTongSoLuong.setForeground(new Color(178, 34, 34));
        add(lblTongSoLuong, BorderLayout.SOUTH);

        btnThongKe.addActionListener(e -> queryStockNow());
        btnXuatBaoCao.addActionListener(e -> moCuaSoXemTruoc());
        
        queryStockNow();
    }

    private void queryStockNow() {
        model.setRowCount(0);
        int tong = 0; int stt = 1;
        String sql = "SELECT s.MASP, s.TENSP, s.SoLuongTon FROM SANPHAM s JOIN DANHMUC d ON s.MaDM = d.MaDM ";
        if (!cbDanhMuc.getSelectedItem().equals("Tất cả")) sql += "WHERE d.TenDM = ?";
        sql += " ORDER BY s.SoLuongTon DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (!cbDanhMuc.getSelectedItem().equals("Tất cả")) pstmt.setString(1, (String)cbDanhMuc.getSelectedItem());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int sl = rs.getInt("SoLuongTon");
                tong += sl;
                model.addRow(new Object[]{stt++, rs.getString("MASP"), rs.getString("TENSP"), sl});
            }
            lblTongSoLuong.setText("Tổng số lượng tồn kho hiện tại: " + tong);
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    // --- CỬA SỔ XEM TRƯỚC VỚI NÚT BẤM MÀU MỚI ---
    private void moCuaSoXemTruoc() {
        JDialog dialog = new JDialog((Frame) null, "Xem trước báo cáo tồn kho", true);
        dialog.setSize(850, 650); 
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(15, 15));

        JEditorPane txtPreview = new JEditorPane();
        txtPreview.setContentType("text/html");
        txtPreview.setEditable(false);
        txtPreview.setText(generatePreviewContent());

        // Panel chứa các nút bấm
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));

        // Nút Cập nhật Preview: Nền Vàng - Chữ Trắng
        JButton btnUpdate = new JButton("Cập nhật Preview");
        btnUpdate.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnUpdate.setBackground(BRAND_GOLD);
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setPreferredSize(new Dimension(150, 35));
        btnUpdate.setOpaque(true);
        btnUpdate.setContentAreaFilled(true);
        btnUpdate.setBorderPainted(false);

        // Nút Xác nhận lưu: Nền Đen - Chữ Trắng
        JButton btnSave = new JButton("Xác nhận Lưu (.csv)");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSave.setBackground(Color.BLACK);
        btnSave.setForeground(Color.WHITE);
        btnSave.setPreferredSize(new Dimension(160, 35));
        btnSave.setOpaque(true);
        btnSave.setContentAreaFilled(true);
        btnSave.setBorderPainted(false);

        pnlBottom.add(btnUpdate);
        pnlBottom.add(btnSave);

        btnUpdate.addActionListener(e -> {
            txtPreview.setText(generatePreviewContent());
            txtPreview.setCaretPosition(0);
        });

        btnSave.addActionListener(e -> {
            xuatFile(generateCSVContent());
            dialog.dispose();
        });

        dialog.add(new JScrollPane(txtPreview), BorderLayout.CENTER);
        dialog.add(pnlBottom, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private String generatePreviewContent() {
        String ngayGioHienTai = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='font-family: Arial; padding: 20px;'>");
        sb.append("<div style='text-align: center;'>");
        sb.append("<h2 style='color: #D4AF37;'>CỬA HÀNG BEAUTY SHOP</h2>");
        sb.append("<h3>BÁO CÁO TỒN KHO THỜI ĐIỂM HIỆN TẠI</h3>");
        sb.append("<p><i>Dữ liệu cập nhật lúc: ").append(ngayGioHienTai).append("</i></p></div><hr/>");
        sb.append("<table width='100%' border='1' cellspacing='0' cellpadding='8' style='border-collapse: collapse;'>");
        sb.append("<tr bgcolor='#f2f2f2'><th>STT</th><th>Mã SP</th><th>Tên Sản Phẩm</th><th>Số Lượng Tồn</th></tr>");
        for (int i = 0; i < model.getRowCount(); i++) {
            sb.append("<tr><td align='center'>").append(model.getValueAt(i, 0)).append("</td><td>")
              .append(model.getValueAt(i, 1)).append("</td><td>").append(model.getValueAt(i, 2))
              .append("</td><td align='right'>").append(model.getValueAt(i, 3)).append("</td></tr>");
        }
        sb.append("</table>");
        sb.append("<div style='text-align: right; margin-top: 30px;'>");
        sb.append("<p><b>").append(lblTongSoLuong.getText()).append("</b></p>");
        sb.append("<p>Người lập biểu: <b>Đoàn Xuân Chiến</b></p></div></body></html>");
        return sb.toString();
    }

    private String generateCSVContent() {
        String ngayGio = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append("CỬA HÀNG BEAUTY SHOP\nBÁO CÁO TỒN KHO HIỆN TẠI\nThời điểm: ").append(ngayGio).append("\n\n");
        sb.append("STT,Mã SP,Tên Sản Phẩm,Số Lượng Tồn\n");
        for (int i = 0; i < model.getRowCount(); i++) {
            sb.append(model.getValueAt(i, 0)).append(",").append(model.getValueAt(i, 1)).append(",")
              .append(model.getValueAt(i, 2)).append(",").append(model.getValueAt(i, 3)).append("\n");
        }
        sb.append("\n").append(lblTongSoLuong.getText()).append("\nNgười lập biểu: Đoàn Xuân Chiến\n");
        return sb.toString();
    }

    private void xuatFile(String content) {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fc.getSelectedFile().getAbsolutePath() + ".csv"), StandardCharsets.UTF_8))) {
                bw.write('\ufeff'); bw.write(content);
                JOptionPane.showMessageDialog(this, "Đã lưu báo cáo thành công!");
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }
}