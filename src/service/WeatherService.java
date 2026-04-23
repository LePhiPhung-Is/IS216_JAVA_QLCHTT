package src.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import src.dto.WeatherDTO;
import src.model.WeatherEntity;
import src.repository.WeatherRepository;

public class WeatherService {
    private WeatherRepository repository;

    public WeatherService() {
        this.repository = new WeatherRepository();
    }

    public WeatherDTO processWeatherRequest(String cityInput) throws Exception {
        // Xử lý đầu vào
        String cleanCity = removeAccents(cityInput);
        String encodedCity = URLEncoder.encode(cleanCity, StandardCharsets.UTF_8.toString());

        // Gọi Repository lấy Entity (Dữ liệu thô)
        WeatherEntity entity = repository.getWeatherByCityUrl(encodedCity);

        // Xử lý nghiệp vụ (Làm đẹp dữ liệu)
        String moTa = entity.getDescription();
        String moTaDep = moTa.substring(0, 1).toUpperCase() + moTa.substring(1);
        
        String nhietDoHienThi = entity.getTemperature() + "°C";
        
        String thoiGian = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        // Trả về DTO cho Controller
        return new WeatherDTO(
            entity.getName(),
            nhietDoHienThi,
            moTaDep,
            entity.getMainCondition(),
            thoiGian
        );
    }

    private String removeAccents(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
    }
}
