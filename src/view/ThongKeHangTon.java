package src.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// Thư viện chọn ngày tháng xổ xuống (Nhớ add jcalendar-1.4.jar)
import com.toedter.calendar.JDateChooser;

public class ThongKeHangTon extends JPanel {
    private JComboBox<String> cbDanhMuc;
    private JDateChooser dcTuNgay, dcDenNgay;
    private JButton btnThongKe, btnXuatBaoCao;
    private JTable tableKetQua;
    private DefaultTableModel model;
    private JLabel lblTongSoLuong;

    // TODO: CHIẾN NHỚ KIỂM TRA LẠI MẬT KHẨU DATABASE Ở ĐÂY NHÉ!
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
        // --- THANH ĐIỀU KHIỂN ---
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlHeader.setOpaque(false);

        pnlHeader.add(new JLabel("Danh mục:"));
        cbDanhMuc = new JComboBox<>(new String[]{"Tất cả", "Áo", "Quần", "Váy", "Giày", "Mũ", "Túi"});
        pnlHeader.add(cbDanhMuc);

        pnlHeader.add(new JLabel("Từ:"));
        dcTuNgay = new JDateChooser();
        dcTuNgay.setDateFormatString("dd/MM/yyyy");
        dcTuNgay.setPreferredSize(new Dimension(130, 30));
        setInitialDate(dcTuNgay, true);
        pnlHeader.add(dcTuNgay);

        pnlHeader.add(new JLabel("Đến:"));
        dcDenNgay = new JDateChooser();
        dcDenNgay.setDateFormatString("dd/MM/yyyy");
        dcDenNgay.setPreferredSize(new Dimension(130, 30));
        setInitialDate(dcDenNgay, false);
        pnlHeader.add(dcDenNgay);

        // --- NÚT THỐNG KÊ (ĐÃ FIX LỖI ẨN CHỮ) ---
        btnThongKe = new JButton("Thống kê");
        btnThongKe.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnThongKe.setBackground(Color.BLACK);
        btnThongKe.setForeground(Color.WHITE);
        btnThongKe.setPreferredSize(new Dimension(100, 30));
        // 3 dòng code ép Windows hiển thị đúng màu nút
        btnThongKe.setContentAreaFilled(true); 
        btnThongKe.setOpaque(true);
        btnThongKe.setBorderPainted(false);
        pnlHeader.add(btnThongKe);

        // --- NÚT XUẤT BÁO CÁO (ĐÃ FIX LỖI ẨN CHỮ) ---
        btnXuatBaoCao = new JButton("Xuất báo cáo");
        btnXuatBaoCao.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXuatBaoCao.setBackground(new Color(212, 175, 55));
        btnXuatBaoCao.setForeground(Color.BLACK); 
        btnXuatBaoCao.setPreferredSize(new Dimension(120, 30));
        // 3 dòng code ép Windows hiển thị đúng màu nút
        btnXuatBaoCao.setContentAreaFilled(true);
        btnXuatBaoCao.setOpaque(true);
        btnXuatBaoCao.setBorderPainted(false);
        pnlHeader.add(btnXuatBaoCao);

        add(pnlHeader, BorderLayout.NORTH);

        // --- BẢNG HIỂN THỊ ---
        String[] columns = {"STT", "Mã SP", "Tên Sản Phẩm", "Ngày Nhập", "Số Lượng Tồn"};
        model = new DefaultTableModel(columns, 0);
        tableKetQua = new JTable(model);
        tableKetQua.setRowHeight(30);
        add(new JScrollPane(tableKetQua), BorderLayout.CENTER);

        lblTongSoLuong = new JLabel("Tổng số lượng tồn: 0");
        lblTongSoLuong.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(lblTongSoLuong, BorderLayout.SOUTH);

