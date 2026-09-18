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
import com.hospital.util.Validator;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Dependency-free tests. Run: java -cp out com.hospital.HospitalSystemTest
 * Exit code 0 = all passed.
 */
public class HospitalSystemTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        testValidator();
        testPatientCrud();
        testDoubleBookingBlocked();
        testCancelFreesSlot();
        testBillingTotal();
        testCsvRoundTrip();

        System.out.println("\n==== TEST RESULT: " + passed + " passed, " + failed + " failed ====");
        if (failed > 0) System.exit(1);
    }

    private static void check(boolean cond, String name) {
        if (cond) { passed++; System.out.println("PASS: " + name); }
        else { failed++; System.out.println("FAIL: " + name); }
    }

    private static void testValidator() {
        check(Validator.isPhone("9876543210"), "validator accepts 10-digit phone");
        check(!Validator.isPhone("123"), "validator rejects short phone");
        check(Validator.isDate("2026-09-18"), "validator accepts date");
        check(!Validator.isDate("18-09-2026"), "validator rejects bad date");
        check(Validator.isSlot("10:30"), "validator accepts slot");
        check(!Validator.isSlot("25:00"), "validator rejects bad slot");
    }

    private static void testPatientCrud() {
        PatientService ps = new PatientService();
        ps.addPatient(new Patient("P001", "Aarav", 30, "M", "9876543210", "Vellore", "O+"));
        check(ps.getPatient("P001") != null, "patient add+get");
        check(ps.searchByName("aar").size() == 1, "patient search by name");
        boolean dup = false;
        try {
            ps.addPatient(new Patient("P001", "X", 20, "M", "9999999999", "A", "A+"));
        } catch (IllegalArgumentException e) { dup = true; }
        check(dup, "duplicate patient ID rejected");
    }

    private static PatientService seedPatients() {
        PatientService ps = new PatientService();
        ps.addPatient(new Patient("P1", "A", 25, "M", "1111111111", "Addr", "O+"));
        return ps;
    }

    private static DoctorService seedDoctors() {
        DoctorService ds = new DoctorService();
        ds.addDoctor(new Doctor("D1", "Dr Smith", "Cardiology", "2222222222", 500.0,
                Arrays.asList("10:00", "11:00")));
        return ds;
    }

    private static void testDoubleBookingBlocked() {
        PatientService ps = seedPatients();
        DoctorService ds = seedDoctors();
        AppointmentService as = new AppointmentService();
        as.bookAppointment("P1", "D1", "2026-10-01", "10:00", "checkup", ps, ds);
        boolean blocked = false;
        try {
            as.bookAppointment("P1", "D1", "2026-10-01", "10:00", "again", ps, ds);
        } catch (IllegalStateException e) { blocked = true; }
        check(blocked, "double booking blocked");
        // different slot same day should succeed
        Appointment a2 = as.bookAppointment("P1", "D1", "2026-10-01", "11:00", "follow", ps, ds);
        check(a2 != null, "different slot allowed");
    }

    private static void testCancelFreesSlot() {
        PatientService ps = seedPatients();
        DoctorService ds = seedDoctors();
        AppointmentService as = new AppointmentService();
        Appointment a = as.bookAppointment("P1", "D1", "2026-10-02", "10:00", "x", ps, ds);
        as.cancelAppointment(a.getId());
        boolean ok = false;
        try {
            as.bookAppointment("P1", "D1", "2026-10-02", "10:00", "rebook", ps, ds);
            ok = true;
        } catch (Exception e) { ok = false; }
        check(ok, "cancel frees slot for rebooking");
    }

    private static void testBillingTotal() {
        PatientService ps = seedPatients();
        DoctorService ds = seedDoctors();
        AppointmentService as = new AppointmentService();
        Appointment a = as.bookAppointment("P1", "D1", "2026-10-03", "10:00", "x", ps, ds);
        BillingService bs = new BillingService();
        Bill b = bs.generateBill(a, ds.getDoctor("D1"), 200.0);
        check(Math.abs(b.getTotal() - 700.0) < 0.001, "bill total = fee + extra");
    }

    private static void testCsvRoundTrip() throws Exception {
        String tmp = "/tmp/hms_test_patients.csv";
        PatientService ps = new PatientService();
        ps.addPatient(new Patient("PX", "Test User", 40, "F", "9998887776", "Chennai", "B+"));
        FileStorage.savePatients(tmp, ps.getAll());
        List<Patient> loaded = FileStorage.loadPatients(tmp);
        check(loaded.size() == 1 && loaded.get(0).getName().equals("Test User"), "CSV save/load round-trip");
        Files.deleteIfExists(Paths.get(tmp));
    }
}
