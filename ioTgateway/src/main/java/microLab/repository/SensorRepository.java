package microLab.repository;

import microLab.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

    Sensor findTopByTeamIdOrderByIdDesc(long teamId);
}
