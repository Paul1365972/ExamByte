package de.paul1365972.exambyte.repository;

import de.paul1365972.exambyte.domain.FrageMultipleChoiceItem;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface FrageMultipleChoiceItemRepository extends CrudRepository<FrageMultipleChoiceItem, Long> {
	List<FrageMultipleChoiceItem> findByQuestionId(Long questionId);
}
