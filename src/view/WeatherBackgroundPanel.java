package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

public class WeatherBackgroundPanel extends JPanel implements ActionListener {
    private String weatherType = "Clear";
    private List<Cloud> clouds = new ArrayList<>();
    private List<RainDrop> rainDrops = new ArrayList<>();
    private Random random = new Random();

    public WeatherBackgroundPanel() {
        for (int i = 0; i < 5; i++) {
            clouds.add(new Cloud(random.nextInt(500), random.nextInt(80), 0.5 + random.nextDouble()));
        }
        new Timer(30, this).start();
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

        Color c1 = weatherType.equals("Clear") ? new Color(135, 206, 250) : new Color(112, 128, 144);
        Color c2 = weatherType.equals("Clear") ? new Color(25, 118, 210) : new Color(47, 79, 79);
        g2d.setPaint(new GradientPaint(0, 0, c1, 0, getHeight(), c2));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        if (weatherType.equals("Clear")) {
            g2d.setColor(Color.YELLOW);
            g2d.fillOval(40, 40, 50, 50);
        }

        g2d.setColor(weatherType.equals("Clear") ? Color.WHITE : new Color(200, 200, 200, 180));
        for (Cloud c : clouds) c.paint(g2d);

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

    // Các lớp hỗ trợ bên trong (Inner Classes)
    static class Cloud {
        double x, y, speed, scale;
        public Cloud(double x, double y, double s) { 
            this.x = x; this.y = y; this.speed = s; 
            this.scale = 0.6 + new Random().nextDouble() * 0.4; 
        }
        public void move(int w) { 
            x += speed; 
            if (x > w + 50) x = -100; 
        }
        public void paint(Graphics2D g) {
            int cx = (int)x, cy = (int)y, cw = (int)(60 * scale);
            g.fillOval(cx, cy, cw, (int)(40 * scale));
            g.fillOval(cx + 20, cy - 15, cw, (int)(50 * scale));
        }
    }

    static class RainDrop {
        int x, y, speed;
        public RainDrop(int x, int y) { 
            this.x = x; this.y = y; 
            this.speed = 8 + new Random().nextInt(7); 
        }
        public void move(int w, int h) { 
            y += speed; x -= 2; 
            if (y > h || x < -10) { 
                y = -20; x = new Random().nextInt(Math.max(w, 800) + 200); 
            } 
        }
        public void paint(Graphics2D g) { g.drawLine(x, y, x - 1, y + 8); }
    }
}
    
