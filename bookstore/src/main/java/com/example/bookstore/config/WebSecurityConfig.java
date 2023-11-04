package com.example.bookstore.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Slf4j
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/", "/home").permitAll()
				.requestMatchers("/api/books/**").hasAnyRole("USER", "ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")  // This line is added
				.anyRequest().authenticated()
			)
			.formLogin((form) -> form
				.loginPage("/login")
				.defaultSuccessUrl("/swagger-ui/index.html", true)
				.permitAll()
			)
			.logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/")
					.permitAll()
			);

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user =
				User.withDefaultPasswordEncoder()
						.username("user")
						.password("user")
						.roles("USER")
						.build();

		UserDetails admin =
				User.withDefaultPasswordEncoder()
						.username("admin")
						.password("admin")
						.roles("ADMIN")
						.build();

		return new InMemoryUserDetailsManager(user, admin);
	}
}