package com.project.back_end.services;

import com.project.back_end.models.Patient;
import com.project.back_end.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient getPatientByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    public Patient getPatientByEmailOrPhone(String email, String phone) {
        return patientRepository.findByEmailOrPhone(email, phone);
    }

    public void deletePatientById(Long id) {
        patientRepository.deleteById(id);
    }

    // Stub/filter methods for demonstration (implement as needed)
    public Object filterAppointmentsByConditionAndDoctorName(Long patientId, String condition, String doctorName) {
        // Implement logic as needed
        return null;
    }
    public Object filterAppointmentsByCondition(Long patientId, String condition) {
        // Implement logic as needed
        return null;
    }
    public Object filterAppointmentsByDoctorName(Long patientId, String doctorName) {
        // Implement logic as needed
        return null;
    }
    public Object getAppointmentsByPatientId(Long patientId) {
        // Implement logic as needed
        return null;
    }
}