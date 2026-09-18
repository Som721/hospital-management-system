package com.hospital.model;

/**
 * Represents an appointment between a patient and a doctor.
 * Status: BOOKED, CANCELLED, COMPLETED
 */
public class Appointment {
    private String id;
    private String patientId;
    private String doctorId;
    private String date;   // YYYY-MM-DD
    private String slot;   // HH:MM (24h)
    private String status;
    private String reason;

    public Appointment(String id, String patientId, String doctorId,
                       String date, String slot, String status, String reason) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.slot = slot;
        this.status = status;
        this.reason = reason;
    }

    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getDate() { return date; }
    public String getSlot() { return slot; }
    public String getStatus() { return status; }
    public String getReason() { return reason; }

    public void setDate(String d) { this.date = d; }
    public void setSlot(String s) { this.slot = s; }
    public void setStatus(String s) { this.status = s; }

    /** CSV: id,patientId,doctorId,date,slot,status,reason */
    public String toCSV() {
        return esc(id) + "," + esc(patientId) + "," + esc(doctorId) + ","
                + esc(date) + "," + esc(slot) + "," + esc(status) + "," + esc(reason);
    }

    public static Appointment fromCSV(String line) {
        String[] p = line.split(",", -1);
        if (p.length != 7) throw new IllegalArgumentException("Bad appointment CSV: " + line);
        for (int i = 0; i < p.length; i++) p[i] = p[i].trim();
        return new Appointment(p[0], p[1], p[2], p[3], p[4], p[5], p[6]);
    }

    private static String esc(String s) {
        return s == null ? "" : s.replace(",", ";").trim();
    }

    @Override
    public String toString() {
        return String.format("%-8s | Pat:%-8s Doc:%-8s | %s %s | %-9s | %s",
                id, patientId, doctorId, date, slot, status, reason);
    }
}
