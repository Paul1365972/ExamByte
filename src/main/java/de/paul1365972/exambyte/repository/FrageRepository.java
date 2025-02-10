package de.paul1365972.exambyte.repository;

import de.paul1365972.exambyte.domain.Frage;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface FrageRepository extends CrudRepository<Frage, Long> {
	List<Frage> findByTestId(Long testId);
}
