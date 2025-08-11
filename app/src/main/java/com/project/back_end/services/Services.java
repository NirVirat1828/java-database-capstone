package com.project.back_end.services;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.repo.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Services {

    public final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Autowired
    public Services(TokenService tokenService,
                    AdminRepository adminRepository,
                    DoctorRepository doctorRepository,
                    PatientRepository patientRepository,
                    AppointmentRepository appointmentRepository,
                    PatientService patientService,
                    DoctorService doctorService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    // 3. validateToken Method
    public ResponseEntity<Object> validateToken(String token, String role) {
        boolean isValid = tokenService.validateToken(token, role);
        if (isValid) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }
    }

    // 4. validateAdmin Method
    public ResponseEntity<Object> validateAdmin(String username, String password) {
        try {
            Admin admin = adminRepository.findByUsername(username);
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
            }
            if (!admin.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password.");
            }
            String token = tokenService.generateToken(admin.getUsername());
            return ResponseEntity.ok().body(token);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during admin validation.");
        }
    }

    // 5. filterDoctor Method
    public List<Doctor> filterDoctor(String name, String specialty, String availableTime) {
        if (name != null && specialty != null) {
            return doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        } else if (name != null) {
            return doctorRepository.findByNameLike("%" + name + "%");
        } else if (specialty != null) {
            return doctorRepository.findBySpecialtyIgnoreCase(specialty);
        } else {
            return doctorRepository.findAll();
        }
    }

    // 6. validateAppointment Method
    public int validateAppointment(Long doctorId, String date, String time) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (!doctorOpt.isPresent()) return -1;

        Doctor doctor = doctorOpt.get();
        List<String> availableTimes = doctor.getAvailableTimes();
        if (availableTimes == null) return 0;

        for (String slot : availableTimes) {
            if (slot.startsWith(time)) {
                return 1;
            }
        }
        return 0;
    }

    // 7. validatePatient Method
    public boolean validatePatient(String email, String phone) {
        Patient patient = patientRepository.findByEmailOrPhone(email, phone);
        return patient == null;
    }

    // 8. validatePatientLogin Method
    public ResponseEntity<Object> validatePatientLogin(String email, String password) {
        try {
            Patient patient = patientRepository.findByEmail(email);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Patient not found.");
            }
            if (!patient.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password.");
            }
            String token = tokenService.generateToken(patient.getEmail());
            return ResponseEntity.ok().body(token);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during patient login.");
        }
    }

    // 9. filterPatient Method
    public Object filterPatient(String token, String condition, String doctorName) {
        String email = tokenService.extractEmail(token);
        Patient patient = patientRepository.findByEmail(email);
        if (patient == null) return null;

        // Assuming PatientService has filtering methods (pseudo-code)
        if (condition != null && doctorName != null) {
            return patientService.filterAppointmentsByConditionAndDoctorName(patient.getId(), condition, doctorName);
        } else if (condition != null) {
            return patientService.filterAppointmentsByCondition(patient.getId(), condition);
        } else if (doctorName != null) {
            return patientService.filterAppointmentsByDoctorName(patient.getId(), doctorName);
        } else {
            return patientService.getAppointmentsByPatientId(patient.getId());
        }
    }
}