package de.paul1365972.exambyte.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("korrektur")
public class Korrektur {

	@Id
	private Long answerId;
	private Long korrektorId;
	private String feedback;
	private Integer punkte;

	protected Korrektur() {
	}

	public Korrektur(Long answerId, Long korrektorId, String feedback, Integer punkte) {
		this.answerId = answerId;
		this.korrektorId = korrektorId;
		this.feedback = feedback;
		this.punkte = punkte;
	}

	public Long getAnswerId() {
		return answerId;
	}

	public Long getKorrektorId() {
		return korrektorId;
	}

	public String getFeedback() {
		return feedback;
	}

	public Integer getPunkte() {
		return punkte;
	}
}
