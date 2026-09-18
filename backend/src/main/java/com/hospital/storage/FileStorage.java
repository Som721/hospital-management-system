package com.hospital.storage;

import com.hospital.model.Appointment;
import com.hospital.model.Doctor;
import com.hospital.model.Patient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV file persistence. No external DB required.
 * Files: database/patients.csv, database/doctors.csv, database/appointments.csv
 */
public final class FileStorage {
    private FileStorage() {}

    public static List<Patient> loadPatients(String path) {
        List<Patient> out = new ArrayList<>();
        for (String line : readLines(path)) {
            if (line.trim().isEmpty() || line.startsWith("id,")) continue;
            try {
                out.add(Patient.fromCSV(line));
            } catch (Exception ignored) {
            }
        }
        return out;
    }

    public static List<Doctor> loadDoctors(String path) {
        List<Doctor> out = new ArrayList<>();
        for (String line : readLines(path)) {
            if (line.trim().isEmpty() || line.startsWith("id,")) continue;
            try {
                out.add(Doctor.fromCSV(line));
            } catch (Exception ignored) {
            }
        }
        return out;
    }

    public static List<Appointment> loadAppointments(String path) {
        List<Appointment> out = new ArrayList<>();
        for (String line : readLines(path)) {
            if (line.trim().isEmpty() || line.startsWith("id,")) continue;
            try {
                out.add(Appointment.fromCSV(line));
            } catch (Exception ignored) {
            }
        }
        return out;
    }

    public static void savePatients(String path, Iterable<Patient> patients) throws IOException {
        StringBuilder sb = new StringBuilder("id,name,age,gender,phone,address,bloodGroup\n");
        for (Patient p : patients) sb.append(p.toCSV()).append("\n");
        writeFile(path, sb.toString());
    }

    public static void saveDoctors(String path, Iterable<Doctor> doctors) throws IOException {
        StringBuilder sb = new StringBuilder("id,name,specialization,phone,fee,slots\n");
        for (Doctor d : doctors) sb.append(d.toCSV()).append("\n");
        writeFile(path, sb.toString());
    }

    public static void saveAppointments(String path, Iterable<Appointment> appts) throws IOException {
        StringBuilder sb = new StringBuilder("id,patientId,doctorId,date,slot,status,reason\n");
        for (Appointment a : appts) sb.append(a.toCSV()).append("\n");
        writeFile(path, sb.toString());
    }

    private static List<String> readLines(String path) {
        try {
            Path p = Paths.get(path);
            if (!Files.exists(p)) return new ArrayList<>();
            return Files.readAllLines(p);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private static void writeFile(String path, String content) throws IOException {
        Path p = Paths.get(path);
        if (p.getParent() != null) Files.createDirectories(p.getParent());
        Files.write(p, content.getBytes());
    }
}
