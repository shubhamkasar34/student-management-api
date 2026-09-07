package com.example.crud.service;

import com.example.crud.dto.StudentRequest;
import com.example.crud.dto.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    StudentResponse create(StudentRequest request);

    StudentResponse update(Long id, StudentRequest request);

    Page<StudentResponse> findAll(Pageable pageable);

    StudentResponse findById(Long id);
}