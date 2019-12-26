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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

            String uuid = UUID.randomUUID().toString();

            //When a team requests to create a sensor with the same name, a new record will be created in DB with a different primary key.
            sensors.forEach(requestedSensor -> sensorRepository.save(new Sensor(requestedSensor.getName(), requestedSensor.getValue(), team, uuid)));
            return new ResponseEntity<>("Successfully Received Data from team " + team.getName() + ".", HttpStatus.OK);
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

    @GetMapping("/{name}/latest")
    public ResponseEntity<?> getLatestSensorDataPerTeam(@PathVariable String name) {
        Optional<Team> optionalTeam = teamRepository.findTeamByName(name);
        if (optionalTeam.isPresent()) {
            Team team = optionalTeam.get();
            //Get the latest received sensor (for the given team)
            Sensor latestRecordsPerTeam = sensorRepository.findTopByTeamIdOrderByIdDesc(team.getId());
            return new ResponseEntity<>(latestRecordsPerTeam, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Team " + name + " does not exist.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{name}/latestPOSTCall")
    public ResponseEntity<?> getLatestPOSTCallDataPerTeam(@PathVariable String name) {
        Optional<Team> optionalTeam = teamRepository.findTeamByName(name);
        if (optionalTeam.isPresent()) {
            Team team = optionalTeam.get();
            //Get the latest received sensor (for the given team)
            String latestUUID = sensorRepository.findTopByTeamIdOrderByIdDesc(team.getId()).getUuid();

            List<Sensor> latestRecordsPerTeam = team.getSensors().stream()
                    .filter(sensor -> sensor.getUuid().equals(latestUUID))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(latestRecordsPerTeam, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Team " + name + " does not exist.", HttpStatus.BAD_REQUEST);
        }
    }
}
