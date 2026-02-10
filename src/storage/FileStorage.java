package storage;

import model.Customer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    private final String filePath;

    public FileStorage(String filePath) {
        this.filePath = filePath;
    }

    public void saveToFile(List<Customer> customers) {
        Path path = Paths.get(filePath);
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write("id,fullName,phone,email,createdAt");
                writer.newLine();
                for (Customer customer : customers) {
                    writer.write(customer.toCsvLine());
                    writer.newLine();
                }
            }
            System.out.println("Saved " + customers.size() + " customers to " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to save customers: " + e.getMessage());
        }
    }

    public List<Customer> loadFromFile() {
        List<Customer> customers = new ArrayList<>();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return customers;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (firstLine && line.toLowerCase().startsWith("id,")) {
                    firstLine = false;
                    continue;
                }
                firstLine = false;
                List<String> fields = parseCsvLine(line);
                if (fields.size() < 5) {
                    continue;
                }
                int id;
                try {
                    id = Integer.parseInt(fields.get(0).trim());
                } catch (NumberFormatException e) {
                    continue;
                }
                String fullName = fields.get(1);
                String phone = fields.get(2);
                String email = fields.get(3);
                LocalDateTime createdAt;
                try {
                    createdAt = LocalDateTime.parse(fields.get(4), Customer.CSV_FORMAT);
                } catch (Exception e) {
                    createdAt = LocalDateTime.now();
                }
                customers.add(new Customer(id, fullName, phone, email, createdAt));
            }
            System.out.println("Loaded " + customers.size() + " customers from " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to load customers: " + e.getMessage());
        }
        return customers;
    }

    private List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }
        fields.add(current.toString());
        return fields;
    }
}
