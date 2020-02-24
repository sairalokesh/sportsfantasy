
package com.sports.fantasy.domain;

import java.io.Serializable;
import com.sports.fantasy.model.UserInfo;

public class UserTokenInfo implements Serializable {

	private static final long serialVersionUID = -5488291851737060509L;

	private UserInfo user;
	private String token;
	private String message;

	public UserTokenInfo() {
		super();
	}

	public UserTokenInfo(String message) {
		super();
		this.message = message;
	}

	public UserTokenInfo(UserInfo user, String token) {
		super();
		this.user = user;
		this.token = token;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
