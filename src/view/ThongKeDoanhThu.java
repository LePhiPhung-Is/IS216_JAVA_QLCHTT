package src.view;

import src.database.DatabaseConnection;
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

/**
 * Thống kê doanh thu - BEAUTY SHOP
 * Sinh viên thực hiện: ĐOÀN XUÂN CHIẾN - MSSV: 24520217
 */
public class ThongKeDoanhThu extends JPanel {
    private JDateChooser dcTuNgay, dcDenNgay;
    private JComboBox<String> cboTuGio, cboDenGio; 
    private JButton btnThongKe, btnXuatPDF;
    private JTable tableOrders;
    private DefaultTableModel model;
    private JLabel lblTongTienHienThi;
    private ChartPanel pnlBarChart, pnlPieChart;

    // Định nghĩa màu vàng sáng chuẩn cho nút Xuất PDF
    private final Color YELLOW_BTN = new Color(255, 221, 0);
    private final Color BRAND_GOLD = new Color(212, 175, 55);

    public ThongKeDoanhThu() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(new Color(244, 247, 246));
        initComponents();
    }

    private void initComponents() {
        // --- HEADER ---
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlHeader.setOpaque(false);

        pnlHeader.add(new JLabel("Từ ngày:"));
        dcTuNgay = new JDateChooser();
        dcTuNgay.setDateFormatString("dd/MM/yyyy");
        dcTuNgay.setPreferredSize(new Dimension(120, 30));
        setInitialDate(dcTuNgay, true);
        pnlHeader.add(dcTuNgay);

        pnlHeader.add(new JLabel("lúc"));
        cboTuGio = new JComboBox<>();
        for (int i = 0; i < 24; i++) cboTuGio.addItem(i + "h");
        cboTuGio.setSelectedIndex(0); 
        cboTuGio.setPreferredSize(new Dimension(60, 30));
        pnlHeader.add(cboTuGio);

        pnlHeader.add(Box.createHorizontalStrut(10)); 

        pnlHeader.add(new JLabel("Đến ngày:"));
        dcDenNgay = new JDateChooser();
        dcDenNgay.setDateFormatString("dd/MM/yyyy");
        dcDenNgay.setPreferredSize(new Dimension(120, 30));
        setInitialDate(dcDenNgay, false);
        pnlHeader.add(dcDenNgay);

        pnlHeader.add(new JLabel("lúc"));
        cboDenGio = new JComboBox<>();
        for (int i = 0; i < 24; i++) cboDenGio.addItem(i + "h");
        cboDenGio.setSelectedIndex(23); 
        cboDenGio.setPreferredSize(new Dimension(60, 30));
        pnlHeader.add(cboDenGio);

        // Nút Thống Kê - NỀN ĐEN CHỮ TRẮNG
        btnThongKe = new JButton("Thống kê");
        btnThongKe.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnThongKe.setBackground(Color.BLACK);
        btnThongKe.setForeground(Color.WHITE);
        btnThongKe.setPreferredSize(new Dimension(100, 30));
        btnThongKe.setFocusPainted(false);
        btnThongKe.setOpaque(true);
        btnThongKe.setBorderPainted(false);
        btnThongKe.setContentAreaFilled(true);
        pnlHeader.add(btnThongKe);

        // Nút Xuất PDF - NỀN VÀNG CHỮ ĐEN
        btnXuatPDF = new JButton("Xuất PDF");
        btnXuatPDF.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXuatPDF.setBackground(YELLOW_BTN);
        btnXuatPDF.setForeground(Color.BLACK);
        btnXuatPDF.setPreferredSize(new Dimension(100, 30));
        btnXuatPDF.setFocusPainted(false);
        btnXuatPDF.setOpaque(true);
        btnXuatPDF.setBorderPainted(false);
        btnXuatPDF.setContentAreaFilled(true);
        pnlHeader.add(btnXuatPDF);

        add(pnlHeader, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // --- TAB 1: DANH SÁCH ĐƠN HÀNG ---
        JPanel pnlTable = new JPanel(new BorderLayout());
        String[] columns = {"STT", "Mã Đơn Hàng", "Thời Gian Đặt", "Mã Nhân Viên", "Tổng tiền (VNĐ)"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tableOrders = new JTable(model);
        tableOrders.setRowHeight(35);
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

        // --- TAB 2: BIỂU ĐỒ ---
        JPanel pnlCharts = new JPanel(new GridLayout(1, 2, 15, 15));
        pnlBarChart = new ChartPanel(null);
        pnlPieChart = new ChartPanel(null);
        pnlCharts.add(pnlBarChart);
        pnlCharts.add(pnlPieChart);
        tabbedPane.addTab("📊 Biểu Đồ Thống Kê", pnlCharts);

        add(tabbedPane, BorderLayout.CENTER);

        btnThongKe.addActionListener(e -> thucHienThongKe());
        btnXuatPDF.addActionListener(e -> moCuaSoXemTruoc());
    }

    private void hienThiSanPhamCuaDonHang(String maDH) {
        JDialog detailDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết đơn hàng: " + maDH, true);
        detailDialog.setSize(850, 450);
        detailDialog.setLocationRelativeTo(this);
        detailDialog.setLayout(new BorderLayout(15, 15));

        String[] columns = {"STT", "Mã SP", "Tên Sản Phẩm", "Size", "Màu Sắc", "Đơn Giá", "SL", "Thành Tiền"};
        DefaultTableModel detailModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable detailTable = new JTable(detailModel);
        detailTable.setRowHeight(35);
        detailTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        String sql = "SELECT s.MaSP, s.TenSP, s.KichCo, s.MauSac, ct.DonGia, ct.SoLuong, ct.ThanhTien " +
                     "FROM CHITIET_DONHANG ct " +
                     "JOIN SANPHAM s ON ct.MaSP = s.MaSP " +
                     "WHERE ct.MaDH = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maDH);
            ResultSet rs = pstmt.executeQuery();
            DecimalFormat df = new DecimalFormat("#,###");
            int stt = 1;

            while (rs.next()) {
                detailModel.addRow(new Object[]{
                    stt++, rs.getString("MaSP"), rs.getString("TenSP"), rs.getString("KichCo"),
                    rs.getString("MauSac"), df.format(rs.getLong("DonGia")), rs.getInt("SoLuong"), df.format(rs.getLong("ThanhTien"))
                });
            }
        } catch (SQLException ex) { 
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn chi tiết: " + ex.getMessage());
        }

        JPanel pnlContent = new JPanel(new BorderLayout());
        pnlContent.setBorder(new EmptyBorder(15, 15, 15, 15));
        pnlContent.setBackground(Color.WHITE);
        pnlContent.add(new JScrollPane(detailTable), BorderLayout.CENTER);
        detailDialog.add(pnlContent, BorderLayout.CENTER);
        
        JButton btnClose = new JButton("Đóng cửa sổ");
        btnClose.addActionListener(e -> detailDialog.dispose());
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBottom.add(btnClose);
        detailDialog.add(pnlBottom, BorderLayout.SOUTH);

        detailDialog.setVisible(true);
    }

    private void setInitialDate(JDateChooser dateChooser, boolean isStartOfMonth) {
        Calendar cal = Calendar.getInstance();
        if (isStartOfMonth) cal.set(Calendar.DAY_OF_MONTH, 1);
        dateChooser.setDate(cal.getTime());
    }

    private Timestamp getTimestampBoundary(Date date, int hour, boolean isEndOfHour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, isEndOfHour ? 59 : 0);
        cal.set(Calendar.SECOND, isEndOfHour ? 59 : 0);
        return new Timestamp(cal.getTimeInMillis());
    }

    private void thucHienThongKe() {
        if (dcTuNgay.getDate() == null || dcDenNgay.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ ngày bắt đầu và kết thúc!");
            return;
        }

        int tuGio = cboTuGio.getSelectedIndex();
        int denGio = cboDenGio.getSelectedIndex();
        
        Timestamp tsTu = getTimestampBoundary(dcTuNgay.getDate(), tuGio, false);
        Timestamp tsDen = getTimestampBoundary(dcDenNgay.getDate(), denGio, true);

        if (tsTu.after(tsDen)) {
            JOptionPane.showMessageDialog(this, "Thời gian bắt đầu không được lớn hơn thời gian kết thúc!");
            return;
        }

        queryData(tsTu, tsDen);
        veBieuDoTheoNgay(dcTuNgay.getDate(), dcDenNgay.getDate(), tuGio, denGio, tsTu, tsDen);
    }

    private void queryData(Timestamp tsTu, Timestamp tsDen) {
        model.setRowCount(0); long tong = 0; int stt = 1;
        // Đã sửa 'Hoàn thành' thành 'Đã hoàn thành'
        String sql = "SELECT MaDH, TO_CHAR(NgayDat, 'DD/MM/YYYY HH24:MI') as Ngay, MaNV, TongTien FROM DONHANG " +
                     "WHERE NgayDat >= ? AND NgayDat <= ? AND LoaiDon = 'OFFLINE' AND TrangThai = N'Đã hoàn thành' " +
                     "ORDER BY NgayDat DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, tsTu);
            pstmt.setTimestamp(2, tsDen);
            
            ResultSet rs = pstmt.executeQuery();
            DecimalFormat df = new DecimalFormat("#,###");
            while (rs.next()) {
                long val = rs.getLong("TongTien"); tong += val;
                model.addRow(new Object[]{stt++, rs.getString(1), rs.getString(2), rs.getString(3), df.format(val)});
            }
            lblTongTienHienThi.setText("Tổng tiền: " + df.format(tong) + " VNĐ");
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void veBieuDoTheoNgay(Date dateTu, Date dateDen, int tuGio, int denGio, Timestamp tsTu, Timestamp tsDen) {
        DefaultCategoryDataset barDS = new DefaultCategoryDataset();
        DefaultPieDataset<String> pieDS = new DefaultPieDataset<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String callFunc = "{ ? = call fn_DoanhThuTheoGio(?, ?, ?) }";
            try (CallableStatement cs = conn.prepareCall(callFunc)) {
                cs.registerOutParameter(1, Types.NUMERIC);
                cs.setDate(2, new java.sql.Date(dateTu.getTime()));
                cs.setDate(3, new java.sql.Date(dateDen.getTime()));
                
                int loopStart = (dateTu.compareTo(dateDen) == 0) ? tuGio : 0;
                int loopEnd = (dateTu.compareTo(dateDen) == 0) ? denGio : 23;

                for (int i = loopStart; i <= loopEnd; i++) {
                    cs.setInt(4, i);
                    cs.execute();
                    double dt = cs.getDouble(1);
                    if (dt > 0) {
                        barDS.addValue(dt, "Doanh thu", i + "h");
                    }
                }
            }

            // Đã cập nhật JOIN qua CHITIET_DANHMUC và cập nhật trạng thái 'Đã hoàn thành'
            String sqlPie = "SELECT d.TenDM, SUM(ct.ThanhTien) as DT " +
                            "FROM DONHANG h " +
                            "JOIN CHITIET_DONHANG ct ON h.MaDH = ct.MaDH " +
                            "JOIN SANPHAM s ON ct.MaSP = s.MaSP " +
                            "JOIN CHITIET_DANHMUC ctdm ON s.MaSP = ctdm.MaSP " +
                            "JOIN DANHMUC d ON ctdm.MaDM = d.MaDM " +
                            "WHERE h.NgayDat >= ? AND h.NgayDat <= ? AND h.LoaiDon = 'OFFLINE' AND h.TrangThai = N'Đã hoàn thành' " +
                            "GROUP BY d.TenDM";
            
            try (PreparedStatement ps = conn.prepareStatement(sqlPie)) {
                ps.setTimestamp(1, tsTu);
                ps.setTimestamp(2, tsDen);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) pieDS.setValue(rs.getString("TenDM"), rs.getDouble("DT"));
            }
            
            JFreeChart bar = ChartFactory.createBarChart("DOANH THU THEO GIỜ", "Khung Giờ", "VNĐ", barDS, PlotOrientation.VERTICAL, false, true, false);
            JFreeChart pie = ChartFactory.createPieChart("CƠ CẤU DOANH THU", pieDS, true, true, false);
            ((PiePlot) pie.getPlot()).setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}"));
            pnlBarChart.setChart(bar); pnlPieChart.setChart(pie);
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void moCuaSoXemTruoc() {
        if (dcTuNgay.getDate() == null || dcDenNgay.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày tháng!");
            return;
        }

        JDialog dialog = new JDialog((Frame) null, "Xem trước báo cáo", true);
        dialog.setSize(1000, 700); dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(15, 15));

        int tuGio = cboTuGio.getSelectedIndex();
        int denGio = cboDenGio.getSelectedIndex();
        Timestamp tsTu = getTimestampBoundary(dcTuNgay.getDate(), tuGio, false);
        Timestamp tsDen = getTimestampBoundary(dcDenNgay.getDate(), denGio, true);

        JEditorPane txtPreview = new JEditorPane(); 
        txtPreview.setContentType("text/html");
        txtPreview.setEditable(false);
        txtPreview.setText(generatePreviewContent(tsTu, tsDen));
        dialog.add(new JScrollPane(txtPreview), BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        
        JButton btnLuu = new JButton("Xác nhận lưu PDF");
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLuu.setBackground(YELLOW_BTN); 
        btnLuu.setForeground(Color.BLACK);
        btnLuu.setPreferredSize(new Dimension(160, 35));
        btnLuu.setFocusPainted(false);
        btnLuu.setOpaque(true);
        btnLuu.setBorderPainted(false);
        pnlBtn.add(btnLuu);

        btnLuu.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showSaveDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                thucHienXuatPDF(tsTu, tsDen, fc.getSelectedFile().getAbsolutePath() + ".pdf");
                dialog.dispose();
            }
        });
        
        dialog.add(pnlBtn, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private String generatePreviewContent(Timestamp tsTu, Timestamp tsDen) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        DecimalFormat df = new DecimalFormat("#,###");
        StringBuilder sb = new StringBuilder("<html><body style='font-family: Arial; padding: 20px;'>");
        sb.append("<h2 align='center' style='color:#D4AF37;'>CỬA HÀNG BEAUTY SHOP</h2>");
        sb.append("<h3 align='center'>BÁO CÁO TỔNG TIỀN CHI TIẾT</h3>");
        sb.append("<p align='center'><i>Giai đoạn: ").append(sdf.format(tsTu)).append(" - ").append(sdf.format(tsDen)).append("</i></p>");
        sb.append("<table border='1' width='100%' style='border-collapse: collapse;'>");
        sb.append("<tr bgcolor='#f2f2f2'><th>STT</th><th>Mã Đơn</th><th>Thời Gian Đặt</th><th>Tổng tiền</th></tr>");
        
        long tong = 0;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT MaDH, TO_CHAR(NgayDat, 'DD/MM/YYYY HH24:MI') as Ngay, TongTien FROM DONHANG WHERE NgayDat >= ? AND NgayDat <= ? AND LoaiDon = 'OFFLINE' AND TrangThai = N'Đã hoàn thành' ORDER BY NgayDat ASC")) {
            ps.setTimestamp(1, tsTu);
            ps.setTimestamp(2, tsDen);
            ResultSet rs = ps.executeQuery();
            int i = 1;
            while (rs.next()) {
                long val = rs.getLong("TongTien"); tong += val;
                sb.append("<tr><td align='center'>").append(i++).append("</td><td align='center'>").append(rs.getString(1)).append("</td><td align='center'>").append(rs.getString(2)).append("</td><td align='right'>").append(df.format(val)).append("</td></tr>");
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        
        sb.append("</table><h3 align='right'>Tổng tiền: ").append(df.format(tong)).append(" VNĐ</h3>");
        sb.append("<div align='right'><p>Ngày .... tháng .... năm 2026</p><p><b>Người lập biểu</b></p><p>(Ký và ghi rõ họ tên)</p><br><br><p>Đoàn Xuân Chiến</p></div></body></html>");
        return sb.toString();
    }

    private void thucHienXuatPDF(Timestamp tsTu, Timestamp tsDen, String path) {
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
            String[] h = {"STT", "Mã Đơn", "Thời Gian Đặt", "Tổng tiền"};
            for (String s : h) { PdfPCell c = new PdfPCell(new Phrase(s, fBold)); c.setBackgroundColor(BaseColor.LIGHT_GRAY); t.addCell(c); }
            
            long tong = 0;
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT MaDH, TO_CHAR(NgayDat, 'DD/MM/YYYY HH24:MI') as Ngay, TongTien FROM DONHANG WHERE NgayDat >= ? AND NgayDat <= ? AND LoaiDon = 'OFFLINE' AND TrangThai = N'Đã hoàn thành' ORDER BY NgayDat ASC")) {
                ps.setTimestamp(1, tsTu);
                ps.setTimestamp(2, tsDen);
                ResultSet rs = ps.executeQuery();
                int i = 1;
                while (rs.next()) {
                    long val = rs.getLong("TongTien"); tong += val;
                    
                    PdfPCell cStt = new PdfPCell(new Phrase(String.valueOf(i++), fNormal)); cStt.setHorizontalAlignment(Element.ALIGN_CENTER); t.addCell(cStt);
                    PdfPCell cMa = new PdfPCell(new Phrase(rs.getString(1), fNormal)); cMa.setHorizontalAlignment(Element.ALIGN_CENTER); t.addCell(cMa);
                    PdfPCell cTime = new PdfPCell(new Phrase(rs.getString(2), fNormal)); cTime.setHorizontalAlignment(Element.ALIGN_CENTER); t.addCell(cTime);
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