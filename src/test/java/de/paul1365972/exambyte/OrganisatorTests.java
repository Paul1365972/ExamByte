package de.paul1365972.exambyte;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.paul1365972.exambyte.config.GitHubOAuthConfiguration;
import de.paul1365972.exambyte.controller.OrganisatorController;
import de.paul1365972.exambyte.domain.Frage;
import de.paul1365972.exambyte.domain.FrageMultipleChoiceItem;
import de.paul1365972.exambyte.domain.FrageType;
import de.paul1365972.exambyte.repository.FrageMultipleChoiceItemRepository;
import de.paul1365972.exambyte.repository.FrageRepository;
import de.paul1365972.exambyte.repository.TestRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(OrganisatorController.class)
@Import(GitHubOAuthConfiguration.class)
class OrganisatorTests {

	@Autowired
	private MockMvc mvc;

	@MockitoBean
	private TestRepository testRepository;

	@MockitoBean
	private FrageRepository frageRepository;

	@MockitoBean
	private FrageMultipleChoiceItemRepository mcItemRepository;

	@Test
	@DisplayName("GET /organisator/tests zeigt eine Liste aller Tests mit korrekten Daten an")
	void test_listTests() throws Exception {
		// Arrange
		LocalDateTime now = LocalDateTime.now();
		de.paul1365972.exambyte.domain.Test test1 = new de.paul1365972.exambyte.domain.Test("Test 1", now,
				now.plusHours(2), now.plusHours(3));
		test1.setId(1L);
		de.paul1365972.exambyte.domain.Test test2 = new de.paul1365972.exambyte.domain.Test("Test 2", now.plusDays(1),
				now.plusDays(1).plusHours(2), now.plusDays(1).plusHours(3));
		test2.setId(2L);
		List<de.paul1365972.exambyte.domain.Test> tests = List.of(test1, test2);
		when(testRepository.findAll()).thenReturn(tests);

		// Act & Assert
		MvcResult result = mvc
				.perform(get("/organisator/tests").with(oauth2Login().authorities(() -> "ROLE_ORGANIZER")))
				.andExpect(status().isOk()).andExpect(view().name("organisator/list"))
				.andExpect(model().attributeExists("tests")).andReturn();

		@SuppressWarnings("unchecked")
		List<Test> modelTests = (List<Test>) result.getModelAndView().getModel().get("tests");
		assertThat(modelTests).hasSize(2).extracting("titel").containsExactly("Test 1", "Test 2");

		verify(testRepository).findAll();
	}

	@Test
	@DisplayName("GET /organisator/tests/new erstellt einen neuen Test mit Standardwerten")
	void test_createNewTest() throws Exception {
		// Arrange
		ArgumentCaptor<de.paul1365972.exambyte.domain.Test> testCaptor = ArgumentCaptor
				.forClass(de.paul1365972.exambyte.domain.Test.class);
		de.paul1365972.exambyte.domain.Test savedTest = new de.paul1365972.exambyte.domain.Test("New Test",
				LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
		savedTest.setId(1L);
		when(testRepository.save(any(de.paul1365972.exambyte.domain.Test.class))).thenReturn(savedTest);

		// Act
		mvc.perform(get("/organisator/tests/new").with(oauth2Login().authorities(() -> "ROLE_ORGANIZER")))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("./1/edit"));

		// Assert
		verify(testRepository).save(testCaptor.capture());
		de.paul1365972.exambyte.domain.Test capturedTest = testCaptor.getValue();
		assertThat(capturedTest.getTitel()).isEqualTo("New Test");
		assertThat(capturedTest.getStartZeitpunkt()).isNotNull();
		assertThat(capturedTest.getEndZeitpunkt()).isNotNull();
		assertThat(capturedTest.getErgebnisVeroeffentlichungsZeitpunkt()).isNotNull();
	}

