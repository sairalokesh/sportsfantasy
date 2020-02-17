package com.sports.fantasy.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "user_amount")
public class UserAmount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -33823126118904729L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "id")
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private UserInfo user;

	@Column(name = "bonus_amount")
	private double bonusAmount = 0.00;

	@Column(name = "added_amount")
	private double addedAmount = 0.00;

	@Transient
	private boolean phonevalid;

	@Transient
	private List<CuponCodeUsers> cuponCodeUsers;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public double getBonusAmount() {
		return bonusAmount;
	}

	public void setBonusAmount(double bonusAmount) {
		this.bonusAmount = bonusAmount;
	}

	public double getAddedAmount() {
		return addedAmount;
	}

	public void setAddedAmount(double addedAmount) {
		this.addedAmount = addedAmount;
	}

	public boolean isPhonevalid() {
		return phonevalid;
	}

	public void setPhonevalid(boolean phonevalid) {
		this.phonevalid = phonevalid;
	}

	public List<CuponCodeUsers> getCuponCodeUsers() {
		return cuponCodeUsers;
	}

	public void setCuponCodeUsers(List<CuponCodeUsers> cuponCodeUsers) {
		this.cuponCodeUsers = cuponCodeUsers;
	}

}
