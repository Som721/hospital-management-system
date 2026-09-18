package com.hospital.service;

import com.hospital.model.Appointment;
import com.hospital.util.Validator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Appointment booking with conflict detection.
 * Rule: same doctor + same date + same slot cannot be double-booked.
 */
public class AppointmentService {
    private final Map<String, Appointment> appointments = new LinkedHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(1000);

    public Appointment bookAppointment(String patientId, String doctorId,
                                       String date, String slot, String reason,
                                       PatientService ps, DoctorService ds) {
        if (ps.getPatient(patientId) == null) {
            throw new IllegalArgumentException("Patient not found: " + patientId);
        }
        if (ds.getDoctor(doctorId) == null) {
            throw new IllegalArgumentException("Doctor not found: " + doctorId);
        }
        if (!Validator.isDate(date)) {
            throw new IllegalArgumentException("Invalid date. Use YYYY-MM-DD.");
        }
        if (!Validator.isSlot(slot)) {
            throw new IllegalArgumentException("Invalid slot. Use HH:MM (24h).");
        }
        if (hasConflict(doctorId, date, slot)) {
            throw new IllegalStateException(
                    "Slot already booked for doctor " + doctorId + " on " + date + " " + slot);
        }
        String id = "A" + counter.getAndIncrement();
        Appointment a = new Appointment(id, patientId, doctorId, date, slot, "BOOKED", reason);
        appointments.put(id, a);
        return a;
    }

    /** Used when loading from CSV (IDs already exist). */
    public void addLoaded(Appointment a) {
        appointments.put(a.getId(), a);
        // keep counter ahead of max numeric ID to avoid collisions
        try {
            int n = Integer.parseInt(a.getId().replaceAll("\\D", ""));
            counter.updateAndGet(x -> Math.max(x, n + 1));
        } catch (NumberFormatException ignored) {
        }
    }

    public boolean hasConflict(String doctorId, String date, String slot) {
        for (Appointment a : appointments.values()) {
            if (a.getStatus().equals("BOOKED")
                    && a.getDoctorId().equals(doctorId)
                    && a.getDate().equals(date)
                    && a.getSlot().equals(slot)) {
                return true;
            }
        }
        return false;
    }

    public boolean cancelAppointment(String id) {
        Appointment a = appointments.get(id);
        if (a == null) return false;
        a.setStatus("CANCELLED");
        return true;
    }

    public void reschedule(String id, String newDate, String newSlot) {
        Appointment a = appointments.get(id);
        if (a == null) throw new IllegalArgumentException("Appointment not found: " + id);
        if (!Validator.isDate(newDate) || !Validator.isSlot(newSlot)) {
            throw new IllegalArgumentException("Invalid date/slot format.");
        }
        // temporarily ignore self when checking conflict
        for (Appointment other : appointments.values()) {
            if (other == a) continue;
            if (other.getStatus().equals("BOOKED")
                    && other.getDoctorId().equals(a.getDoctorId())
                    && other.getDate().equals(newDate)
                    && other.getSlot().equals(newSlot)) {
                throw new IllegalStateException("New slot already booked.");
            }
        }
        a.setDate(newDate);
        a.setSlot(newSlot);
        a.setStatus("BOOKED");
    }

    public void markCompleted(String id) {
        Appointment a = appointments.get(id);
        if (a == null) throw new IllegalArgumentException("Appointment not found: " + id);
        a.setStatus("COMPLETED");
    }

    public Appointment get(String id) {
        return appointments.get(id);
    }

    public List<Appointment> getAll() {
        return new ArrayList<>(appointments.values());
    }

    public List<Appointment> getByDate(String date) {
        List<Appointment> out = new ArrayList<>();
        for (Appointment a : appointments.values()) {
            if (a.getDate().equals(date)) out.add(a);
        }
        return out;
    }

    public List<Appointment> getByDoctor(String doctorId) {
        List<Appointment> out = new ArrayList<>();
        for (Appointment a : appointments.values()) {
            if (a.getDoctorId().equals(doctorId)) out.add(a);
        }
        return out;
    }

    public List<Appointment> getByPatient(String patientId) {
        List<Appointment> out = new ArrayList<>();
        for (Appointment a : appointments.values()) {
            if (a.getPatientId().equals(patientId)) out.add(a);
        }
        return out;
    }

    /** True if patient has any BOOKED appointment (used to block unsafe deletes). */
    public boolean hasActiveForPatient(String patientId) {
        for (Appointment a : appointments.values()) {
            if (a.getPatientId().equals(patientId) && a.getStatus().equals("BOOKED")) return true;
        }
        return false;
    }
}
