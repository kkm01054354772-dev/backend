package com.example.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpa.entity.Student;

// DAO
// 기본적인 CRUD 메소드는 이미 정의되어 있음 
public interface StudentRepository extends JpaRepository<Student,Long> {
    // <entity명,id키의 type>
}
