package com.project.back_end.services;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrescriptionService {

    private static final Logger logger = LoggerFactory.getLogger(PrescriptionService.class);

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    /**
     * Saves a new prescription for an appointment.
     * Returns 400 if a prescription already exists for the appointment.
     */
    public ResponseEntity<Object> savePrescription(Prescription prescription) {
        try {
            List<Prescription> existingPrescriptions = prescriptionRepository.findByAppointmentId(prescription.getAppointmentId());
            if (existingPrescriptions != null && !existingPrescriptions.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Prescription already exists for this appointment.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            prescriptionRepository.save(prescription);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Prescription saved successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception ex) {
            logger.error("Error saving prescription: {}", ex.getMessage(), ex);
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Failed to save prescription.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves the prescription for a given appointment ID.
     */
    public ResponseEntity<Object> getPrescription(Long appointmentId) {
        try {
            List<Prescription> prescriptions = prescriptionRepository.findByAppointmentId(appointmentId);
            Map<String, Object> response = new HashMap<>();
            if (prescriptions == null || prescriptions.isEmpty()) {
                response.put("message", "No prescription found for this appointment.");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            response.put("prescription", prescriptions.get(0));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception ex) {
            logger.error("Error fetching prescription: {}", ex.getMessage(), ex);
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Failed to fetch prescription.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}