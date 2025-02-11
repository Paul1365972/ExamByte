// StudentController.java
package de.paul1365972.exambyte.controller;

import de.paul1365972.exambyte.domain.Test;
import de.paul1365972.exambyte.repository.TestRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final TestRepository testRepository;

    public StudentController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @GetMapping("/tests")
    public String zeigeVerfuegbareTests(Model model) {
        List<Test> verfuegbareTests = testRepository.findLaufendeTests(LocalDateTime.now());
        model.addAttribute("tests", verfuegbareTests);
        return "student/test_liste";
    }

}
