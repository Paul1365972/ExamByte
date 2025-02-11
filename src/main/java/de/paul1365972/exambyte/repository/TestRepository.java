package de.paul1365972.exambyte.repository;

import de.paul1365972.exambyte.domain.Test;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TestRepository extends CrudRepository<Test, Long> {
	@Query("SELECT * FROM tests WHERE start_zeitpunkt <= :now AND end_zeitpunkt > :now")
	List<Test> findLaufendeTests(@Param("now") LocalDateTime now);

	@Query("SELECT * FROM tests WHERE end_zeitpunkt <= :now AND ergebnis_veroeffentlichungs_zeitpunkt > :now")
	List<Test> findAbelaufeneTests(@Param("now") LocalDateTime now);

	@Query("SELECT * FROM tests WHERE ergebnis_veroeffentlichungs_zeitpunkt <= :now")
	List<Test> findBewerteteTests(@Param("now") LocalDateTime now);
}
