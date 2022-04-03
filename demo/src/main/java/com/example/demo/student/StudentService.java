package com.example.demo.student;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudent() {
        return studentRepository.findAll();
    }

	public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository
                .findStudentByEmail(student.getEmail());

        if(studentOptional.isPresent()){
          throw new IllegalStateException("email already exists");  
        }
        studentRepository.save(student);
	}

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if(!exists){
            throw new IllegalStateException(
                "Student with Id " + studentId + " does not exist");
        }
        studentRepository.deleteById(studentId);        
    }

    @Transactional // entity goes into managed state (no need to use repo)
    public void updateStudent(Long studentId, String name, String email) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                    "Student with id " + studentId + " does not exist"
                ));

        if(name != null &&
          name.length() > 0 &&
          !Objects.equals(student.getName(), name)){ //name provided doesnt already exist

            student.setName(name);
        }

        if(email != null &&
        email.length() > 0 &&
        !Objects.equals(student.getEmail(), email)){ //name provided doesnt already exist

            Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
            if(studentOptional.isPresent()){
                throw new IllegalStateException("email already exists");
            } 
            student.setEmail(email);
      }
    }
    
}
