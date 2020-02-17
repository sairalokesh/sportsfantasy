package com.sports.fantasy.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "game_participants")
public class GameParticipants implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5920541285790265222L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "id")
	private Long id;

	@Column(name = "participant_name")
	private String participantName;

	@Column(name = "participant_game_type")
	private String participantGameType;

	@Column(name = "participant_effect")
	private String participantEffect;

	@Column(name = "participant_image")
	private String participantImage;

	@Column(name = "participant_type")
	private String participantType;

	@ManyToOne
	@JoinColumn(name = "question_id")
	private GameQuestions gameQuestions;

	@Column(name = "participant_points")
	private double participantPoints;

	@Column(name = "participant_rating")
	private double participantRating;

	@Column(name = "availability")
	private String availability;

	@Transient
	private Long capitanId;

	@Transient
	private Long viceCapitanId;

	@Transient
	private Long suppoterId;

	public GameParticipants() {
		super();
	}

	public GameParticipants(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public String getParticipantEffect() {
		return participantEffect;
	}

	public void setParticipantEffect(String participantEffect) {
		this.participantEffect = participantEffect;
	}

	public String getParticipantImage() {
		return participantImage;
	}

	public void setParticipantImage(String participantImage) {
		this.participantImage = participantImage;
	}

	public String getParticipantType() {
		return participantType;
	}

	public void setParticipantType(String participantType) {
		this.participantType = participantType;
	}

	public GameQuestions getGameQuestions() {
		return gameQuestions;
	}

	public void setGameQuestions(GameQuestions gameQuestions) {
		this.gameQuestions = gameQuestions;
	}

	public double getParticipantPoints() {
		return participantPoints;
	}

	public void setParticipantPoints(double participantPoints) {
		this.participantPoints = participantPoints;
	}

	public double getParticipantRating() {
		return participantRating;
	}

	public void setParticipantRating(double participantRating) {
		this.participantRating = participantRating;
	}

	public String getParticipantGameType() {
		return participantGameType;
	}

	public void setParticipantGameType(String participantGameType) {
		this.participantGameType = participantGameType;
	}

	public Long getCapitanId() {
		return capitanId;
	}

	public void setCapitanId(Long capitanId) {
		this.capitanId = capitanId;
	}

	public Long getViceCapitanId() {
		return viceCapitanId;
	}

	public void setViceCapitanId(Long viceCapitanId) {
		this.viceCapitanId = viceCapitanId;
	}

	public Long getSuppoterId() {
		return suppoterId;
	}

	public void setSuppoterId(Long suppoterId) {
		this.suppoterId = suppoterId;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

}
