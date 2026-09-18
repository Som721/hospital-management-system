package com.hospital.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a doctor with specialization and daily slots.
 */
public class Doctor {
    private String id;
    private String name;
    private String specialization;
    private String phone;
    private double fee;
    private List<String> slots;

    public Doctor(String id, String name, String specialization,
                  String phone, double fee, List<String> slots) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.fee = fee;
        this.slots = new ArrayList<>(slots);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getPhone() { return phone; }
    public double getFee() { return fee; }
    public List<String> getSlots() { return slots; }

    public void setName(String n) { this.name = n; }
    public void setSpecialization(String s) { this.specialization = s; }
    public void setPhone(String p) { this.phone = p; }
    public void setFee(double f) { this.fee = f; }
    public void setSlots(List<String> s) { this.slots = new ArrayList<>(s); }

    /** CSV: id,name,specialization,phone,fee,slot1;slot2;... */
    public String toCSV() {
        return esc(id) + "," + esc(name) + "," + esc(specialization) + ","
                + esc(phone) + "," + fee + "," + String.join(";", slots);
    }

    public static Doctor fromCSV(String line) {
        String[] p = line.split(",", -1);
        if (p.length != 6) throw new IllegalArgumentException("Bad doctor CSV: " + line);
        for (int i = 0; i < p.length; i++) p[i] = p[i].trim();
        List<String> slots = p[5].isEmpty() ? new ArrayList<>()
                : new ArrayList<>(Arrays.asList(p[5].split(";")));
        return new Doctor(p[0], p[1], p[2], p[3], Double.parseDouble(p[4]), slots);
    }

    private static String esc(String s) {
        return s == null ? "" : s.replace(",", ";").trim();
    }

    @Override
    public String toString() {
        return String.format("%-8s | %-20s | %-15s | %-12s | %7.2f | %s",
                id, name, specialization, phone, fee, String.join(" ", slots));
    }
}
