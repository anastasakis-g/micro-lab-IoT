package microLab.repository;

import microLab.entity.custom.Display;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisplayedTeamRepository extends JpaRepository<Display, Long> {

    Display findTopByTeamNameOrderByIdDesc(String teamName);
}
