package com.example.service;

import com.example.model.Student;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private List<Student> students = new ArrayList<>();
    private long currentId = 1;

    public StudentService() {
        // Dữ liệu mẫu
        save(new Student(null, "SV0001", "Nguyen Van A", "a@gmail.com", "CNTT", 8.5));
        save(new Student(null, "SV0002", "Tran Thi B", "b@gmail.com", "Kinh tế", 7.0));
    }

    public List<Student> getAll() { return students; }

    public void save(Student student) {
        if (student.getId() == null) {
            student.setId(currentId++);
            students.add(student);
        } else {
            int index = -1;
            for(int i=0; i<students.size(); i++) {
                if(students.get(i).getId().equals(student.getId())) {
                    index = i; break;
                }
            }
            students.set(index, student);
        }
    }

    public void delete(Long id) {
        students.removeIf(s -> s.getId().equals(id));
    }

    public Student findById(Long id) {
        return students.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    public boolean isCodeExists(String code, Long id) {
        return students.stream().anyMatch(s -> s.getStudentCode().equalsIgnoreCase(code) && !s.getId().equals(id));
    }

    public boolean isEmailExists(String email, Long id) {
        return students.stream().anyMatch(s -> s.getEmail().equalsIgnoreCase(email) && !s.getId().equals(id));
    }

    public Map<String, Object> getProcessedList(String keyword, String sort, String dir, int page) {
        List<Student> filtered = students.stream()
                .filter(s -> keyword == null || s.getFullName().toLowerCase().contains(keyword.toLowerCase())
                        || s.getStudentCode().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        if (sort != null) {
            Comparator<Student> comp = switch (sort) {
                case "fullName" -> Comparator.comparing(Student::getFullName);
                case "gpa" -> Comparator.comparing(Student::getGpa);
                default -> Comparator.comparing(Student::getStudentCode);
            };
            if ("desc".equals(dir)) comp = comp.reversed();
            filtered.sort(comp);
        }

        int pageSize = 5;
        int totalItems = filtered.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalItems);

        List<Student> pagedList = (start < totalItems) ? filtered.subList(start, end) : new ArrayList<>();

        Map<String, Object> result = new HashMap<>();
        result.put("list", pagedList);
        result.put("totalPages", totalPages);
        return result;
    }
}