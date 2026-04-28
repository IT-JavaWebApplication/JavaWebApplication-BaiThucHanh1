package com.example.controller;

import com.example.model.Student;
import com.example.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) { this.service = service; }

    @GetMapping
    public String list(Model model,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "sort", defaultValue = "studentCode") String sort,
                       @RequestParam(value = "dir", defaultValue = "asc") String dir,
                       @RequestParam(value = "page", defaultValue = "1") int page) {

        Map<String, Object> data = service.getProcessedList(keyword, sort, dir, page);

        model.addAttribute("students", data.get("list"));
        model.addAttribute("totalPages", data.get("totalPages"));
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("revDir", dir.equals("asc") ? "desc" : "asc");

        return "student/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("student", new Student());
        return "student/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("student", service.findById(id));
        return "student/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("student") Student student, Model model) {
        String error = null;
        if (service.isCodeExists(student.getStudentCode(), student.getId())) {
            error = "Mã SV đã tồn tại!";
        } else if (service.isEmailExists(student.getEmail(), student.getId())) {
            error = "Email đã tồn tại!";
        }

        if (error != null) {
            model.addAttribute("errorMessage", error);
            return "student/form";
        }

        service.save(student);
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        service.delete(id);
        return "redirect:/students";
    }
}