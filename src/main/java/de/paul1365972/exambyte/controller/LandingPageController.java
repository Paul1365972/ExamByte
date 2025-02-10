package de.paul1365972.exambyte.controller;

import de.paul1365972.exambyte.domain.User;
import de.paul1365972.exambyte.misc.UserType;
import de.paul1365972.exambyte.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingPageController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public String landingPage(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
			OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
			Integer githubId = (Integer) oauthUser.getAttributes().get("id");
			UserType usertype = (UserType) oauthUser.getAttributes().get("usertype");

			User user = userRepository.findByGithubId(githubId.toString());

			model.addAttribute("username", user.getUsername());
			model.addAttribute("usertype", usertype.toString());
			model.addAttribute("isLoggedIn", true);
		} else {
			model.addAttribute("isLoggedIn", false);
		}

		return "index";
	}
}
