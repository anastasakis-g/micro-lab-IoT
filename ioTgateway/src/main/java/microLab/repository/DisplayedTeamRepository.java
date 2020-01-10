package microLab.repository;

import microLab.entity.custom.Display;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisplayedTeamRepository extends JpaRepository<Display, Long> {

    List<Display> findDisplayByTeamName(String teamName);

    Display findTopByTeamNameOrderByIdDesc(String teamName);
}
