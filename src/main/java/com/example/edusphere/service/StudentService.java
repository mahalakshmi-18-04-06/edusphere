package com.example.edusphere.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.edusphere.entity.Student;
import com.example.edusphere.exception.StudentNotFoundException;
import com.example.edusphere.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Create a new student
   @Transactional
public Student createStudent(Student student) {
    return studentRepository.save(student);
}

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by ID
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // Update student
    public Student updateStudent(Long id, Student studentDetails) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with ID " + id + " not found"
                        )
                );

        student.setRegistrationNumber(studentDetails.getRegistrationNumber());
        student.setName(studentDetails.getName());
        student.setEmail(studentDetails.getEmail());
        student.setDepartment(studentDetails.getDepartment());
        student.setYear(studentDetails.getYear());
        student.setSection(studentDetails.getSection());

        return studentRepository.save(student);
    }

    // Delete student
    public void deleteStudent(Long id) {

    if (!studentRepository.existsById(id)) {
        throw new StudentNotFoundException(
                "Student with ID " + id + " not found"
        );
    }

    studentRepository.deleteById(id);
}
    // Search student by registration number
    public Optional<Student> getStudentByRegistrationNumber(
            String registrationNumber) {

        return studentRepository
                .findByRegistrationNumber(registrationNumber);
    }

}