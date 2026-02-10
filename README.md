# Java Customer Management System

**Overview**
A clean, menu-driven Java console app for managing customer records. It supports creating, editing, deleting, searching, sorting, and persisting customers to a CSV file so data is preserved between runs.

**Repository Name**
`java-customer-management`

**How It Works**
1. The app starts by loading `data/customers.csv` (if it exists).
2. You choose actions from the menu (add, view, search, edit, delete, sort, report).
3. Each change is validated and applied to an in-memory list of customers.
4. You can save at any time, and the app also asks to save on exit.

**Features**
- Menu loop until exit
- Add, view, search, edit, delete customers
- Search by ID, name, or phone
- Input validation
- Non-empty names
- Phone digits only
- Email must contain `@`
- Auto-generated unique customer IDs
- Save/load from CSV
- Sort by name or newest
- Report: total customer count
- Confirmation before delete

**Tech**
- Java (console app)
- CSV file storage

**Project Structure**
```
java-customer-management/
  src/
    Main.java
    model/
      Customer.java
    service/
      CustomerService.java
    storage/
      FileStorage.java
    util/
      InputHelper.java
  data/
    customers.csv
  README.md
```

**How To Run**
1. Open a terminal in the project root:
   ```bash
   cd "/Users/ardalanjanpour/Documents/New project/java-customer-management"
   ```
2. Compile:
   ```bash
   javac -d out src/Main.java src/model/Customer.java src/service/CustomerService.java src/storage/FileStorage.java src/util/InputHelper.java
   ```
3. Run:
   ```bash
   java -cp out Main
   ```

**Usage Guide**
- `1` Add customer
- `2` View all customers
- `3` Search customers
- `4` Edit customer
- `5` Delete customer
- `6` Sort customers
- `7` Export report (total count)
- `8` Save to file
- `9` Load from file
- `0` Exit

**Data File Format (CSV)**
- File path: `data/customers.csv`
- Header: `id,fullName,phone,email,createdAt`
- Date format: ISO local date-time (example: `2026-02-10T14:23:11`)

**Validation Rules**
- Name cannot be empty
- Phone must be digits only
- Email must contain `@`

**Example Session**
1. Add a customer
2. View all
3. Search by name or ID
4. Edit a field
5. Save and exit

**Future Improvements**
- Pagination for large lists
- Export to JSON
- Stronger email validation
- Better phone formatting
