package com.bbytes.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.netflix.config.DynamicPropertyFactory;

public class AdminAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Transactional(readOnly = true)
	protected final UserDetails retrieveUser(final String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {

		String adminUserName = DynamicPropertyFactory.getInstance().getStringProperty("admin-username", "admin").get();
		String adminPassword = DynamicPropertyFactory.getInstance().getStringProperty("admin-password", "admin").get();
		

		if (authentication.getPrincipal() == null)
			throw new AuthenticationServiceException("No username entered");

		if (authentication.getCredentials() == null)
			throw new AuthenticationServiceException("No password entered");

		if (!username.equals(adminUserName)) {
			throw new UsernameNotFoundException("User with username :" + username + " not found");
		}

		if (!authentication.getCredentials().toString().equals(adminPassword)) {
			throw new BadCredentialsException("credentials are invalid or doesn't match");
		}
		// // load the roles before returning for permission checks
		// if (user.getUserRoles() != null)
		// user.getUserRoles().size();
		UserDetails userDetails = new UserDetails() {

			@Override
			public boolean isEnabled() {
				return true;
			}

			@Override
			public boolean isCredentialsNonExpired() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public boolean isAccountNonLocked() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public boolean isAccountNonExpired() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public String getUsername() {
				// TODO Auto-generated method stub
				return username;
			}

			@Override
			public String getPassword() {
				return "not available";
			}

			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
				authorities.add(new SimpleGrantedAuthority("ADMIN"));
				return authorities;
			}
		};

		return userDetails;
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

}
