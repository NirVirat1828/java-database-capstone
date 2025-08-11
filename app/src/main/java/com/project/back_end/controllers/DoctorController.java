package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.DTO.Login;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final Services services;

    @Autowired
    public DoctorController(DoctorService doctorService, Services services) {
        this.doctorService = doctorService;
        this.services = services;
    }

    @GetMapping("/availability/{userType}/{doctorId}/{date}/{token}")
    public ResponseEntity<Object> getDoctorAvailability(
            @PathVariable String userType,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token) {
        ResponseEntity<Object> validation = services.validateToken(token, userType);
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }
        // Implement availability logic as needed
        Map<String, Object> response = new HashMap<>();
        response.put("available", true); // Placeholder
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<Object> getDoctor() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctors);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/{token}")
    public ResponseEntity<Object> saveDoctor(@RequestBody Doctor doctor, @PathVariable String token) {
        ResponseEntity<Object> validation = services.validateToken(token, "admin");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }
        Doctor existing = doctorService.getDoctorByEmail(doctor.getEmail());
        if (existing != null) {
            return ResponseEntity.status(409).body("Doctor already exists.");
        }
        Doctor saved = doctorService.saveDoctor(doctor);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> doctorLogin(@RequestBody Login login) {
        // Implement login logic in doctorService
        Doctor doctor = doctorService.getDoctorByEmail(login.getEmail());
        if (doctor == null || !doctor.getPassword().equals(login.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials.");
        }
        // Create and return token as needed
        Map<String, Object> result = new HashMap<>();
        result.put("token", "mocked-token"); // Replace with actual token generation
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update/{token}")
    public ResponseEntity<Object> updateDoctor(@RequestBody Doctor doctor, @PathVariable String token) {
        ResponseEntity<Object> validation = services.validateToken(token, "admin");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }
        Doctor existing = doctorService.getDoctorByEmail(doctor.getEmail());
        if (existing == null) {
            return ResponseEntity.status(404).body("Doctor not found.");
        }
        Doctor updated = doctorService.saveDoctor(doctor);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}/{token}")
    public ResponseEntity<Object> deleteDoctor(@PathVariable Long id, @PathVariable String token) {
        ResponseEntity<Object> validation = services.validateToken(token, "admin");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }
        doctorService.deleteDoctorById(id);
        return ResponseEntity.ok("Doctor deleted successfully.");
    }

    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Object> filter(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {
        List<Doctor> filteredDoctors = services.filterDoctor(name, speciality, time);
        Map<String, Object> response = new HashMap<>();
        response.put("doctors", filteredDoctors);
        return ResponseEntity.ok(response);
    }
}