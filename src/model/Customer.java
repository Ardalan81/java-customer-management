package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Customer {
    private final int id;
    private String fullName;
    private String phone;
    private String email;
    private final LocalDateTime createdAt;

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter CSV_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Customer(int id, String fullName, String phone, String email, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.createdAt = createdAt;
    }

    public Customer(int id, String fullName, String phone, String email) {
        this(id, fullName, phone, email, LocalDateTime.now());
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCreatedAtFormatted() {
        return createdAt.format(DISPLAY_FORMAT);
    }

    public String toCsvLine() {
        return id + "," + escapeCsv(fullName) + "," + escapeCsv(phone) + "," + escapeCsv(email) + "," + createdAt.format(CSV_FORMAT);
    }

    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        boolean needsQuotes = value.contains(",") || value.contains("\"") || value.contains("\n");
        if (!needsQuotes) {
            return value;
        }
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + fullName + " | Phone: " + phone + " | Email: " + email + " | Created: " + getCreatedAtFormatted();
    }
}
