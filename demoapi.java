import java.text.Normalizer;
import java.util.regex.Pattern;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


class WeatherModel {
    private String cityName;
    private String temperature;
    private String description;
    private String mainCondition;
    private String dateTime;

    public WeatherModel(String cityName, String temperature, String description, String mainCondition) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.description = description;
        this.mainCondition = mainCondition;
        this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    public String getCityName() { return cityName; }
    public String getTemperature() { return temperature; }
    public String getDescription() { return description; }
    public String getMainCondition() { return mainCondition; }
    public String getDateTime() { return dateTime; }
}


class WeatherService {
    private final String API_KEY = "hihi"; 

    public WeatherModel fetchWeatherData(String city) throws Exception {
        if (API_KEY.isEmpty()) throw new Exception("API Key trống! Hãy nhập API Key vào code.");

        
        String cityForSearch = removeAccents(city);

        
        String encodedCity = URLEncoder.encode(cityForSearch, StandardCharsets.UTF_8.toString());
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=" + API_KEY + "&units=metric&lang=vi";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        if (response.statusCode() != 200) {
            throw new Exception("Không tìm thấy thành phố hoặc lỗi HTTP: " + response.statusCode());
        }

        return parseJson(response.body());
    }

    private WeatherModel parseJson(String json) {
        String temp = json.split("\"temp\":")[1].split(",")[0];
        String desc = json.split("\"description\":\"")[1].split("\"")[0];
        String main = json.split("\"main\":\"")[1].split("\"")[0];
        
        String tenKhuVuc = json.split("\"name\":\"")[1].split("\"")[0];
        tenKhuVuc = unescapeUnicode(tenKhuVuc);
        
        return new WeatherModel(tenKhuVuc, temp, desc, main);
    }

    private String unescapeUnicode(String st) {
        StringBuilder sb = new StringBuilder(st.length());
        for (int i = 0; i < st.length(); i++) {
            char ch = st.charAt(i);
            if (ch == '\\' && i + 1 < st.length() && st.charAt(i + 1) == 'u') {
                try {
                    String hex = st.substring(i + 2, i + 6);
                    sb.append((char) Integer.parseInt(hex, 16));
                    i += 5;
                } catch (Exception e) {
                    sb.append(ch);
                }
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    
    public static String removeAccents(String s) {
        
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        
        return pattern.matcher(temp).replaceAll("")
                      .replace('đ', 'd')
                      .replace('Đ', 'D');
    }
}

public class demoapi extends JFrame {
    private JTextField txtCity;
    private JButton btnSearch;
    private JLabel lblCity, lblTemp, lblDesc, lblTime;
    private BackgroundPanel backgroundPanel;
    private WeatherService weatherService;

    public demoapi() {
        setTitle("Ứng Dụng Thời Tiết OOP");
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        weatherService = new WeatherService();
        initUI();
    }

    private void initUI() {
        backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
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

        Font fontMain = new Font("Arial", Font.BOLD, 18);
        Font fontSub = new Font("Arial", Font.ITALIC, 14);

        lblCity = createLabel(" Khu vực: ---", fontMain);
        lblTemp = createLabel(" Nhiệt độ: ---", fontMain);
        lblDesc = createLabel(" Tình trạng: ---", fontMain);
        lblTime = createLabel(" Thời gian cập nhật: ---", fontSub);

        centerPanel.add(lblCity);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(lblTemp);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(lblDesc);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(lblTime);

        
        JLabel lblLiveClock = new JLabel();
        lblLiveClock.setForeground(Color.WHITE);
        lblLiveClock.setHorizontalAlignment(SwingConstants.RIGHT);
        lblLiveClock.setBorder(BorderFactory.createEmptyBorder(0,0,10,10));
        
        new Timer(1000, e -> {
            lblLiveClock.setText("Bây giờ: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")));
        }).start();

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(lblLiveClock, BorderLayout.SOUTH);

        
        btnSearch.addActionListener(e -> performSearch());
        
        txtCity.addActionListener(e -> performSearch()); 
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(Color.WHITE);
        return label;
    }

    private void performSearch() {
        String city = txtCity.getText().trim();
        if (city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khu vực cần xem thời tiết!");
            return;
        }

        btnSearch.setEnabled(false);
        btnSearch.setText("Đang tìm...");

        
        new Thread(() -> {
            try {
                WeatherModel data = weatherService.fetchWeatherData(city);
                
                SwingUtilities.invokeLater(() -> {
                    lblCity.setText(" Khu vực: " + data.getCityName());
                    lblTemp.setText(" Nhiệt độ: " + data.getTemperature() + "°C");
                    
                    
                    String moTa = data.getDescription();
                    String moTaDep = moTa.substring(0, 1).toUpperCase() + moTa.substring(1);
                    lblDesc.setText(" Tình trạng: " + moTaDep);
                    
                    lblTime.setText(" Cập nhật lúc: " + data.getDateTime());
                    
                    
                    backgroundPanel.setWeather(data.getMainCondition());
                    btnSearch.setEnabled(true);
                    btnSearch.setText("Tìm Kiếm");
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    btnSearch.setEnabled(true);
                    btnSearch.setText("Tìm Kiếm");
                });
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new demoapi().setVisible(true));
    }

    
    static class BackgroundPanel extends JPanel implements ActionListener {
        private String weatherType = "Clear";
        private List<Cloud> clouds = new ArrayList<>();
        private List<RainDrop> rainDrops = new ArrayList<>();
        private Random random = new Random();

        public BackgroundPanel() {
            
            for (int i = 0; i < 5; i++) {
                clouds.add(new Cloud(random.nextInt(500), random.nextInt(80), 0.5 + random.nextDouble()));
            }
            new Timer(30, this).start(); // 30ms render một lần (~33 FPS)
        }

        public void setWeather(String type) {
            this.weatherType = type;
            rainDrops.clear();
            if (type.contains("Rain") || type.contains("Drizzle") || type.contains("Storm")) {
                for (int i = 0; i < 80; i++) {
                    rainDrops.add(new RainDrop(random.nextInt(800), random.nextInt(400)));
                }
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Bầu trời
            Color c1 = weatherType.equals("Clear") ? new Color(135, 206, 250) : new Color(112, 128, 144);
            Color c2 = weatherType.equals("Clear") ? new Color(25, 118, 210) : new Color(47, 79, 79);
            g2d.setPaint(new GradientPaint(0, 0, c1, 0, getHeight(), c2));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Mặt trời (Chỉ hiện khi trời quang)
            if (weatherType.equals("Clear")) {
                g2d.setColor(Color.YELLOW);
                g2d.fillOval(40, 40, 50, 50);
            }

            // Mây (Đổi sang xám nếu trời mưa)
            g2d.setColor(weatherType.equals("Clear") ? Color.WHITE : new Color(200,200,200,180));
            for (Cloud c : clouds) c.paint(g2d);

            // Mưa
            if (!rainDrops.isEmpty()) {
                g2d.setColor(new Color(173, 216, 230, 180));
                for (RainDrop d : rainDrops) d.paint(g2d);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for (Cloud c : clouds) c.move(getWidth());
            for (RainDrop d : rainDrops) d.move(getWidth(), getHeight());
            repaint();
        }

        // --- ĐỐI TƯỢNG ĐÁM MÂY ---
        static class Cloud {
            double x, y, speed, scale;
            public Cloud(double x, double y, double s) { 
                this.x = x; this.y = y; this.speed = s; 
                this.scale = 0.6 + new Random().nextDouble()*0.4; 
            }
            public void move(int w) { 
                x += speed; 
                if (x > w + 50) x = -100; 
            }
            public void paint(Graphics2D g) {
                int cx = (int)x, cy = (int)y, cw = (int)(60*scale);
                g.fillOval(cx, cy, cw, (int)(40*scale));
                g.fillOval(cx+20, cy-15, cw, (int)(50*scale));
            }
        }

       
        static class RainDrop {
            int x, y, speed;
            public RainDrop(int x, int y) { 
                this.x = x; this.y = y; 
                this.speed = 8 + new Random().nextInt(7); 
            }
            public void move(int w, int h) { 
                y += speed; 
                x -= 2; 
                if (y > h || x < -10) { 
                    y = -20; 
                    x = new Random().nextInt(Math.max(w, 800) + 200); 
                } 
            }
            public void paint(Graphics2D g) { 
                g.drawLine(x, y, x - 1, y + 8); 
            }
        }
    }
}