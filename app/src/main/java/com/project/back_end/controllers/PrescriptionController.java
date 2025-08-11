package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final Services services;
    private final AppointmentService appointmentService;

    @Autowired
    public PrescriptionController(
            PrescriptionService prescriptionService,
            Services services,
            AppointmentService appointmentService) {
        this.prescriptionService = prescriptionService;
        this.services = services;
        this.appointmentService = appointmentService;
    }

    // 3. Save prescription for an appointment (doctor only)
    @PostMapping("/save/{token}")
    public ResponseEntity<Object> savePrescription(
            @RequestBody Prescription prescription,
            @PathVariable String token) {
        ResponseEntity<Object> validation = services.validateToken(token, "doctor");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }
        // Update appointment status as needed here if required
        // appointmentService.updateStatus(...);
        return prescriptionService.savePrescription(prescription);
    }

    // 4. Get prescription by appointmentId (doctor only)
    @GetMapping("/get/{appointmentId}/{token}")
    public ResponseEntity<Object> getPrescription(
            @PathVariable Long appointmentId,
            @PathVariable String token) {
        ResponseEntity<Object> validation = services.validateToken(token, "doctor");
        if (!validation.getStatusCode().is2xxSuccessful()) {
            return validation;
        }
        return prescriptionService.getPrescription(appointmentId);
    }
}