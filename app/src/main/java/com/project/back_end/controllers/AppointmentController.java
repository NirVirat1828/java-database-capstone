package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final Services services;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, Services services) {
        this.appointmentService = appointmentService;
        this.services = services;
    }

    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<Object> getAppointments(
            @PathVariable String date,
            @PathVariable String patientName,
            @PathVariable String token) {
        ResponseEntity<Object> validation = services.validateToken(token, "doctor");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }
        // You may want to implement filtering logic in appointmentService as needed
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctorAndPatientNameAndTimeRange(
                null, patientName, null, null // Replace with actual logic
        );
        return ResponseEntity.ok(appointments);
    }

    @PostMapping("/book/{token}")
    public ResponseEntity<Object> bookAppointment(@RequestBody Appointment appointment, @PathVariable String token) {
        ResponseEntity<Object> validation = services.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }
        // Implement appointment validation logic as needed
        Appointment saved = appointmentService.saveAppointment(appointment);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/update/{token}")
    public ResponseEntity<Object> updateAppointment(@RequestBody Appointment appointment, @PathVariable String token) {
        ResponseEntity<Object> validation = services.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }
        // Update logic
        Appointment updated = appointmentService.saveAppointment(appointment);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/cancel/{id}/{token}")
    public ResponseEntity<Object> cancelAppointment(@PathVariable Long id, @PathVariable String token) {
        ResponseEntity<Object> validation = services.validateToken(token, "patient");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }
        appointmentService.deleteAppointmentById(id);
        return ResponseEntity.ok("Appointment cancelled successfully.");
    }
}