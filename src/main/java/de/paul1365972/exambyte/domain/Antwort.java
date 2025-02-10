package de.paul1365972.exambyte.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("answers")
public class Antwort {

	@Id
	private Long id;
	private Long studentId;
	private Long questionId;
	private Long antwortMcItem;
	private String antwortText;

	protected Antwort() {
	}

	public Antwort(Long id, Long studentId, Long questionId, Long antwortMcItem, String antwortText) {
		this.id = id;
		this.studentId = studentId;
		this.questionId = questionId;
		this.antwortMcItem = antwortMcItem;
		this.antwortText = antwortText;
	}

	public Antwort(Long studentId, Long questionId, Long antwortMcItem, String antwortText) {
		this(null, studentId, questionId, antwortMcItem, antwortText);
	}

	public Long getId() {
		return id;
	}

	public Long getStudentId() {
		return studentId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public Long getAntwortMcItem() {
		return antwortMcItem;
	}

	public String getAntwortText() {
		return antwortText;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
