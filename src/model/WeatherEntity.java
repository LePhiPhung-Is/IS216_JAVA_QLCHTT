package src.model;

public class WeatherEntity {
    private String name;
    private double temperature;
    private String description;
    private String mainCondition;

    public WeatherEntity(String name, double temperature, String description, String mainCondition) {
        this.name = name;
        this.temperature = temperature;
        this.description = description;
        this.mainCondition = mainCondition;
    }

    public String getName() { return name; }
    public double getTemperature() { return temperature; }
    public String getDescription() { return description; }
    public String getMainCondition() { return mainCondition; }
}
