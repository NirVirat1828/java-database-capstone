package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final Services services;

    @Autowired
    public PatientController(PatientService patientService, Services services) {
        this.patientService = patientService;
        this.services = services;
    }

    // 3. Get patient details using token
    @GetMapping("/profile/{token}")
    public ResponseEntity<Object> getPatient(@PathVariable String token) {
        ResponseEntity<Object> validation = services.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }
        String email = services.tokenService.extractEmail(token);
        Patient patient = patientService.getPatientByEmail(email);
        if (patient == null) {
            return ResponseEntity.status(404).body("Patient not found.");
        }
        return ResponseEntity.ok(patient);
    }

    // 4. Create/register patient
    @PostMapping("/register")
    public ResponseEntity<Object> createPatient(@RequestBody Patient patient) {
        boolean isValid = services.validatePatient(patient.getEmail(), patient.getPhone());
        if (!isValid) {
            return ResponseEntity.status(409).body("Patient already exists.");
        }
        try {
            Patient saved = patientService.savePatient(patient);
            return ResponseEntity.ok(saved);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Failed to register patient.");
        }
    }

    // 5. Patient login
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Login login) {
        return services.validatePatientLogin(login.getEmail(), login.getPassword());
    }

    // 6. Get appointments for a patient (by patientId, token, userType)
    @GetMapping("/appointments/{patientId}/{token}/{userType}")
    public ResponseEntity<Object> getPatientAppointment(
            @PathVariable Long patientId,
            @PathVariable String token,
            @PathVariable String userType
    ) {
        ResponseEntity<Object> validation = services.validateToken(token, userType);
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }
        return ResponseEntity.ok(patientService.getAppointmentsByPatientId(patientId));
    }

    // 7. Filter patient appointments (by condition, doctor name, token)
    @GetMapping("/appointments/filter/{condition}/{name}/{token}")
    public ResponseEntity<Object> filterPatientAppointment(
            @PathVariable(required = false) String condition,
            @PathVariable(required = false) String name,
            @PathVariable String token
    ) {
        ResponseEntity<Object> validation = services.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }
        Object result = services.filterPatient(token, condition, name);
        return ResponseEntity.ok(result);
    }
}