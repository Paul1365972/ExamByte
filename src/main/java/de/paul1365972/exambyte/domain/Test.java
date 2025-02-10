package de.paul1365972.exambyte.domain;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("tests")
public class Test {

	@Id
	private Long id;
	private String titel;
	private LocalDateTime startZeitpunkt;
	private LocalDateTime endZeitpunkt;
	private LocalDateTime ergebnisVeroeffentlichungsZeitpunkt;

	protected Test() {
	}

	public Test(Long id, String titel, LocalDateTime startZeitpunkt, LocalDateTime endZeitpunkt,
			LocalDateTime ergebnisVeroeffentlichungsZeitpunkt) {
		this.id = id;
		this.titel = titel;
		this.startZeitpunkt = startZeitpunkt;
		this.endZeitpunkt = endZeitpunkt;
		this.ergebnisVeroeffentlichungsZeitpunkt = ergebnisVeroeffentlichungsZeitpunkt;
	}

	public Test(String titel, LocalDateTime startZeitpunkt, LocalDateTime endZeitpunkt,
			LocalDateTime ergebnisVeroeffentlichungsZeitpunkt) {
		this(null, titel, startZeitpunkt, endZeitpunkt, ergebnisVeroeffentlichungsZeitpunkt);
	}

	public Long getId() {
		return id;
	}

	public String getTitel() {
		return titel;
	}

	public LocalDateTime getStartZeitpunkt() {
		return startZeitpunkt;
	}

	public LocalDateTime getEndZeitpunkt() {
		return endZeitpunkt;
	}

	public LocalDateTime getErgebnisVeroeffentlichungsZeitpunkt() {
		return ergebnisVeroeffentlichungsZeitpunkt;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public void setStartZeitpunkt(LocalDateTime startZeitpunkt) {
		this.startZeitpunkt = startZeitpunkt;
	}

	public void setEndZeitpunkt(LocalDateTime endZeitpunkt) {
		this.endZeitpunkt = endZeitpunkt;
	}

	public void setErgebnisVeroeffentlichungsZeitpunkt(LocalDateTime ergebnisVeroeffentlichungsZeitpunkt) {
		this.ergebnisVeroeffentlichungsZeitpunkt = ergebnisVeroeffentlichungsZeitpunkt;
	}
}
