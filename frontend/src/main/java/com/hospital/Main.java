package com.hospital;

import com.hospital.model.Appointment;
import com.hospital.model.Bill;
import com.hospital.model.Doctor;
import com.hospital.model.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.BillingService;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import com.hospital.storage.FileStorage;
import com.hospital.util.LoggerUtil;
import com.hospital.util.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Console entry point for Hospital Management System.
 * Run: java -cp out com.hospital.Main
 */
public class Main {
    private static final String PATIENT_FILE = "database/patients.csv";
    private static final String DOCTOR_FILE = "database/doctors.csv";
    private static final String APPT_FILE = "database/appointments.csv";

    private final PatientService patients = new PatientService();
    private final DoctorService doctors = new DoctorService();
    private final AppointmentService appointments = new AppointmentService();
    private final BillingService billing = new BillingService();
    private final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        loadAll();
        System.out.println("===========================================");
        System.out.println("  HOSPITAL MANAGEMENT SYSTEM (Java + CSV)");
        System.out.println("===========================================");
        boolean exit = false;
        while (!exit) {
            printMenu();
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": patientMenu(); break;
                    case "2": doctorMenu(); break;
                    case "3": appointmentMenu(); break;
                    case "4": billingMenu(); break;
                    case "5": reportsMenu(); break;
                    case "0": exit = true; break;
                    default: System.out.println("Invalid choice. Try 0-5.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                LoggerUtil.log("ERROR " + e.getMessage());
            }
            saveAll();
        }
        saveAll();
        System.out.println("Data saved. Goodbye!");
    }

    private void printMenu() {
        System.out.println("\n----- MAIN MENU -----");
        System.out.println("1. Patient Management");
        System.out.println("2. Doctor Management");
        System.out.println("3. Appointment Scheduling");
        System.out.println("4. Billing");
        System.out.println("5. Reports");
        System.out.println("0. Exit & Save");
        System.out.print("Enter choice: ");
    }

    // ---------- Patient ----------
    private void patientMenu() {
        System.out.println("\n-- Patient: 1.Add 2.View All 3.Search 4.Update 5.Delete 0.Back --");
        System.out.print("Choice: ");
        String c = sc.nextLine().trim();
        switch (c) {
            case "1": addPatient(); break;
            case "2":
                System.out.println("ID       | Name                 | Age | Gender | Phone        | BG   | Address");
                for (Patient p : patients.getAll()) System.out.println(p);
                System.out.println("Total: " + patients.count());
                break;
            case "3":
                System.out.print("Enter name/phone keyword: ");
                List<Patient> r = patients.searchByName(sc.nextLine().trim());
                if (r.isEmpty()) System.out.println("No matches.");
                else r.forEach(System.out::println);
                break;
            case "4": updatePatient(); break;
            case "5": {
                System.out.print("Patient ID to delete: ");
                String id = sc.nextLine().trim();
                if (appointments.hasActiveForPatient(id)) {
                    System.out.println("Cannot delete: patient has active BOOKED appointments.");
                    break;
                }
                System.out.println(patients.deletePatient(id) ? "Deleted." : "Not found.");
                LoggerUtil.log("DELETE patient " + id);
                break;
            }
            default: break;
        }
    }

    private void addPatient() {
        System.out.print("ID (e.g. P001): ");
        String id = sc.nextLine().trim();
        if (!Validator.isNonEmpty(id)) throw new IllegalArgumentException("ID required.");
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Age: ");
        String ageS = sc.nextLine().trim();
        System.out.print("Gender (M/F/Other): ");
        String gender = sc.nextLine().trim();
        System.out.print("Phone (10 digits): ");
        String phone = sc.nextLine().trim();
        System.out.print("Address: ");
        String address = sc.nextLine().trim();
        System.out.print("Blood group (e.g. O+): ");
        String bg = sc.nextLine().trim();
        if (!Validator.isNonEmpty(name)) throw new IllegalArgumentException("Name required.");
        if (!Validator.isAge(ageS)) throw new IllegalArgumentException("Invalid age (0-120).");
        if (!Validator.isPhone(phone)) throw new IllegalArgumentException("Phone must be 10 digits.");
        patients.addPatient(new Patient(id, name, Integer.parseInt(ageS),
                gender, phone, address, bg));
        LoggerUtil.log("ADD patient " + id);
        System.out.println("Patient added.");
    }

    private void updatePatient() {
        System.out.print("Patient ID to update: ");
        String id = sc.nextLine().trim();
        Patient p = patients.getPatient(id);
        if (p == null) { System.out.println("Not found."); return; }
        System.out.print("New phone [" + p.getPhone() + "]: ");
        String phone = sc.nextLine().trim();
        if (!phone.isEmpty()) {
            if (!Validator.isPhone(phone)) throw new IllegalArgumentException("Phone must be 10 digits.");
            p.setPhone(phone);
        }
        System.out.print("New address [" + p.getAddress() + "]: ");
        String addr = sc.nextLine().trim();
        if (!addr.isEmpty()) p.setAddress(addr);
        patients.updatePatient(p);
        LoggerUtil.log("UPDATE patient " + id);
        System.out.println("Updated.");
    }

    // ---------- Doctor ----------
    private void doctorMenu() {
        System.out.println("\n-- Doctor: 1.Add 2.View All 3.Search by Specialization 0.Back --");
        System.out.print("Choice: ");
        String c = sc.nextLine().trim();
        switch (c) {
            case "1": addDoctor(); break;
            case "2":
                System.out.println("ID       | Name                 | Specialization  | Phone        | Fee     | Slots");
                for (Doctor d : doctors.getAll()) System.out.println(d);
                break;
            case "3": {
                System.out.print("Specialization: ");
                List<Doctor> r = doctors.findBySpecialization(sc.nextLine().trim());
                if (r.isEmpty()) System.out.println("No doctors found.");
                else r.forEach(System.out::println);
                break;
            }
            default: break;
        }
    }

    private void addDoctor() {
        System.out.print("ID (e.g. D001): ");
        String id = sc.nextLine().trim();
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Specialization: ");
        String spec = sc.nextLine().trim();
        System.out.print("Phone (10 digits): ");
        String phone = sc.nextLine().trim();
        System.out.print("Consultation fee: ");
        String feeS = sc.nextLine().trim();
        System.out.print("Slots (comma separated HH:MM, e.g. 10:00,11:00): ");
        String slotsS = sc.nextLine().trim();
        if (!Validator.isNonEmpty(name) || !Validator.isNonEmpty(spec)) {
            throw new IllegalArgumentException("Name/specialization required.");
        }
        if (!Validator.isPhone(phone)) throw new IllegalArgumentException("Phone must be 10 digits.");
        if (!Validator.isFee(feeS)) throw new IllegalArgumentException("Invalid fee.");
        List<String> slots = Arrays.stream(slotsS.split(","))
                .map(String::trim).filter(s -> !s.isEmpty()).toList();
        for (String s : slots) {
            if (!Validator.isSlot(s)) throw new IllegalArgumentException("Bad slot: " + s);
        }
        doctors.addDoctor(new Doctor(id, name, spec, phone, Double.parseDouble(feeS), slots));
        LoggerUtil.log("ADD doctor " + id);
        System.out.println("Doctor added.");
    }

    // ---------- Appointment ----------
    private void appointmentMenu() {
        System.out.println("\n-- Appointment: 1.Book 2.View All 3.By Date 4.By Doctor 5.Cancel 6.Reschedule 7.Complete 0.Back --");
        System.out.print("Choice: ");
        String c = sc.nextLine().trim();
        switch (c) {
            case "1": {
                System.out.print("Patient ID: ");
                String pid = sc.nextLine().trim();
                System.out.print("Doctor ID: ");
                String did = sc.nextLine().trim();
                System.out.print("Date (YYYY-MM-DD): ");
                String date = sc.nextLine().trim();
                System.out.print("Slot (HH:MM): ");
                String slot = sc.nextLine().trim();
                System.out.print("Reason: ");
                String reason = sc.nextLine().trim();
                Appointment a = appointments.bookAppointment(pid, did, date, slot, reason, patients, doctors);
                LoggerUtil.log("BOOK " + a.getId());
                System.out.println("Booked: " + a);
                break;
            }
            case "2": appointments.getAll().forEach(System.out::println); break;
            case "3": {
                System.out.print("Date (YYYY-MM-DD): ");
                appointments.getByDate(sc.nextLine().trim()).forEach(System.out::println);
                break;
            }
            case "4": {
                System.out.print("Doctor ID: ");
                appointments.getByDoctor(sc.nextLine().trim()).forEach(System.out::println);
                break;
            }
            case "5": {
                System.out.print("Appointment ID to cancel: ");
                System.out.println(appointments.cancelAppointment(sc.nextLine().trim()) ? "Cancelled." : "Not found.");
                break;
            }
            case "6": {
                System.out.print("Appointment ID: ");
                String id = sc.nextLine().trim();
                System.out.print("New date: ");
                String d = sc.nextLine().trim();
                System.out.print("New slot: ");
                String s = sc.nextLine().trim();
                appointments.reschedule(id, d, s);
                System.out.println("Rescheduled.");
                break;
            }
            case "7": {
                System.out.print("Appointment ID to complete: ");
                appointments.markCompleted(sc.nextLine().trim());
                System.out.println("Marked COMPLETED.");
                break;
            }
            default: break;
        }
    }

    // ---------- Billing ----------
    private void billingMenu() {
        System.out.print("Appointment ID for billing: ");
        String aid = sc.nextLine().trim();
        Appointment a = appointments.get(aid);
        if (a == null) { System.out.println("Not found."); return; }
        Doctor d = doctors.getDoctor(a.getDoctorId());
        if (d == null) { System.out.println("Doctor record missing."); return; }
        System.out.print("Extra charges (tests/medicine, 0 if none): ");
        double extra;
        try {
            extra = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount.");
        }
        Bill b = billing.generateBill(a, d, extra);
        Patient p = patients.getPatient(a.getPatientId());
        System.out.println("\n" + b.receipt(
                p == null ? a.getPatientId() : p.getName(), d.getName()));
        LoggerUtil.log("BILL " + b.getBillId() + " total=" + b.getTotal());
    }

    private void reportsMenu() {
        System.out.println("\n-- Reports --");
        System.out.println("Patients     : " + patients.count());
        System.out.println("Doctors      : " + doctors.count());
        System.out.println("Appointments : " + appointments.getAll().size());
        System.out.println("Bills raised : " + billing.getAll().size());
        System.out.println("Collection   : " + String.format("%.2f", billing.totalCollection()));
    }

    private void loadAll() {
        patients.load(FileStorage.loadPatients(PATIENT_FILE));
        doctors.load(FileStorage.loadDoctors(DOCTOR_FILE));
        for (Appointment a : FileStorage.loadAppointments(APPT_FILE)) {
            appointments.addLoaded(a);
        }
        LoggerUtil.log("STARTUP patients=" + patients.count()
                + " doctors=" + doctors.count()
                + " appointments=" + appointments.getAll().size());
    }

    private void saveAll() {
        try {
            FileStorage.savePatients(PATIENT_FILE, patients.getAll());
            FileStorage.saveDoctors(DOCTOR_FILE, doctors.getAll());
            FileStorage.saveAppointments(APPT_FILE, appointments.getAll());
        } catch (Exception e) {
            System.out.println("Warning: could not save data: " + e.getMessage());
        }
    }
}
