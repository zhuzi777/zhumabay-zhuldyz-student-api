package kz.iitu.zhumabay.studentapi.controller;

import java.util.List;

import jakarta.validation.Valid;
import kz.iitu.zhumabay.studentapi.exception.StudentNotFoundException;
import kz.iitu.zhumabay.studentapi.model.Student;
import kz.iitu.zhumabay.studentapi.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student create(@Valid @RequestBody Student student) {
        student.setId(null);
        return repository.save(student);
    }

    // READ all
    @GetMapping
    public List<Student> getAll() {
        return repository.findAll();
    }

    // READ one
    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @Valid @RequestBody Student changes) {
        Student student = repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        student.setName(changes.getName());
        student.setSurname(changes.getSurname());
        student.setEmail(changes.getEmail());
        student.setGroupName(changes.getGroupName());
        return repository.save(student);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
