package com.hospital.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Central input validation helpers.
 */
public final class Validator {
    private Validator() {}

    public static boolean isNonEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static boolean isPhone(String s) {
        return s != null && s.trim().matches("\\d{10}");
    }

    public static boolean isAge(String s) {
        try {
            int a = Integer.parseInt(s.trim());
            return a >= 0 && a <= 120;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDate(String s) {
        try {
            LocalDate.parse(s.trim());
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /** HH:MM 24-hour format */
    public static boolean isSlot(String s) {
        return s != null && s.trim().matches("([01]\\d|2[0-3]):[0-5]\\d");
    }

    public static boolean isFee(String s) {
        try {
            return Double.parseDouble(s.trim()) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
