package de.paul1365972.exambyte.repository;

import de.paul1365972.exambyte.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByGithubId(String githubId);
}
