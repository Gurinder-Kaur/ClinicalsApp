package com.gurinder.clinicals.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gurinder.clinicals.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

}
