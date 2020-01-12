package microLab.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import static microLab.utils.Constants.sensorNames;
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

    @PostMapping(path = "/{name}/sensors", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postSensorsToTeam(@PathVariable String name, @RequestParam(value = "isReady", defaultValue = "false") boolean teamIsReady, @RequestBody List<SensorDto> sensorsDto) throws JsonProcessingException {
        logger.debug("Requested team " + name + "\n {}", utils.toJsonString(sensorsDto));

        if (teams.contains(name)) {
            Team team = teamRepository.findTeamByName(name)
                    .orElseGet(() -> teamRepository.save(new Team(name)));

            if (sensorsDto.size() == 0) return new ResponseEntity<>("Empty Payload.", HttpStatus.OK);
            team.setReady(teamIsReady);

            List<Sensor> persistedSensors = sensorsDto.stream()
                    .filter(requestedSensorDto -> sensorNames.contains(requestedSensorDto.getName()))
                    .map(requestedSensorDto -> sensorRepository.save(new Sensor(requestedSensorDto.getName(), requestedSensorDto.getValue(), team)))
                    .collect(Collectors.toList());

            if (persistedSensors.size() >= 1) {
                Display displayedTeam = new Display(name, utils.toJsonString(persistedSensors), teamIsReady);
                displayedTeamRepository.save(displayedTeam);
            }

            List<String> invalidRequestedSensorNames = sensorsDto.stream()
                    .filter(s -> !sensorNames.contains(s.getName()))
                    .map(SensorDto::getName)
                    .collect(Collectors.toList());
            if (invalidRequestedSensorNames.size() > 0)
                return new ResponseEntity<>("Invalid Sensor:" + utils.toJsonString(invalidRequestedSensorNames), HttpStatus.OK);

            return new ResponseEntity<>("OK Team:" + team.getName(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid Team:" + name, HttpStatus.OK);
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
        return teams.stream().filter(team -> {
            int size = 0;
            try {
                size = utils.getTheLatestPersistedSensorsByTeam(team.getName()).size();
            } catch (IOException ignored) {
            }
            return size > 0;
        }).map(team -> {
            try {
                team.setSensors(utils.getTheLatestPersistedSensorsByTeam(team.getName()));
            } catch (IOException ignored) {
            }
            return team;
        }).collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<Team> getAllTeamsData() {
        return teamRepository.findAll();
    }
}
