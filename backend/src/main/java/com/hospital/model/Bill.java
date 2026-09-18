package com.hospital.model;

import java.time.LocalDate;

/**
 * Simple bill generated for an appointment.
 */
public class Bill {
    private String billId;
    private String appointmentId;
    private double consultationFee;
    private double extraCharges;
    private double total;
    private String date;

    public Bill(String billId, String appointmentId,
                double consultationFee, double extraCharges) {
        this.billId = billId;
        this.appointmentId = appointmentId;
        this.consultationFee = consultationFee;
        this.extraCharges = extraCharges;
        this.total = consultationFee + extraCharges;
        this.date = LocalDate.now().toString();
    }

    public String getBillId() { return billId; }
    public String getAppointmentId() { return appointmentId; }
    public double getConsultationFee() { return consultationFee; }
    public double getExtraCharges() { return extraCharges; }
    public double getTotal() { return total; }
    public String getDate() { return date; }

    public String receipt(String patientName, String doctorName) {
        return "========== HOSPITAL BILL ==========\n"
                + "Bill ID   : " + billId + "\n"
                + "Date      : " + date + "\n"
                + "Patient   : " + patientName + "\n"
                + "Doctor    : " + doctorName + "\n"
                + "Appt ID   : " + appointmentId + "\n"
                + "Consult   : " + String.format("%.2f", consultationFee) + "\n"
                + "Extra     : " + String.format("%.2f", extraCharges) + "\n"
                + "TOTAL     : " + String.format("%.2f", total) + "\n"
                + "=================================";
    }
}
