package src.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// --- THƯ VIỆN LỊCH VÀ BIỂU ĐỒ ---
import com.toedter.calendar.JDateChooser;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

// --- THƯ VIỆN ITEXT XUẤT PDF ---
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ThongKeDoanhThu extends JPanel {
    private JDateChooser dcTuNgay, dcDenNgay;
    private JButton btnThongKe, btnXuatPDF;
    private JTable tableOrders;
    private DefaultTableModel model;
    private JLabel lblTongTienHienThi;
    private ChartPanel pnlBarChart, pnlPieChart;

    private final Color BRAND_GOLD = new Color(212, 175, 55);
    private final String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private final String DB_USER = "FASHION_ADMIN";
    private final String DB_PASS = "123456"; 

    public ThongKeDoanhThu() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(new Color(244, 247, 246));
        initComponents();
    }

    private void initComponents() {
        // --- HEADER ---
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlHeader.setOpaque(false);

        pnlHeader.add(new JLabel("Từ ngày:"));
        dcTuNgay = new JDateChooser();
        dcTuNgay.setDateFormatString("dd/MM/yyyy");
        dcTuNgay.setPreferredSize(new Dimension(140, 30));
        setInitialDate(dcTuNgay, true);
        pnlHeader.add(dcTuNgay);

        pnlHeader.add(new JLabel("Đến ngày:"));
        dcDenNgay = new JDateChooser();
        dcDenNgay.setDateFormatString("dd/MM/yyyy");
        dcDenNgay.setPreferredSize(new Dimension(140, 30));
        setInitialDate(dcDenNgay, false);
        pnlHeader.add(dcDenNgay);

        btnThongKe = new JButton("Thống kê");
        btnThongKe.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnThongKe.setBackground(Color.BLACK);
        btnThongKe.setForeground(Color.WHITE);
        btnThongKe.setPreferredSize(new Dimension(120, 30));
        btnThongKe.setOpaque(true);
        btnThongKe.setContentAreaFilled(true);
        btnThongKe.setBorderPainted(false);
        pnlHeader.add(btnThongKe);

        btnXuatPDF = new JButton("Xuất PDF");
        btnXuatPDF.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXuatPDF.setBackground(BRAND_GOLD);
        btnXuatPDF.setForeground(Color.BLACK);
        btnXuatPDF.setPreferredSize(new Dimension(120, 30));
        btnXuatPDF.setOpaque(true);
        btnXuatPDF.setContentAreaFilled(true);
        btnXuatPDF.setBorderPainted(false);
        pnlHeader.add(btnXuatPDF);

        add(pnlHeader, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel pnlTable = new JPanel(new BorderLayout());
        String[] columns = {"STT", "Mã Đơn Hàng", "Ngày Đặt", "Mã Nhân Viên", "Tổng tiền (VNĐ)"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tableOrders = new JTable(model);
        tableOrders.setRowHeight(30);
        pnlTable.add(new JScrollPane(tableOrders), BorderLayout.CENTER);

        tableOrders.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = tableOrders.getSelectedRow();
                if (row != -1) hienThiSanPhamCuaDonHang(tableOrders.getValueAt(row, 1).toString());
            }
        });

        lblTongTienHienThi = new JLabel("Tổng tiền: 0 VNĐ");
        lblTongTienHienThi.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTongTienHienThi.setForeground(new Color(178, 34, 34));
        lblTongTienHienThi.setBorder(new EmptyBorder(10, 0, 0, 0));
        pnlTable.add(lblTongTienHienThi, BorderLayout.SOUTH);

        tabbedPane.addTab("📋 Danh Sách Đơn Hàng", pnlTable);

        JPanel pnlCharts = new JPanel(new GridLayout(1, 2, 15, 15));
        pnlBarChart = new ChartPanel(null);
        pnlPieChart = new ChartPanel(null);
        pnlCharts.add(pnlBarChart);
        pnlCharts.add(pnlPieChart);
        tabbedPane.addTab("📊 Biểu Đồ Thống Kê", pnlCharts);

        add(tabbedPane, BorderLayout.CENTER);

        btnThongKe.addActionListener(e -> {
            queryData(dcTuNgay.getDate(), dcDenNgay.getDate());
            veBieuDoTheoNgay(dcTuNgay.getDate(), dcDenNgay.getDate());
        });
        btnXuatPDF.addActionListener(e -> moCuaSoXemTruoc());
    }

    private void hienThiSanPhamCuaDonHang(String maDH) {
        StringBuilder sb = new StringBuilder("Chi tiết sản phẩm đơn hàng: " + maDH + "\n\n");
        String sql = "SELECT s.TenSP, ct.SoLuong, ct.ThanhTien FROM CHITIET_DONHANG ct " +
                     "JOIN SANPHAM s ON ct.MaSP = s.MaSP WHERE ct.MaDH = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maDH);
            ResultSet rs = pstmt.executeQuery();
            DecimalFormat df = new DecimalFormat("#,###");
            while (rs.next()) {
                sb.append("- ").append(rs.getString("TenSP")).append(" (x").append(rs.getInt("SoLuong"))
                  .append("): ").append(df.format(rs.getLong("ThanhTien"))).append(" VNĐ\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Thông tin chi tiết", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void setInitialDate(JDateChooser dateChooser, boolean isStartOfMonth) {
        Calendar cal = Calendar.getInstance();
        if (isStartOfMonth) cal.set(Calendar.DAY_OF_MONTH, 1);
        dateChooser.setDate(cal.getTime());
    }

    private void queryData(Date tu, Date den) {
        if (tu == null || den == null) return;
        Calendar cal = Calendar.getInstance(); cal.setTime(den); cal.add(Calendar.DAY_OF_MONTH, 1);
        Date denPlusOne = cal.getTime();
        model.setRowCount(0); long tong = 0; int stt = 1;
        String sql = "SELECT MaDH, TO_CHAR(NgayDat, 'DD/MM/YYYY HH24:MI') as Ngay, MaNV, TongTien FROM DONHANG " +
                     "WHERE NgayDat >= ? AND NgayDat < ? AND LoaiDon = 'OFFLINE' ORDER BY NgayDat DESC";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, new java.sql.Date(tu.getTime()));
            pstmt.setDate(2, new java.sql.Date(denPlusOne.getTime()));
            ResultSet rs = pstmt.executeQuery();
            DecimalFormat df = new DecimalFormat("#,###");
            while (rs.next()) {
                long val = rs.getLong("TongTien"); tong += val;
                model.addRow(new Object[]{stt++, rs.getString(1), rs.getString(2), rs.getString(3), df.format(val)});
            }
            lblTongTienHienThi.setText("Tổng tiền: " + df.format(tong) + " VNĐ");
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void veBieuDoTheoNgay(Date tu, Date den) {
        if (tu == null || den == null) return;
        Calendar cal = Calendar.getInstance(); cal.setTime(den); cal.add(Calendar.DAY_OF_MONTH, 1);
        Date denPlusOne = cal.getTime();
        DefaultCategoryDataset barDS = new DefaultCategoryDataset();
        DefaultPieDataset<String> pieDS = new DefaultPieDataset<>();

        String sqlBar = "SELECT TO_CHAR(NgayDat, 'DD/MM') as Ngay, SUM(TongTien) as DT FROM DONHANG " +
                        "WHERE NgayDat >= ? AND NgayDat < ? AND LoaiDon = 'OFFLINE' GROUP BY TO_CHAR(NgayDat, 'DD/MM') ORDER BY MIN(NgayDat)";
        String sqlPie = "SELECT d.TenDM, SUM(ct.ThanhTien) as DT FROM DONHANG h JOIN CHITIET_DONHANG ct ON h.MaDH = ct.MaDH " +
                        "JOIN SANPHAM s ON ct.MaSP = s.MaSP JOIN DANHMUC d ON s.MaDM = d.MaDM " +
                        "WHERE h.NgayDat >= ? AND h.NgayDat < ? AND h.LoaiDon = 'OFFLINE' GROUP BY d.TenDM";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            try (PreparedStatement ps = conn.prepareStatement(sqlBar)) {
                ps.setDate(1, new java.sql.Date(tu.getTime())); ps.setDate(2, new java.sql.Date(denPlusOne.getTime()));
                ResultSet rs = ps.executeQuery();
                while (rs.next()) barDS.addValue(rs.getDouble("DT"), "Tổng tiền", rs.getString("Ngay"));
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlPie)) {
                ps.setDate(1, new java.sql.Date(tu.getTime())); ps.setDate(2, new java.sql.Date(denPlusOne.getTime()));
                ResultSet rs = ps.executeQuery();
                while (rs.next()) pieDS.setValue(rs.getString("TenDM"), rs.getDouble("DT"));
            }
            JFreeChart bar = ChartFactory.createBarChart("THỐNG KÊ TỔNG TIỀN THEO NGÀY", "Ngày", "VNĐ", barDS, PlotOrientation.VERTICAL, false, true, false);
            JFreeChart pie = ChartFactory.createPieChart("TỶ TRỌNG (%)", pieDS, true, true, false);
            ((PiePlot) pie.getPlot()).setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}"));
            pnlBarChart.setChart(bar); pnlPieChart.setChart(pie);
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    // --- CỬA SỔ XEM TRƯỚC ---
    private void moCuaSoXemTruoc() {
        JDialog dialog = new JDialog((Frame) null, "Xem trước báo cáo", true);
        dialog.setSize(1000, 700); dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(15, 15));

        // 1. THANH CHỌN NGÀY TRONG DIALOG
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JDateChooser pdfTuNgay = new JDateChooser();
        pdfTuNgay.setDateFormatString("dd/MM/yyyy");
        pdfTuNgay.setDate(dcTuNgay.getDate()); 
        
        JDateChooser pdfDenNgay = new JDateChooser();
        pdfDenNgay.setDateFormatString("dd/MM/yyyy");
        pdfDenNgay.setDate(dcDenNgay.getDate());

        JButton btnXemTruoc = new JButton("Cập nhật xem trước");
        btnXemTruoc.setBackground(new Color(173, 216, 230));
        
        pnlTop.add(new JLabel("Từ ngày:"));
        pnlTop.add(pdfTuNgay);
        pnlTop.add(new JLabel("Đến ngày:"));
        pnlTop.add(pdfDenNgay);
        pnlTop.add(btnXemTruoc);
        dialog.add(pnlTop, BorderLayout.NORTH);

        // 2. NỘI DUNG XEM TRƯỚC
        JEditorPane txtPreview = new JEditorPane(); 
        txtPreview.setContentType("text/html");
        txtPreview.setEditable(false);
        txtPreview.setText(generatePreviewContent(pdfTuNgay.getDate(), pdfDenNgay.getDate()));
        dialog.add(new JScrollPane(txtPreview), BorderLayout.CENTER);

        // 3. NÚT LƯU Ở GÓC DƯỚI PHẢI
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        JButton btnLuu = new JButton("Xác nhận lưu PDF");
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLuu.setBackground(BRAND_GOLD); 
        btnLuu.setForeground(Color.BLACK);
        btnLuu.setPreferredSize(new Dimension(160, 35));
        btnLuu.setOpaque(true);
        btnLuu.setContentAreaFilled(true);
        btnLuu.setBorderPainted(false);
        pnlBtn.add(btnLuu);

        btnXemTruoc.addActionListener(e -> {
            txtPreview.setText(generatePreviewContent(pdfTuNgay.getDate(), pdfDenNgay.getDate()));
            txtPreview.setCaretPosition(0);
        });

        btnLuu.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showSaveDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                thucHienXuatPDF(pdfTuNgay.getDate(), pdfDenNgay.getDate(), fc.getSelectedFile().getAbsolutePath() + ".pdf");
                dialog.dispose();
            }
        });
        
        dialog.add(pnlBtn, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private String generatePreviewContent(Date tu, Date den) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat df = new DecimalFormat("#,###");
        StringBuilder sb = new StringBuilder("<html><body style='font-family: Arial; padding: 20px;'>");
        sb.append("<h2 align='center' style='color:#D4AF37;'>CỬA HÀNG BEAUTY SHOP</h2>");
        sb.append("<h3 align='center'>BÁO CÁO TỔNG TIỀN CHI TIẾT</h3>");
        sb.append("<p align='center'><i>Giai đoạn: ").append(sdf.format(tu)).append(" - ").append(sdf.format(den)).append("</i></p>");
        sb.append("<table border='1' width='100%' style='border-collapse: collapse;'>");
        sb.append("<tr bgcolor='#f2f2f2'><th>STT</th><th>Mã Đơn</th><th>Ngày Đặt</th><th>Tổng tiền</th></tr>");
        
        long tong = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement("SELECT MaDH, TO_CHAR(NgayDat, 'DD/MM/YYYY') as Ngay, TongTien FROM DONHANG WHERE NgayDat >= ? AND NgayDat < ? AND LoaiDon = 'OFFLINE'")) {
            ps.setDate(1, new java.sql.Date(tu.getTime()));
            Calendar cal = Calendar.getInstance(); cal.setTime(den); cal.add(Calendar.DAY_OF_MONTH, 1);
            ps.setDate(2, new java.sql.Date(cal.getTime().getTime()));
            ResultSet rs = ps.executeQuery();
            int i = 1;
            while (rs.next()) {
                long val = rs.getLong("TongTien"); tong += val;
                sb.append("<tr><td>").append(i++).append("</td><td>").append(rs.getString(1)).append("</td><td>").append(rs.getString(2)).append("</td><td align='right'>").append(df.format(val)).append("</td></tr>");
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        
        sb.append("</table><h3 align='right'>Tổng tiền: ").append(df.format(tong)).append(" VNĐ</h3>");
        sb.append("<div align='right'><p>Ngày .... tháng .... năm 2026</p><p><b>Người lập biểu</b></p><p>(Ký và ghi rõ họ tên)</p><br><br><p>Đoàn Xuân Chiến</p></div></body></html>");
        return sb.toString();
    }

    private void thucHienXuatPDF(Date tu, Date den, String path) {
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(path));
            doc.open();
            BaseFont bf = BaseFont.createFont("C:/Windows/Fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            com.itextpdf.text.Font fBold = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font fNormal = new com.itextpdf.text.Font(bf, 10, com.itextpdf.text.Font.NORMAL);
            doc.add(new Paragraph("CỬA HÀNG BEAUTY SHOP", fBold));
            doc.add(new Paragraph("BÁO CÁO TỔNG TIỀN OFFLINE", new com.itextpdf.text.Font(bf, 16, com.itextpdf.text.Font.BOLD, new BaseColor(212, 175, 55))));
            doc.add(new Paragraph(" "));
            PdfPTable t = new PdfPTable(4); t.setWidthPercentage(100);
            String[] h = {"STT", "Mã Đơn", "Ngày Đặt", "Tổng tiền"};
            for (String s : h) { PdfPCell c = new PdfPCell(new Phrase(s, fBold)); c.setBackgroundColor(BaseColor.LIGHT_GRAY); t.addCell(c); }
            
            long tong = 0;
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 PreparedStatement ps = conn.prepareStatement("SELECT MaDH, TO_CHAR(NgayDat, 'DD/MM/YYYY') as Ngay, TongTien FROM DONHANG WHERE NgayDat >= ? AND NgayDat < ? AND LoaiDon = 'OFFLINE'")) {
                ps.setDate(1, new java.sql.Date(tu.getTime()));
                Calendar cal = Calendar.getInstance(); cal.setTime(den); cal.add(Calendar.DAY_OF_MONTH, 1);
                ps.setDate(2, new java.sql.Date(cal.getTime().getTime()));
                ResultSet rs = ps.executeQuery();
                int i = 1;
                while (rs.next()) {
                    long val = rs.getLong("TongTien"); tong += val;
                    t.addCell(new Phrase(String.valueOf(i++), fNormal)); t.addCell(new Phrase(rs.getString(1), fNormal));
                    t.addCell(new Phrase(rs.getString(2), fNormal));
                    PdfPCell cVal = new PdfPCell(new Phrase(new DecimalFormat("#,###").format(val), fNormal));
                    cVal.setHorizontalAlignment(Element.ALIGN_RIGHT); t.addCell(cVal);
                }
            }
            doc.add(t);
            Paragraph pTong = new Paragraph("\nTổng tiền: " + new DecimalFormat("#,###").format(tong) + " VNĐ", fBold); 
            pTong.setAlignment(Element.ALIGN_RIGHT); doc.add(pTong);
            Paragraph pKy = new Paragraph("\nNgày .... tháng .... năm 2026\nNgười lập biểu\n(Ký và ghi rõ họ tên)\n\n\n\nĐoàn Xuân Chiến", fNormal);
            pKy.setAlignment(Element.ALIGN_RIGHT); doc.add(pKy);
            doc.close();
            JOptionPane.showMessageDialog(this, "Đã xuất PDF thành công!");
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}