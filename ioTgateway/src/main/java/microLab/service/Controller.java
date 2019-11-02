package microLab.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import microLab.entity.Sensor;
import microLab.entity.Team;
import microLab.repository.SensorRepository;
import microLab.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.List;

import static microLab.utils.Constants.teams;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/students")
public class Controller {
    private Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @PostMapping("/{name}")
    public ResponseEntity<Team> createTeam(@PathVariable String name) {
        Team registeredTeam = teamRepository.save(new Team(name));
        return new ResponseEntity<>(registeredTeam, HttpStatus.OK);
    }

    @PostMapping(path = "/{name}/sensors", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addSensorsToTeam(@PathVariable String name, @RequestBody List<Sensor> sensors) throws JsonProcessingException {
        logger.debug("Requested team " + name + "\n {}", new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(sensors));

        if (teams.contains(name)) {
            Team team = teamRepository.findTeamByName(name)
                    .orElseGet(() -> teamRepository.save(new Team(name)));
            sensors.forEach(requestedSensor -> sensorRepository.save(new Sensor(requestedSensor.getName(), requestedSensor.getValue(), team)));
            return new ResponseEntity<>(team, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid Team Name : " + name, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<Team> getTeamInfo(@PathVariable String name) {
        return teamRepository.findTeamByName(name)
                .map(team -> new ResponseEntity<>(team, HttpStatus.OK))
                .orElseThrow(() -> new RestClientException("Team " + name + " does not exist."));
    }
}
