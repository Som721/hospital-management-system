package com.hospital.service;

import com.hospital.model.Doctor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Doctor CRUD + search by specialization.
 */
public class DoctorService {
    private final Map<String, Doctor> doctors = new LinkedHashMap<>();

    public void addDoctor(Doctor d) {
        if (doctors.containsKey(d.getId())) {
            throw new IllegalArgumentException("Doctor ID already exists: " + d.getId());
        }
        doctors.put(d.getId(), d);
    }

    public Doctor getDoctor(String id) {
        return doctors.get(id);
    }

    public List<Doctor> getAll() {
        return new ArrayList<>(doctors.values());
    }

    public List<Doctor> findBySpecialization(String spec) {
        List<Doctor> out = new ArrayList<>();
        for (Doctor d : doctors.values()) {
            if (d.getSpecialization().equalsIgnoreCase(spec.trim())) {
                out.add(d);
            }
        }
        return out;
    }

    public boolean deleteDoctor(String id) {
        return doctors.remove(id) != null;
    }

    public void load(List<Doctor> list) {
        doctors.clear();
        for (Doctor d : list) doctors.put(d.getId(), d);
    }

    public int count() {
        return doctors.size();
    }
}
