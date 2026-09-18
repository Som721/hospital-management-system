package com.hospital.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Minimal file logger. Appends to database/hospital.log.
 * Never throws — logging must not crash the app.
 */
public final class LoggerUtil {
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static String logFile = "database/hospital.log";

    private LoggerUtil() {}

    public static void setLogFile(String path) {
        logFile = path;
    }

    public static synchronized void log(String msg) {
        try (FileWriter fw = new FileWriter(logFile, true)) {
            fw.write(LocalDateTime.now().format(FMT) + " | " + msg + System.lineSeparator());
        } catch (IOException ignored) {
        }
    }
}
