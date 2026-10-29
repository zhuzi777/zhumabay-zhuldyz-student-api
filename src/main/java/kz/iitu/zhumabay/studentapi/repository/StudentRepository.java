package kz.iitu.zhumabay.studentapi.repository;

import java.util.List;

import kz.iitu.zhumabay.studentapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findBySurnameIgnoreCase(String surname);
}
