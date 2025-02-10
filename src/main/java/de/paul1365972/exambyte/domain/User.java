package de.paul1365972.exambyte.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
public class User {

	@Id
	private Long id;
	private String username;
	private String githubId;

	protected User() {
	}

	public User(Long id, String name, String githubId) {
		this.id = id;
		this.username = name;
		this.githubId = githubId;
	}

	public User(String username, String githubId) {
		this(null, username, githubId);
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getGithubId() {
		return githubId;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
