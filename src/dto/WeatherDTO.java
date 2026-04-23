package src.dto;

public class WeatherDTO {
    private String displayCity;
    private String displayTemp;
    private String displayDesc;
    private String mainCondition;
    private String updateTime;

    public WeatherDTO(String displayCity, String displayTemp, String displayDesc, String mainCondition, String updateTime) {
        this.displayCity = displayCity;
        this.displayTemp = displayTemp;
        this.displayDesc = displayDesc;
        this.mainCondition = mainCondition;
        this.updateTime = updateTime;
    }

    public String getDisplayCity() { return displayCity; }
    public String getDisplayTemp() { return displayTemp; }
    public String getDisplayDesc() { return displayDesc; }
    public String getMainCondition() { return mainCondition; }
    public String getUpdateTime() { return updateTime; }
}
