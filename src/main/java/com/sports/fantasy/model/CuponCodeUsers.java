package com.sports.fantasy.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "cupon_code_users")
public class CuponCodeUsers implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2248920323760962832L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "creator_user_id")
	private UserInfo creatorUser;

	@OneToOne
	@JoinColumn(name = "utilize_user_id")
	private UserInfo utilizeUser;

	@Column(name = "is_code_used")
	private boolean codeused = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserInfo getCreatorUser() {
		return creatorUser;
	}

	public void setCreatorUser(UserInfo creatorUser) {
		this.creatorUser = creatorUser;
	}

	public UserInfo getUtilizeUser() {
		return utilizeUser;
	}

	public void setUtilizeUser(UserInfo utilizeUser) {
		this.utilizeUser = utilizeUser;
	}

	public boolean isCodeused() {
		return codeused;
	}

	public void setCodeused(boolean codeused) {
		this.codeused = codeused;
	}

}
