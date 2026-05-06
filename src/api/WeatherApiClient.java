package src.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class WeatherApiClient {
    private String API_KEY;

    // Constructor: Khi khởi tạo class sẽ tự động đọc file
    public WeatherApiClient() {
        loadApiKey();
    }

    private void loadApiKey() {
        Properties properties = new Properties();
        // Dùng try-with-resources để tự động đóng file sau khi đọc xong
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            API_KEY = properties.getProperty("WEATHER_API_KEY");
        } catch (IOException e) {
            System.err.println("Không tìm thấy file config.properties! Hãy kiểm tra lại.");
            e.printStackTrace();
        }
    }

    public String fetchRawJson(String url) throws Exception {
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new Exception("API Key trống hoặc chưa được cấu hình!");
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        if (response.statusCode() != 200) {
            throw new Exception("Lỗi kết nối hoặc không tìm thấy thành phố: HTTP " + response.statusCode());
        }
        return response.body();
    }

    public String getApiKey() {
        return API_KEY;
    }
}