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

    public Display() {
    }

    public Display(String teamName, String sensors) {
        this.teamName = teamName;
        this.sensorsAsJSONString = sensors;
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
}