	@Test
	@DisplayName("GET /organisator/tests/{id}/edit lädt alle notwendigen Testdaten")
	void test_editTestForm() throws Exception {
		// Arrange
		LocalDateTime now = LocalDateTime.now();
		de.paul1365972.exambyte.domain.Test test = new de.paul1365972.exambyte.domain.Test("Test 1", now,
				now.plusHours(2), now.plusHours(3));
		test.setId(1L);

		Frage mcFrage = new Frage(1L, FrageType.MultipleChoice, "Was ist 2+2?", 2);
		mcFrage.setId(1L);
		Frage textFrage = new Frage(1L, FrageType.Freitext, "Warum ist es 5?!", 5);
		textFrage.setId(2L);

		List<FrageMultipleChoiceItem> mcItems = List.of(new FrageMultipleChoiceItem("4", 1L, true),
				new FrageMultipleChoiceItem("5", 1L, false));

		when(testRepository.findById(1L)).thenReturn(Optional.of(test));
		when(frageRepository.findByTestId(1L)).thenReturn(List.of(mcFrage, textFrage));
		when(mcItemRepository.findByQuestionId(1L)).thenReturn(mcItems);
		when(mcItemRepository.findByQuestionId(2L)).thenReturn(List.of());

		// Act & Assert
		MvcResult result = mvc
				.perform(get("/organisator/tests/1/edit").with(oauth2Login().authorities(() -> "ROLE_ORGANIZER")))
				.andExpect(status().isOk()).andExpect(view().name("organisator/edit"))
				.andExpect(model().attributeExists("test", "questions", "mcItems")).andReturn();

		de.paul1365972.exambyte.domain.Test modelTest = (de.paul1365972.exambyte.domain.Test) result.getModelAndView()
				.getModel().get("test");
		assertThat(modelTest).isNotNull();
		assertThat(modelTest.getId()).isEqualTo(1L);
		assertThat(modelTest.getTitel()).isEqualTo("Test 1");

		@SuppressWarnings("unchecked")
		List<Frage> modelQuestions = (List<Frage>) result.getModelAndView().getModel().get("questions");
		assertThat(modelQuestions).hasSize(2);
		assertThat(modelQuestions.get(0).getTyp()).isEqualTo(FrageType.MultipleChoice);
		assertThat(modelQuestions.get(1).getTyp()).isEqualTo(FrageType.Freitext);

		@SuppressWarnings("unchecked")
		List<List<FrageMultipleChoiceItem>> modelMcItems = (List<List<FrageMultipleChoiceItem>>) result
				.getModelAndView().getModel().get("mcItems");
		assertThat(modelMcItems).hasSize(2);
		assertThat(modelMcItems.get(0)).hasSize(2);
		assertThat(modelMcItems.get(1)).isEmpty();
	}

	@Test
	@DisplayName("POST /organisator/tests/{id}/edit aktualisiert einen Test mit allen Attributen")
	void test_updateTest() throws Exception {
		// Arrange
		LocalDateTime now = LocalDateTime.now();
		ArgumentCaptor<de.paul1365972.exambyte.domain.Test> testCaptor = ArgumentCaptor
				.forClass(de.paul1365972.exambyte.domain.Test.class);

		// Act
		mvc.perform(post("/organisator/tests/1/edit").with(oauth2Login().authorities(() -> "ROLE_ORGANIZER"))
				.with(csrf()).param("id", "1").param("titel", "Updated Test").param("startZeitpunkt", now.toString())
				.param("endZeitpunkt", now.plusHours(2).toString())
				.param("ergebnisVeroeffentlichungsZeitpunkt", now.plusHours(3).toString()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("./tests"));

		// Assert
		verify(testRepository).save(testCaptor.capture());
		de.paul1365972.exambyte.domain.Test capturedTest = testCaptor.getValue();
		assertThat(capturedTest.getTitel()).isEqualTo("Updated Test");
		assertThat(capturedTest.getStartZeitpunkt()).isEqualTo(now);
		assertThat(capturedTest.getEndZeitpunkt()).isEqualTo(now.plusHours(2));
		assertThat(capturedTest.getErgebnisVeroeffentlichungsZeitpunkt()).isEqualTo(now.plusHours(3));
	}

	@Test
	@DisplayName("POST /organisator/tests/{id}/questions/new fügt eine neue Frage hinzu")
	void test_addQuestion() throws Exception {
		// Arrange
		ArgumentCaptor<Frage> frageCaptor = ArgumentCaptor.forClass(Frage.class);

		// Act
		mvc.perform(post("/organisator/tests/1/questions/new").with(oauth2Login().authorities(() -> "ROLE_ORGANIZER"))
				.with(csrf()).param("typ", "MultipleChoice").param("fragestellung", "Was ist 2+2?")
				.param("maximalePunktzahl", "2")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("../edit"));

		// Assert
		verify(frageRepository).save(frageCaptor.capture());
		Frage capturedFrage = frageCaptor.getValue();
		assertThat(capturedFrage.getTestId()).isEqualTo(1L);
		assertThat(capturedFrage.getTyp()).isEqualTo(FrageType.MultipleChoice);
		assertThat(capturedFrage.getFragestellung()).isEqualTo("Was ist 2+2?");
		assertThat(capturedFrage.getMaximalePunktzahl()).isEqualTo(2);
	}

	@Test
	@DisplayName("Zugriff wird für nicht-Organisatoren verweigert")
	void test_accessControl() throws Exception {
		// TODO: Aufteilen
		// Student test
		mvc.perform(get("/organisator/tests").with(oauth2Login().authorities(() -> "ROLE_STUDENT")))
				.andExpect(status().isForbidden());

		// Korrektor test
		mvc.perform(get("/organisator/tests").with(oauth2Login().authorities(() -> "ROLE_KORREKTOR")))
				.andExpect(status().isForbidden());

		// Nicht einmal eingeloggte user
		mvc.perform(get("/organisator/tests")).andExpect(status().is3xxRedirection());

		// Verifiziere, dass keine Repository-Aufrufe stattfanden
		verifyNoInteractions(testRepository);
	}
}
