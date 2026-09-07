package com.example.crud.service.impl;

import com.example.crud.dto.StudentRequest;
import com.example.crud.dto.StudentResponse;
import com.example.crud.controller.StudentController;
import com.example.crud.entity.Student;
import com.example.crud.exception.ConflictException;
import com.example.crud.exception.ResourceNotFoundException;
import com.example.crud.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.crud.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;

    public StudentServiceImpl(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public StudentResponse create(StudentRequest request) {
        String email = normalizeEmail(request.getEmail());

        if (repository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("Email already exists");
        }

        Student student = new Student(
                request.getName().trim(),
                email,
                request.getCourse().trim()
        );

        return StudentResponse.from(repository.save(student));
    }

    @Override
    public StudentResponse update(Long id, StudentRequest request) {
        Student student = getStudent(id);
        String email = normalizeEmail(request.getEmail());

        if (repository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new ConflictException("Email already exists");
        }

        student.update(
                request.getName().trim(),
                email,
                request.getCourse().trim()
        );

        return StudentResponse.from(student);
    }

    @Override
    public Page<StudentResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(StudentResponse::from);
    }

    @Override
    public StudentResponse findById(Long id) {
        return StudentResponse.from(getStudent(id));
    }
    private Student getStudent(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

}