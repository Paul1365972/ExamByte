package de.paul1365972.exambyte.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class GitHubOAuthConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/", "/public/**", "/css/**").permitAll()
				.requestMatchers("/organisator/**").hasAuthority("ROLE_ORGANIZER").requestMatchers("/todo/**")
				.hasAuthority("ROLE_KORREKTOR").requestMatchers("/student/**").hasAuthority("ROLE_STUDENT")
				.anyRequest().authenticated()).oauth2Login(Customizer.withDefaults())
				.logout(logout -> logout.logoutSuccessUrl("/").permitAll());
		return http.build();
	}
}
