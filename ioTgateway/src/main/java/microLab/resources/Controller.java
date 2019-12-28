package microLab.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import microLab.entity.Sensor;
import microLab.entity.Team;
import microLab.entity.custom.Display;
import microLab.repository.DisplayedTeamRepository;
import microLab.repository.SensorRepository;
import microLab.repository.TeamRepository;
import microLab.services.Utils;
import microLab.services.model.SensorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private DisplayedTeamRepository displayedTeamRepository;

    @Autowired
    private Utils utils;

    @PostMapping("/{name}")
    public ResponseEntity<Team> createTeam(@PathVariable String name) {
        Team registeredTeam = teamRepository.save(new Team(name));
        return new ResponseEntity<>(registeredTeam, HttpStatus.OK);
    }

    @PostMapping(path = "/{name}/sensors", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postSensorsToTeam(@PathVariable String name, @RequestBody List<SensorDto> sensorsDto) throws JsonProcessingException {
        logger.debug("Requested team " + name + "\n {}", utils.toJsonString(sensorsDto));

        if (teams.contains(name)) {
            Team team = teamRepository.findTeamByName(name)
                    .orElseGet(() -> teamRepository.save(new Team(name)));

            List<Sensor> persistedSensors = sensorsDto.stream()
                    .map(requestedSensorDto -> sensorRepository.save(new Sensor(requestedSensorDto.getName(), requestedSensorDto.getValue(), team)))
                    .collect(Collectors.toList());

            Display displayedTeam = new Display(name, utils.toJsonString(persistedSensors));
            displayedTeamRepository.save(displayedTeam);
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

    @GetMapping("/{name}/sensors/latest")
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

    @GetMapping("/{name}/latestPostedSensors")
    public ResponseEntity<?> getLatestPostedSensorsPerTeam(@PathVariable String name) throws IOException {
        if (teamRepository.findTeamByName(name).isPresent()) {

            List<Sensor> sensors = utils.getTheLatestPersistedSensorsByTeam(name);
            return new ResponseEntity<>(sensors, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Team " + name + " does not exist.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Team> getAllTeamsWithLatestPostedSensors() {
        List<Team> teams = teamRepository.findAll();
        return teams.stream().map(team ->
                {
                    try {
                        team.setSensors(utils.getTheLatestPersistedSensorsByTeam(team.getName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return team;
                }
        ).collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<Team> getAllTeamsData() {
        return teamRepository.findAll();
    }
}
