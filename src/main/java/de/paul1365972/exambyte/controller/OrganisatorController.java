package de.paul1365972.exambyte.controller;

import de.paul1365972.exambyte.domain.Frage;
import de.paul1365972.exambyte.domain.FrageMultipleChoiceItem;
import de.paul1365972.exambyte.domain.FrageType;
import de.paul1365972.exambyte.domain.Test;
import de.paul1365972.exambyte.repository.FrageMultipleChoiceItemRepository;
import de.paul1365972.exambyte.repository.FrageRepository;
import de.paul1365972.exambyte.repository.TestRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/organisator")
public class OrganisatorController {

	@Autowired
	private TestRepository testRepository;

	@Autowired
	private FrageRepository frageRepository;

	@Autowired
	private FrageMultipleChoiceItemRepository mcItemRepository;

	@GetMapping("/")
	public String indexRedirect() {
		return "redirect:./tests";
	}

	@GetMapping("/tests")
	public String listTests(Model model) {
		List<Test> tests = (List<Test>) testRepository.findAll();
		model.addAttribute("tests", tests);
		return "organisator/list";
	}

	@GetMapping("/tests/{id}/edit")
	public String editTestForm(@PathVariable Long id, Model model) {
		Test test = testRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid test id: " + id));
		List<Frage> fragen = frageRepository.findByTestId(id);
		List<List<FrageMultipleChoiceItem>> mcItems = fragen.stream()
				.map(frage -> mcItemRepository.findByQuestionId(frage.getId())).collect(Collectors.toList());
		model.addAttribute("test", test);
		model.addAttribute("testId", id);
		model.addAttribute("questions", fragen);
		model.addAttribute("mcItems", mcItems);
		model.addAttribute("newQuestion", new Frage(id, FrageType.Freitext, "", 0));
		model.addAttribute("newMcItem", new FrageMultipleChoiceItem("", null, false));
		return "organisator/edit";
	}

	@PostMapping("/tests/{id}/edit")
	public String updateTest(@PathVariable Long id, @ModelAttribute("test") Test test) {
		testRepository.save(test);
		return "redirect:./tests";
	}

	@GetMapping("/tests/new")
	public String newTest() {
		LocalDateTime now = LocalDateTime.now();
		Test newTest = new Test("New Test", now, now, now);
		Test savedTest = testRepository.save(newTest);
		return "redirect:./" + savedTest.getId() + "/edit";
	}

	@PostMapping("/tests/{id}/delete")
	public String deleteTest(@PathVariable Long id) {
		testRepository.deleteById(id);
		return "redirect:../..";
	}

	@PostMapping("/tests/{testId}/questions/new")
	public String addQuestion(@PathVariable Long testId, @ModelAttribute("newQuestion") Frage frage) {
		frage.setTestId(testId);
		frageRepository.save(frage);
		return "redirect:../edit";
	}

	@PostMapping("/tests/{testId}/questions/{questionId}/addMcItem")
	public String addQuestionMcItem(@PathVariable Long testId, @PathVariable Long questionId,
			@ModelAttribute("newMcItem") FrageMultipleChoiceItem mcItem) {
		mcItem.setQuestionId(questionId);
		mcItemRepository.save(mcItem);
		return "redirect:../../edit";
	}

	@PostMapping("/tests/{testId}/questions/{questionId}/delete")
	public String deleteQuestion(@PathVariable Long testId, @PathVariable Long questionId) {
		frageRepository.deleteById(questionId);
		return "redirect:../../edit";
	}
}
