package service;

import model.Customer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CustomerService {
    private final List<Customer> customers;
    private int nextId;

    public CustomerService(List<Customer> initialCustomers) {
        this.customers = new ArrayList<>();
        if (initialCustomers != null) {
            this.customers.addAll(initialCustomers);
        }
        recalcNextId();
    }

    public void replaceCustomers(List<Customer> newCustomers) {
        customers.clear();
        if (newCustomers != null) {
            customers.addAll(newCustomers);
        }
        recalcNextId();
    }

    public Customer addCustomer(String fullName, String phone, String email) {
        Customer customer = new Customer(nextId++, fullName, phone, email);
        customers.add(customer);
        return customer;
    }

    public boolean updateCustomer(int id, String newFullName, String newPhone, String newEmail) {
        Customer customer = findById(id);
        if (customer == null) {
            return false;
        }
        if (newFullName != null) {
            customer.setFullName(newFullName);
        }
        if (newPhone != null) {
            customer.setPhone(newPhone);
        }
        if (newEmail != null) {
            customer.setEmail(newEmail);
        }
        return true;
    }

    public boolean deleteCustomer(int id) {
        Customer customer = findById(id);
        if (customer == null) {
            return false;
        }
        return customers.remove(customer);
    }

    public Customer findById(int id) {
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        return null;
    }

    public List<Customer> searchByName(String name) {
        List<Customer> results = new ArrayList<>();
        String needle = name.toLowerCase();
        for (Customer customer : customers) {
            if (customer.getFullName().toLowerCase().contains(needle)) {
                results.add(customer);
            }
        }
        return results;
    }

    public List<Customer> searchByPhone(String phone) {
        List<Customer> results = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer.getPhone().contains(phone)) {
                results.add(customer);
            }
        }
        return results;
    }

    public List<Customer> listCustomers() {
        return new ArrayList<>(customers);
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }

    public void sortByName() {
        customers.sort(Comparator.comparing(c -> c.getFullName().toLowerCase()));
    }

    public void sortByNewest() {
        customers.sort(Comparator.comparing(Customer::getCreatedAt).reversed());
    }

    public int getTotalCustomers() {
        return customers.size();
    }

    private void recalcNextId() {
        int maxId = 0;
        for (Customer customer : customers) {
            if (customer.getId() > maxId) {
                maxId = customer.getId();
            }
        }
        nextId = maxId + 1;
    }
}
