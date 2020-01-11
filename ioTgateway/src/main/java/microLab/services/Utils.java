package microLab.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import microLab.entity.Sensor;
import microLab.entity.custom.Display;
import microLab.repository.DisplayedTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class Utils {

    @Autowired
    private DisplayedTeamRepository displayedTeamRepository;

    public String toJsonString(Object object) throws JsonProcessingException {
        return new ObjectMapper()
                .writeValueAsString(object);
    }

    public List<Sensor> getTheLatestPersistedSensorsByTeam(String teamName) throws IOException {
        if (displayedTeamRepository.findDisplayByTeamName(teamName).size() > 0) {
            Display displayedData = displayedTeamRepository.findTopByTeamNameOrderByIdDesc(teamName);

            String sensorsAsString = displayedData.getSensorsAsJSONString();
            List<Sensor> sensors = new ObjectMapper().readValue(sensorsAsString, new TypeReference<List<Sensor>>() {
            });

            Sensor sensor = sensors.get(sensors.size() - 1);
            /** Display only the persisted sensors in last 10 minutes. */
            if (sensor.getTimeReceived().isAfter(LocalDateTime.now().minusMinutes(10))) {
                return sensors;
            } else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }
}
