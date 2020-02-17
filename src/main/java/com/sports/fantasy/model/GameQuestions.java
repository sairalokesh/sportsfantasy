package com.sports.fantasy.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "game_questions")
public class GameQuestions implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 855814508857630712L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "id")
	private Long id;

	@Column(name = "team_one")
	private String teamOne;

	@Column(name = "team_one_image")
	private String teamOneImage;
	
	@Column(name = "team_two")
	private String teamTwo;

	@Column(name = "team_two_image")
	private String teamTwoImage;

	@Column(name = "question")
	private String question;

	@Column(name = "valid_date")
	private Date validDate;

	@Column(name = "spin_date")
	private Date spinDate;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "question_effect")
	private String questionEffect;

	@Column(name = "is_active")
	private boolean active = true;

	@Column(name = "choices")
	private Integer choices;

	@Column(name = "question_type")
	private String questionType;

	@Transient
	private String exValidDate;

	@Transient
	private String exSpinDate;

	@Transient
	private String exCreatedDate;

	@Transient
	private String gameQuestion;

	public GameQuestions() {
		super();
	}

	public GameQuestions(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTeamOne() {
		return teamOne;
	}

	public void setTeamOne(String teamOne) {
		this.teamOne = teamOne;
	}

	public String getTeamTwo() {
		return teamTwo;
	}

	public void setTeamTwo(String teamTwo) {
		this.teamTwo = teamTwo;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public Date getSpinDate() {
		return spinDate;
	}

	public void setSpinDate(Date spinDate) {
		this.spinDate = spinDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getQuestionEffect() {
		return questionEffect;
	}

	public void setQuestionEffect(String questionEffect) {
		this.questionEffect = questionEffect;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Integer getChoices() {
		return choices;
	}

	public void setChoices(Integer choices) {
		this.choices = choices;
	}

	public String getExValidDate() {
		return exValidDate;
	}

	public void setExValidDate(String exValidDate) {
		this.exValidDate = exValidDate;
	}

	public String getExSpinDate() {
		return exSpinDate;
	}

	public void setExSpinDate(String exSpinDate) {
		this.exSpinDate = exSpinDate;
	}

	public String getExCreatedDate() {
		return exCreatedDate;
	}

	public void setExCreatedDate(String exCreatedDate) {
		this.exCreatedDate = exCreatedDate;
	}

	public String getTeamOneImage() {
		return teamOneImage;
	}

	public void setTeamOneImage(String teamOneImage) {
		this.teamOneImage = teamOneImage;
	}

	public String getTeamTwoImage() {
		return teamTwoImage;
	}

	public void setTeamTwoImage(String teamTwoImage) {
		this.teamTwoImage = teamTwoImage;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getGameQuestion() {
		return gameQuestion;
	}

	public void setGameQuestion(String gameQuestion) {
		this.gameQuestion = gameQuestion;
	}

}
