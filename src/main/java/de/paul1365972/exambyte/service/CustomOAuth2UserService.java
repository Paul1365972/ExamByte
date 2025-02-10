package de.paul1365972.exambyte.service;

import de.paul1365972.exambyte.config.ExamByteConfig;
import de.paul1365972.exambyte.domain.User;
import de.paul1365972.exambyte.misc.UserType;
import de.paul1365972.exambyte.repository.UserRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ExamByteConfig examByteConfig;

	private final DefaultOAuth2UserService defaultService = new DefaultOAuth2UserService();

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User originalUser = defaultService.loadUser(userRequest);
		String githubId = originalUser.getAttribute("id").toString();
		String githubUsername = originalUser.getAttributes().get("login").toString();

		User user = userRepository.findByGithubId(githubId);
		if (user == null) {
			user = new User(githubUsername, githubId);
			userRepository.save(user);
		}

		UserType userType = UserType.STUDENT;
		if (examByteConfig.getOrganizers().contains(githubUsername)) {
			userType = UserType.ORGANIZER;
		} else if (examByteConfig.getCorrectors().contains(githubUsername)) {
			userType = UserType.KORREKTOR;
		}

		Set<GrantedAuthority> authorities = new HashSet<>(originalUser.getAuthorities());
		if (userType == UserType.ORGANIZER) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ORGANIZER"));
		} else if (userType == UserType.KORREKTOR) {
			authorities.add(new SimpleGrantedAuthority("ROLE_KORREKTOR"));
		} else if (userType == UserType.STUDENT) {
			authorities.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
		}

		Map<String, Object> attributes = new HashMap<>(originalUser.getAttributes());
		attributes.put("usertype", userType);

		return new DefaultOAuth2User(authorities, attributes, "id");
	}
}
