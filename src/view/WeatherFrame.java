package src.view;
import src.dto.WeatherDTO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;




public class WeatherFrame extends JFrame {
    private JTextField txtCity;
    private JButton btnSearch;
    private JLabel lblCity, lblTemp, lblDesc, lblTime;
    private WeatherBackgroundPanel backgroundPanel;

    public WeatherFrame() {
        setTitle("Weather App");
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        backgroundPanel = new WeatherBackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        txtCity = new JTextField(15);
        btnSearch = new JButton("Tìm Kiếm");
        JLabel lblInput = new JLabel("Nhập Tỉnh/Thành: ");
        lblInput.setForeground(Color.BLACK);
        
        topPanel.add(lblInput);
        topPanel.add(txtCity);
        topPanel.add(btnSearch);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 10, 10));

        Font fMain = new Font("Arial", Font.BOLD, 18);
        lblCity = createLabel(" Khu vực: ---", fMain);
        lblTemp = createLabel(" Nhiệt độ: ---", fMain);
        lblDesc = createLabel(" Tình trạng: ---", fMain);
        lblTime = createLabel(" Cập nhật: ---", new Font("Arial", Font.ITALIC, 14));

        centerPanel.add(lblCity); centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(lblTemp); centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(lblDesc); centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(lblTime);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JLabel createLabel(String text, Font font) {
        JLabel l = new JLabel(text); l.setFont(font); l.setForeground(Color.WHITE); return l;
    }

    public String getCityInput() { return txtCity.getText().trim(); }
    public void addSearchListener(ActionListener l) { btnSearch.addActionListener(l); txtCity.addActionListener(l); }

    public void updateData(WeatherDTO dto) {
        lblCity.setText(" Khu vực: " + dto.getDisplayCity());
        lblTemp.setText(" Nhiệt độ: " + dto.getDisplayTemp());
        lblDesc.setText(" Tình trạng: " + dto.getDisplayDesc());
        lblTime.setText(" Cập nhật: " + dto.getUpdateTime());
        backgroundPanel.setWeather(dto.getMainCondition());
    }

    public void setLoading(boolean isLoading) {
        btnSearch.setEnabled(!isLoading);
        btnSearch.setText(isLoading ? "Đang tìm..." : "Tìm Kiếm");
    }

    public void showError(String msg) { JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE); }
}

