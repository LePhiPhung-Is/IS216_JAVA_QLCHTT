package src;

import javax.swing.SwingUtilities;

import src.controller.WeatherController;
import src.service.WeatherService;
import src.view.WeatherFrame;

public class Main {
    public static void main(String[] args) {
        /*
         * SwingUtilities.invokeLater giúp đảm bảo giao diện đồ họa (UI) 
         * được khởi tạo và chạy trên một luồng an toàn (Event Dispatch Thread).
         * Đây là tiêu chuẩn bắt buộc khi code giao diện bằng Java Swing.
         */
        SwingUtilities.invokeLater(() -> {
            
            // 1. Khởi tạo tầng Logic (Service)
            // Tầng này sẽ tự động khởi tạo Repository và API ở bên trong nó
            WeatherService service = new WeatherService();
            
            // 2. Khởi tạo tầng Giao diện (View)
            WeatherFrame view = new WeatherFrame();
            
            // 3. Khởi tạo Bộ điều khiển (Controller) 
            // Ta "tiêm" (inject) view và service vào để Controller làm cầu nối giao tiếp
            new WeatherController(view, service);
            
            // 4. Cuối cùng, hiển thị giao diện lên màn hình
            view.setVisible(true);
            
        });
    }
}
