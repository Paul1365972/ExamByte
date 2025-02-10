package de.paul1365972.exambyte.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("questions_mc_item")
public class FrageMultipleChoiceItem {

	@Id
	private Long id;
	private Long questionId;
	private String text;
	private Boolean korrekt;

	protected FrageMultipleChoiceItem() {
	}

	public FrageMultipleChoiceItem(Long id, Long questionId, String text, Boolean korrekt) {
		this.id = id;
		this.questionId = questionId;
		this.text = text;
		this.korrekt = korrekt;
	}

	public FrageMultipleChoiceItem(String text, Long questionId, Boolean korrekt) {
		this(null, questionId, text, korrekt);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getKorrekt() {
		return korrekt;
	}

	public void setKorrekt(Boolean korrekt) {
		this.korrekt = korrekt;
	}

}
