package src.controller;
import src.view.WeatherFrame;
import javax.swing.SwingUtilities;

import src.dto.WeatherDTO;
import src.service.WeatherService;

public class WeatherController {
    private WeatherFrame view;
    private WeatherService service;

    public WeatherController(WeatherFrame view, WeatherService service) {
        this.view = view;
        this.service = service;

        this.view.addSearchListener(e -> handleSearch());
    }

    private void handleSearch() {
        String city = view.getCityInput();
        if (city.isEmpty()) {
            view.showError("Vui lòng nhập tên khu vực!");
            return;
        }

        view.setLoading(true);

        new Thread(() -> {
            try {
                WeatherDTO result = service.processWeatherRequest(city);
                SwingUtilities.invokeLater(() -> {
                    view.updateData(result);
                    view.setLoading(false);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    view.showError(ex.getMessage());
                    view.setLoading(false);
                });
            }
        }).start();
    }
}
