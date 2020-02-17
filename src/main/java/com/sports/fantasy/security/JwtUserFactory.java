package com.sports.fantasy.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.sports.fantasy.model.UserInfo;

public final class JwtUserFactory {

	private JwtUserFactory() {
	}

	public static JwtUser create(UserInfo user) {
		return new JwtUser(user.getId(), user, user.getEmail(), user.getPassword(),
				mapToGrantedAuthorities(new ArrayList<String>(Arrays.asList("ROLE_" + user.getRole()))),
				user.isEnabled(), null);
	}

	private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
		return authorities.stream().map(authority -> new SimpleGrantedAuthority(authority))
				.collect(Collectors.toList());
	}
}