        // --- SỰ KIỆN ---
        btnThongKe.addActionListener(e -> queryData(dcTuNgay.getDate(), dcDenNgay.getDate()));
        btnXuatBaoCao.addActionListener(e -> moCuaSoXemTruoc());
    }

    private void setInitialDate(JDateChooser dateChooser, boolean isStartOfMonth) {
        Calendar cal = Calendar.getInstance();
        if (isStartOfMonth) {
            cal.set(Calendar.DAY_OF_MONTH, 1);
        }
        dateChooser.setDate(cal.getTime());
    }

    private void queryData(Date tu, Date den) {
        if (tu == null || den == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ ngày tháng!");
            return;
        }
        
        // MẸO XỬ LÝ NGÀY: Cộng thêm 1 ngày vào biến 'den' để lấy trọn vẹn 24h của ngày kết thúc
        Calendar cal = Calendar.getInstance();
        cal.setTime(den);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date denPlusOne = cal.getTime();

        model.setRowCount(0);
        int tong = 0;
        int stt = 1;
        
        // Dùng >= và < thay vì BETWEEN để loại bỏ hoàn toàn lỗi giờ/phút/giây
        String sql = "SELECT s.MASP, s.TENSP, p.NgayNhap, s.SoLuongTon " +
                     "FROM SANPHAM s JOIN CHITIET_PHIEUNHAP ct ON s.MASP = ct.MaSP " +
                     "JOIN PHIEUNHAP p ON ct.MaPN = p.MaPN " +
                     "JOIN DANHMUC d ON s.MaDM = d.MaDM " +
                     "WHERE p.NgayNhap >= ? AND p.NgayNhap < ? ";
                     
        if (!cbDanhMuc.getSelectedItem().equals("Tất cả")) {
            sql += "AND d.TenDM = ?";
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setDate(1, new java.sql.Date(tu.getTime()));
            pstmt.setDate(2, new java.sql.Date(denPlusOne.getTime()));
            if (!cbDanhMuc.getSelectedItem().equals("Tất cả")) {
                pstmt.setString(3, (String)cbDanhMuc.getSelectedItem());
            }
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int sl = rs.getInt("SoLuongTon");
                tong += sl;
                model.addRow(new Object[]{stt++, rs.getString("MASP"), rs.getNString("TENSP"), rs.getDate("NgayNhap"), sl});
            }
            lblTongSoLuong.setText("Tổng số lượng tồn: " + tong);
            
            if(tong == 0) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu trong khoảng thời gian này!");
            }
            
        } catch (SQLException ex) { 
            ex.printStackTrace(); 
            // In thẳng lỗi ra màn hình nếu sai pass hoặc sập DB
            JOptionPane.showMessageDialog(this, "Lỗi Database: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- CỬA SỔ XEM TRƯỚC VÀ XUẤT FILE ---
    // --- CỬA SỔ XEM TRƯỚC VÀ XUẤT FILE (BẢN NÂNG CẤP CHUYÊN NGHIỆP) ---
    private void moCuaSoXemTruoc() {
        JDialog dialog = new JDialog((Frame) null, "Xem trước báo cáo", true);
        dialog.setSize(900, 650); // Mở rộng kích thước cửa sổ
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(15, 15));

        JPanel pnlDate = new JPanel(new FlowLayout());
        
        JDateChooser dcPreviewTu = new JDateChooser();
        dcPreviewTu.setDateFormatString("dd/MM/yyyy");
        dcPreviewTu.setPreferredSize(new Dimension(130, 30));
        setInitialDate(dcPreviewTu, true);

        JDateChooser dcPreviewDen = new JDateChooser();
        dcPreviewDen.setDateFormatString("dd/MM/yyyy");
        dcPreviewDen.setPreferredSize(new Dimension(130, 30));
        setInitialDate(dcPreviewDen, false);

        pnlDate.add(new JLabel("Từ:")); pnlDate.add(dcPreviewTu);
        pnlDate.add(new JLabel("Đến:")); pnlDate.add(dcPreviewDen);

        // Giao diện bảng HTML
        JEditorPane txtPreview = new JEditorPane();
        txtPreview.setContentType("text/html");
        txtPreview.setEditable(false);
        txtPreview.setText("<html><body style='font-family: Arial; padding: 30px; text-align: center; color: #888;'>"
                + "<h2>Nhấn 'Cập nhật Preview' để xem bảng báo cáo...</h2></body></html>");

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCapNhat = new JButton("Cập nhật Preview");
        JButton btnLuu = new JButton("Xác nhận Lưu File");
        pnlBtn.add(btnCapNhat); pnlBtn.add(btnLuu);

        btnCapNhat.addActionListener(e -> {
            if(dcPreviewTu.getDate() != null && dcPreviewDen.getDate() != null) {
                txtPreview.setText(generatePreviewContent(dcPreviewTu.getDate(), dcPreviewDen.getDate()));
                txtPreview.setCaretPosition(0); // Cuộn lên đầu trang
            } else {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ngày!");
            }
        });

        btnLuu.addActionListener(e -> {
            xuatFile(generateCSVContent(dcPreviewTu.getDate(), dcPreviewDen.getDate()));
            dialog.dispose();
        });

        dialog.add(pnlDate, BorderLayout.NORTH);
        dialog.add(new JScrollPane(txtPreview), BorderLayout.CENTER);
        dialog.add(pnlBtn, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // TẠO GIAO DIỆN BẢNG HTML CHO CỬA SỔ PREVIEW
    private String generatePreviewContent(Date tu, Date den) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        
        sb.append("<html><body style='font-family: Arial, sans-serif; padding: 20px;'>");
        
        // Header
        sb.append("<div style='text-align: center;'>");
        sb.append("<h2 style='color: #D4AF37; margin-bottom: 5px;'>CÔNG TY TNHH BEAUTY SHOP</h2>");
        sb.append("<h3 style='margin-top: 0; color: #333;'>BÁO CÁO THỐNG KÊ HÀNG TỒN KHO</h3>");
        sb.append("<p style='color: #555;'><i>Khoảng thời gian: ").append(sdf.format(tu)).append(" - ").append(sdf.format(den)).append("</i></p>");
        sb.append("</div>");
        
        sb.append("<hr style='border: 1px solid #ccc; margin-bottom: 20px;'/>");
        
        // Kẻ bảng (Table)
        sb.append("<table width='100%' border='1' cellspacing='0' cellpadding='8' style='border-collapse: collapse; text-align: left;'>");
        sb.append("<tr style='background-color: #f2f2f2; color: #333;'>");
        sb.append("<th>STT</th><th>Mã SP</th><th>Tên Sản Phẩm</th><th>Số Lượng Tồn</th>");
        sb.append("</tr>");
        
        for (int i = 0; i < model.getRowCount(); i++) {
            sb.append("<tr>");
            sb.append("<td>").append(model.getValueAt(i, 0)).append("</td>");
            sb.append("<td>").append(model.getValueAt(i, 1)).append("</td>");
            sb.append("<td>").append(model.getValueAt(i, 2)).append("</td>");
            sb.append("<td>").append(model.getValueAt(i, 4)).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        
        // Phần chữ ký
        sb.append("<div style='text-align: right; margin-top: 40px; padding-right: 50px;'>");
        sb.append("<p><i>Ngày .... tháng .... năm 2026</i></p>");
        sb.append("<p><b>Người lập biểu</b></p>");
        sb.append("<br><br><br>"); 
        sb.append("<p>Đoàn Xuân Chiến</p>");
        sb.append("</div>");
        
        sb.append("</body></html>");
        return sb.toString();
    }

    // TẠO DỮ LIỆU ĐỂ LƯU THÀNH FILE EXCEL (.CSV)
    private String generateCSVContent(Date tu, Date den) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append("CÔNG TY TNHH BEAUTY SHOP\n");
        sb.append("BÁO CÁO HÀNG TỒN KHO\n");
        sb.append("Khoảng thời gian: ").append(sdf.format(tu)).append(" - ").append(sdf.format(den)).append("\n\n");
        sb.append("STT,Mã SP,Tên Sản Phẩm,Số Lượng Tồn\n");
        
        for (int i = 0; i < model.getRowCount(); i++) {
            sb.append(model.getValueAt(i, 0)).append(",")
              .append(model.getValueAt(i, 1)).append(",")
              .append(model.getValueAt(i, 2)).append(",")
              .append(model.getValueAt(i, 4)).append("\n");
        }
        sb.append("\nNgười lập biểu: Đoàn Xuân Chiến\n");
        return sb.toString();
    }

    // HÀM XUẤT FILE XUỐNG MÁY TÍNH
    private void xuatFile(String content) {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fc.getSelectedFile().getAbsolutePath() + ".csv"), StandardCharsets.UTF_8))) {
                bw.write('\ufeff'); // Thêm BOM để Excel không bị lỗi font tiếng Việt
                bw.write(content);
                JOptionPane.showMessageDialog(this, "Đã lưu báo cáo thành công!");
            } catch (Exception ex) { 
                ex.printStackTrace(); 
            }
        }
    }
}