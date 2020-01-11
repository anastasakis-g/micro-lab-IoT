package microLab.entity.custom;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "display")
public class Display {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotNull
    @Column(name = "team_name")
    private String teamName;

    @NotNull
    @Lob
    @Column(name = "sensors")
    private String sensorsAsJSONString;

    @NotNull
    @Column(name = "is_ready")
    private boolean teamIsReady;

    public Display() {
    }

    public Display(String teamName, String sensors, boolean teamIsReady) {
        this.teamName = teamName;
        this.sensorsAsJSONString = sensors;
        this.teamIsReady = teamIsReady;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getSensorsAsJSONString() {
        return sensorsAsJSONString;
    }

    public void setSensorsAsJSONString(String sensorsAsJSONString) {
        this.sensorsAsJSONString = sensorsAsJSONString;
    }

    public boolean isTeamIsReady() {
        return teamIsReady;
    }

    public void setTeamIsReady(boolean teamIsReady) {
        this.teamIsReady = teamIsReady;
    }
}
