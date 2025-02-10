package de.paul1365972.exambyte.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "exambyte")
public class ExamByteConfig {

	private List<String> organizers;
	private List<String> correctors;

	public List<String> getOrganizers() {
		return organizers;
	}

	public void setOrganizers(List<String> organizers) {
		this.organizers = organizers;
	}

	public List<String> getCorrectors() {
		return correctors;
	}

	public void setCorrectors(List<String> correctors) {
		this.correctors = correctors;
	}
}
