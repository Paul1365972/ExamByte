package de.paul1365972.exambyte;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import de.paul1365972.exambyte.config.GitHubOAuthConfiguration;
import de.paul1365972.exambyte.controller.LandingPageController;
import de.paul1365972.exambyte.domain.User;
import de.paul1365972.exambyte.misc.UserType;
import de.paul1365972.exambyte.repository.UserRepository;

@WebMvcTest(LandingPageController.class)
@Import(GitHubOAuthConfiguration.class)
class LandingPageControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @DisplayName("Die Landing Page zeigt für nicht eingeloggte Nutzer den Login-Button")
    void test_landingPageNotLoggedIn() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("isLoggedIn", false));
    }

    @Test
    @DisplayName("Die Landing Page zeigt für eingeloggte Nutzer den Username und Usertype")
    void test_landingPageLoggedIn() throws Exception {
        // Arrange
        User testUser = new User("testuser", "12345");
        when(userRepository.findByGithubId(anyString())).thenReturn(testUser);

        // Act & Assert
        mvc.perform(get("/")
                .with(SecurityMockMvcRequestPostProcessors.oauth2Login()
                        .attributes(attrs -> {
                            attrs.put("id", 12345);
                            attrs.put("login", "testuser");
                            attrs.put("usertype", UserType.STUDENT);
                        })))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("isLoggedIn", true))
                .andExpect(model().attribute("username", "testuser"))
                .andExpect(model().attribute("usertype", "STUDENT"));
    }
}
