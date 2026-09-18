package com.hospital.service;

import com.hospital.model.Appointment;
import com.hospital.model.Bill;
import com.hospital.model.Doctor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Billing: consultation fee (from doctor) + extra charges.
 */
public class BillingService {
    private final List<Bill> bills = new ArrayList<>();
    private final AtomicInteger counter = new AtomicInteger(5000);

    public Bill generateBill(Appointment appt, Doctor doctor, double extraCharges) {
        if (extraCharges < 0) throw new IllegalArgumentException("Extra charges cannot be negative.");
        Bill b = new Bill("B" + counter.getAndIncrement(),
                appt.getId(), doctor.getFee(), extraCharges);
        bills.add(b);
        return b;
    }

    public List<Bill> getAll() {
        return new ArrayList<>(bills);
    }

    public double totalCollection() {
        double sum = 0;
        for (Bill b : bills) sum += b.getTotal();
        return sum;
    }
}
