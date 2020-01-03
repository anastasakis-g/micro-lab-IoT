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
        Display displayedData = displayedTeamRepository.findTopByTeamNameOrderByIdDesc(teamName);
        String sensorsAsString = displayedData.getSensorsAsJSONString();
        List<Sensor> sensors = new ObjectMapper().readValue(sensorsAsString, new TypeReference<List<Sensor>>() {
        });

        //TODO filter by timestamp
        return sensors;
    }
}
