package com.hospital.model;

/**
 * Represents a hospital patient.
 */
public class Patient {
    private String id;
    private String name;
    private int age;
    private String gender;
    private String phone;
    private String address;
    private String bloodGroup;

    public Patient(String id, String name, int age, String gender,
                   String phone, String address, String bloodGroup) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.bloodGroup = bloodGroup;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getBloodGroup() { return bloodGroup; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    /** CSV: id,name,age,gender,phone,address,bloodGroup */
    public String toCSV() {
        return escape(id) + "," + escape(name) + "," + age + "," + escape(gender)
                + "," + escape(phone) + "," + escape(address) + "," + escape(bloodGroup);
    }

    public static Patient fromCSV(String line) {
        String[] p = splitCsv(line, 7);
        return new Patient(p[0], p[1], Integer.parseInt(p[2]), p[3], p[4], p[5], p[6]);
    }

    static String escape(String s) {
        if (s == null) return "";
        return s.replace(",", ";").trim();
    }

    static String[] splitCsv(String line, int expected) {
        String[] parts = line.split(",", -1);
        if (parts.length != expected) {
            throw new IllegalArgumentException("Bad CSV line: " + line);
        }
        for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();
        return parts;
    }

    @Override
    public String toString() {
        return String.format("%-8s | %-20s | %3d | %-6s | %-12s | %-4s | %s",
                id, name, age, gender, phone, bloodGroup, address);
    }
}
