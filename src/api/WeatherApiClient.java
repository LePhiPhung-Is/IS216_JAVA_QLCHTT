package src.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class WeatherApiClient {
    private final String API_KEY = "hihi"; // Nhập API Key của bạn

    public String fetchRawJson(String url) throws Exception {
        if (API_KEY.isEmpty()) throw new Exception("API Key trống!");

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
