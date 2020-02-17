package com.sports.fantasy.domain;

public class Ranking {

	private Integer rank;
	private double score;

	private String userName;
	private Long userId;
	private Long amountId;
	private Long questionId;
	private Long id;
	private Double winningAmount;
	private String winningResult;
	private String imageUrl;

	public Ranking() {
		super();
	}

	public Ranking(Integer rank, double score, String userName, Long userId, Long amountId, Long questionId, Long id,
			Double winningAmount, String winningResult, String imageUrl) {
		super();
		this.rank = rank;
		this.score = score;
		this.userName = userName;
		this.userId = userId;
		this.amountId = amountId;
		this.questionId = questionId;
		this.id = id;
		this.winningAmount = winningAmount;
		this.winningResult = winningResult;
		this.imageUrl = imageUrl;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAmountId() {
		return amountId;
	}

	public void setAmountId(Long amountId) {
		this.amountId = amountId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getWinningAmount() {
		return winningAmount;
	}

	public void setWinningAmount(Double winningAmount) {
		this.winningAmount = winningAmount;
	}

	public String getWinningResult() {
		return winningResult;
	}

	public void setWinningResult(String winningResult) {
		this.winningResult = winningResult;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
