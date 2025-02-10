package de.paul1365972.exambyte.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("questions")
public class Frage {

	@Id
	private Long id;
	private Long testId;
	private FrageType typ;
	private String fragestellung;
	private Integer maximalePunktzahl;

	protected Frage() {
	}

	public Frage(Long id, Long testId, FrageType typ, String fragestellung, Integer maximalePunktzahl) {
		this.id = id;
		this.testId = testId;
		this.typ = typ;
		this.fragestellung = fragestellung;
		this.maximalePunktzahl = maximalePunktzahl;
	}

	public Frage(Long testId, FrageType typ, String fragestellung, Integer maximalePunktzahl) {
		this(null, testId, typ, fragestellung, maximalePunktzahl);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long test_id) {
		this.testId = test_id;
	}

	public FrageType getTyp() {
		return typ;
	}

	public void setTyp(FrageType typ) {
		this.typ = typ;
	}

	public String getFragestellung() {
		return fragestellung;
	}

	public void setFragestellung(String fragestellung) {
		this.fragestellung = fragestellung;
	}

	public Integer getMaximalePunktzahl() {
		return maximalePunktzahl;
	}

	public void setMaximalePunktzahl(Integer maximalePunktzahl) {
		this.maximalePunktzahl = maximalePunktzahl;
	}
}
