package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public List<Appointment> getAppointmentsByDoctorAndTimeRange(Long doctorId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);
    }

    public List<Appointment> getAppointmentsByDoctorAndPatientNameAndTimeRange(Long doctorId, String patientName, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(doctorId, patientName, start, end);
    }

    public void deleteAppointmentsByDoctorId(Long doctorId) {
        appointmentRepository.deleteAllByDoctorId(doctorId);
    }

    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getAppointmentsByPatientAndStatusOrdered(Long patientId, int status) {
        return appointmentRepository.findByPatient_IdAndStatusOrderByAppointmentTimeAsc(patientId, status);
    }

    public List<Appointment> filterByDoctorNameAndPatientId(String doctorName, Long patientId) {
        return appointmentRepository.filterByDoctorNameAndPatientId(doctorName, patientId);
    }

    public List<Appointment> filterByDoctorNameAndPatientIdAndStatus(String doctorName, Long patientId, int status) {
        return appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(doctorName, patientId, status);
    }

    public void updateStatus(int status, long id) {
        appointmentRepository.updateStatus(status, id);
    }

    public void deleteAppointmentById(Long id) {
        appointmentRepository.deleteById(id);
    }
}