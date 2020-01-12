package microLab.services.model;

import java.util.List;

public class TeamDto {
    private String name;
    private List<SensorDto> sensors;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SensorDto> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorDto> sensors) {
        this.sensors = sensors;
    }
}
