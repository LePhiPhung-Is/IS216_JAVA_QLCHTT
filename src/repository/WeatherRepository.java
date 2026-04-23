package src.repository;

import src.api.WeatherApiClient;
import src.model.WeatherEntity;

public class WeatherRepository {
    private WeatherApiClient apiClient;

    public WeatherRepository() {
        this.apiClient = new WeatherApiClient();
    }

    public WeatherEntity getWeatherByCityUrl(String encodedCity) throws Exception {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=" + apiClient.getApiKey() + "&units=metric&lang=vi";
        
        // Gọi API lấy JSON
        String json = apiClient.fetchRawJson(url);

        // Bóc tách JSON thành Entity
        double temp = Double.parseDouble(json.split("\"temp\":")[1].split(",")[0]);
        String desc = json.split("\"description\":\"")[1].split("\"")[0];
        String main = json.split("\"main\":\"")[1].split("\"")[0];
        
        String rawName = json.split("\"name\":\"")[1].split("\"")[0];
        String finalName = unescapeUnicode(rawName);

        return new WeatherEntity(finalName, temp, desc, main);
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
}
