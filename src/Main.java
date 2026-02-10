import model.Customer;
import service.CustomerService;
import storage.FileStorage;
import util.InputHelper;

import java.util.List;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    private static final String DATA_FILE = "data/customers.csv";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InputHelper input = new InputHelper(scanner);
        FileStorage storage = new FileStorage(DATA_FILE);
        CustomerService service = new CustomerService(storage.loadFromFile());

        boolean running = true;
        while (running) {
            printMenu();
            int choice = input.readIntInRange("Choose an option: ", 0, 9);
            System.out.println();
            switch (choice) {
                case 1:
                    handleAddCustomer(input, service, storage);
                    break;
                case 2:
                    handleViewAll(service);
                    break;
                case 3:
                    handleSearch(input, service);
                    break;
                case 4:
                    handleEditCustomer(input, service, storage);
                    break;
                case 5:
                    handleDeleteCustomer(input, service, storage);
                    break;
                case 6:
                    handleSort(input, service);
                    break;
                case 7:
                    handleReport(service);
                    break;
                case 8:
                    storage.saveToFile(service.getAllCustomers());
                    break;
                case 9:
                    handleLoad(input, service, storage);
                    break;
                case 0:
                    if (input.readYesNo("Save before exit? (Y/N): ")) {
                        storage.saveToFile(service.getAllCustomers());
                    }
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("=== Customer Management System ===");
        System.out.println("1. Add customer");
        System.out.println("2. View all customers");
        System.out.println("3. Search customers");
        System.out.println("4. Edit customer");
        System.out.println("5. Delete customer");
        System.out.println("6. Sort customers");
        System.out.println("7. Export report (total count)");
        System.out.println("8. Save to file");
        System.out.println("9. Load from file");
        System.out.println("0. Exit");
    }

    private static void handleAddCustomer(InputHelper input, CustomerService service, FileStorage storage) {
        String fullName = input.readNonEmptyString("Full name: ");
        String phone = input.readPhoneNumber("Phone (digits only): ");
        String email = input.readEmail("Email: ");
        Customer customer = service.addCustomer(fullName, phone, email);
        System.out.println("Customer added with ID: " + customer.getId());
        storage.saveToFile(service.getAllCustomers());
    }

    private static void handleViewAll(CustomerService service) {
        List<Customer> customers = service.listCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }
        printCustomers(customers);
        System.out.println("Total customers: " + customers.size());
    }

    private static void handleSearch(InputHelper input, CustomerService service) {
        System.out.println("Search by:");
        System.out.println("1. ID");
        System.out.println("2. Name");
        System.out.println("3. Phone");
        System.out.println("0. Back");
        int choice = input.readIntInRange("Choose an option: ", 0, 3);
        if (choice == 0) {
            return;
        }
        if (choice == 1) {
            int id = input.readInt("Enter ID: ");
            Customer customer = service.findById(id);
            if (customer == null) {
                System.out.println("Customer not found.");
            } else {
                printCustomers(Collections.singletonList(customer));
            }
        } else if (choice == 2) {
            String name = input.readNonEmptyString("Enter name: ");
            List<Customer> results = service.searchByName(name);
            if (results.isEmpty()) {
                System.out.println("No matching customers found.");
            } else {
                printCustomers(results);
            }
        } else if (choice == 3) {
            String phone = input.readPhoneNumber("Enter phone digits: ");
            List<Customer> results = service.searchByPhone(phone);
            if (results.isEmpty()) {
                System.out.println("No matching customers found.");
            } else {
                printCustomers(results);
            }
        }
    }

    private static void handleEditCustomer(InputHelper input, CustomerService service, FileStorage storage) {
        int id = input.readInt("Enter customer ID to edit: ");
        Customer customer = service.findById(id);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.println("Leave blank to keep the current value.");
        String newName = readOptionalName(input, customer.getFullName());
        String newPhone = readOptionalPhone(input, customer.getPhone());
        String newEmail = readOptionalEmail(input, customer.getEmail());

        boolean updated = service.updateCustomer(id, newName, newPhone, newEmail);
        if (updated) {
            System.out.println("Customer updated.");
            storage.saveToFile(service.getAllCustomers());
        }
    }

    private static void handleDeleteCustomer(InputHelper input, CustomerService service, FileStorage storage) {
        int id = input.readInt("Enter customer ID to delete: ");
        Customer customer = service.findById(id);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }
        System.out.println("Found: " + customer);
        if (input.readYesNo("Are you sure you want to delete? (Y/N): ")) {
            boolean deleted = service.deleteCustomer(id);
            if (deleted) {
                System.out.println("Customer deleted.");
                storage.saveToFile(service.getAllCustomers());
            }
        } else {
            System.out.println("Delete canceled.");
        }
    }

    private static void handleSort(InputHelper input, CustomerService service) {
        System.out.println("Sort by:");
        System.out.println("1. Name (A-Z)");
        System.out.println("2. Newest first");
        System.out.println("0. Back");
        int choice = input.readIntInRange("Choose an option: ", 0, 2);
        if (choice == 1) {
            service.sortByName();
            System.out.println("Sorted by name.");
            printCustomers(service.listCustomers());
        } else if (choice == 2) {
            service.sortByNewest();
            System.out.println("Sorted by newest.");
            printCustomers(service.listCustomers());
        }
    }

    private static void handleReport(CustomerService service) {
        int total = service.getTotalCustomers();
        System.out.println("Total customers: " + total);
    }

    private static void handleLoad(InputHelper input, CustomerService service, FileStorage storage) {
        if (!input.readYesNo("Loading will replace current customers. Continue? (Y/N): ")) {
            return;
        }
        List<Customer> loaded = storage.loadFromFile();
        service.replaceCustomers(loaded);
    }

    private static void printCustomers(List<Customer> customers) {
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    private static String readOptionalName(InputHelper input, String currentValue) {
        while (true) {
            String raw = input.readLine("Full name [" + currentValue + "]: ");
            if (raw.isEmpty()) {
                return null;
            }
            String trimmed = raw.trim();
            if (!trimmed.isEmpty()) {
                return trimmed;
            }
            System.out.println("Name cannot be empty.");
        }
    }

    private static String readOptionalPhone(InputHelper input, String currentValue) {
        while (true) {
            String raw = input.readLine("Phone [" + currentValue + "]: ");
            if (raw.isEmpty()) {
                return null;
            }
            String trimmed = raw.trim();
            if (trimmed.matches("\\d+")) {
                return trimmed;
            }
            System.out.println("Phone must contain digits only.");
        }
    }

    private static String readOptionalEmail(InputHelper input, String currentValue) {
        while (true) {
            String raw = input.readLine("Email [" + currentValue + "]: ");
            if (raw.isEmpty()) {
                return null;
            }
            String trimmed = raw.trim();
            if (trimmed.contains("@") && !trimmed.startsWith("@") && !trimmed.endsWith("@")) {
                return trimmed;
            }
            System.out.println("Email must contain '@'.");
        }
    }
}
