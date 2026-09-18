package com.hospital.service;

import com.hospital.model.Patient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Patient CRUD with O(1) lookup by ID.
 */
public class PatientService {
    private final Map<String, Patient> patients = new LinkedHashMap<>();

    public void addPatient(Patient p) {
        if (patients.containsKey(p.getId())) {
            throw new IllegalArgumentException("Patient ID already exists: " + p.getId());
        }
        patients.put(p.getId(), p);
    }

    public Patient getPatient(String id) {
        return patients.get(id);
    }

    public List<Patient> getAll() {
        return new ArrayList<>(patients.values());
    }

    public List<Patient> searchByName(String query) {
        List<Patient> out = new ArrayList<>();
        String q = query.toLowerCase();
        for (Patient p : patients.values()) {
            if (p.getName().toLowerCase().contains(q) || p.getPhone().contains(query)) {
                out.add(p);
            }
        }
        return out;
    }

    public void updatePatient(Patient updated) {
        if (!patients.containsKey(updated.getId())) {
            throw new IllegalArgumentException("Patient not found: " + updated.getId());
        }
        patients.put(updated.getId(), updated);
    }

    /** Returns false if patient does not exist. */
    public boolean deletePatient(String id) {
        return patients.remove(id) != null;
    }

    public void load(List<Patient> list) {
        patients.clear();
        for (Patient p : list) patients.put(p.getId(), p);
    }

    public int count() {
        return patients.size();
    }
}
