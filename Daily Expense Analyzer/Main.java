
import java.util.*;
import java.io.*;

class Expense {
    String id;
    String category;
    double amount;

    Expense(String id, String category, double amount) {
        this.id = id;
        this.category = category;
        this.amount = amount;
    }
}

public class Main {

    static ArrayList<Expense> expenses = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static int nextId = 101;

    // 1. Add Expense
    static void addExpense() {

        System.out.print("Enter category: ");
        String category = sc.nextLine().trim();

        if (category.isEmpty() || category.contains("|")) {
            System.out.println("Invalid category.");
            return;
        }

        System.out.print("Enter amount: ");

        double amount;

        try {
            amount = Double.parseDouble(sc.nextLine());

            if (!Double.isFinite(amount) || amount <= 0) {
                System.out.println("Amount must be greater than zero.");
                return;
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
            return;
        }

        String id = "X" + nextId;
        nextId++;

        Expense e = new Expense(id, category, amount);
        expenses.add(e);

        System.out.println("Expense added. ID: " + id +
                " | " + category + " | " + amount);
    }

    // 2. Display All Expenses
    static void displayAll() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        System.out.println("\n--- All Expenses ---");

        for (Expense e : expenses) {
            System.out.println(e.id + " | " +
                    e.category + " | " + e.amount);
        }
    }

    // 3. Sort by Amount (High to Low)
    static void sortByAmount() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        // Manual Bubble Sort
        for (int i = 0; i < expenses.size() - 1; i++) {

            for (int j = 0; j < expenses.size() - i - 1; j++) {

                if (expenses.get(j).amount <
                        expenses.get(j + 1).amount) {

                    Expense temp = expenses.get(j);

                    expenses.set(j, expenses.get(j + 1));

                    expenses.set(j + 1, temp);
                }
            }
        }

        System.out.println("\nExpenses sorted by amount (High to Low):");
        displayAll();
    }

    // 4. Search by Category
    static void searchByCategory() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        System.out.print("Enter category to search: ");
        String search = sc.nextLine().trim();

        boolean found = false;

        for (Expense e : expenses) {

            if (e.category.equalsIgnoreCase(search)) {

                System.out.println("Found: " + e.id +
                        " | " + e.category + " | " + e.amount);

                found = true;
            }
        }

        if (!found) {
            System.out.println("No expense found in this category.");
        }
    }

    // 5. Calculate Total and Highest Expense
    static void totalExpense() {

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        double total = 0;
        Expense highest = expenses.get(0);

        for (Expense e : expenses) {

            total += e.amount;

            if (e.amount > highest.amount) {
                highest = e;
            }
        }

        System.out.println("Total expense: " + total);

        System.out.println("Highest expense: " +
                highest.category + " (" + highest.amount + ")");
    }

    // 6. Load Data from File
    static void loadData() {

        File file = new File("expenses.txt");

        if (!file.exists()) {
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {

            while (fileScanner.hasNextLine()) {

                String line = fileScanner.nextLine();

                String[] data = line.split("\\|", -1);

                if (data.length != 3) {
                    continue;
                }

                try {
                    String id = data[0];
                    String category = data[1];
                    double amount = Double.parseDouble(data[2]);

                    if (id.matches("X\\d+") &&
                            !category.trim().isEmpty() &&
                            !category.contains("|") &&
                            Double.isFinite(amount) &&
                            amount > 0) {

                        expenses.add(new Expense(id, category, amount));

                        int number = Integer.parseInt(id.substring(1));

                        if (number >= nextId) {
                            nextId = number + 1;
                        }
                    }

                } catch (NumberFormatException ex) {
                    System.out.println("Skipping invalid record: " + line);
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading file.");
        }
    }

    // 7. Save Data to File
    static void saveData() {

        try (PrintWriter writer = new PrintWriter(
                new FileWriter("expenses.txt"))) {

            for (Expense e : expenses) {

                writer.println(e.id + "|" +
                        e.category + "|" + e.amount);
            }

            System.out.println("Data saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    // 8. Main Method
    public static void main(String[] args) {

        loadData();

        int choice = 0;

        do {

            System.out.println("\n===== DAILY EXPENSE ANALYZER =====");

            System.out.println("1. Add Expense");
            System.out.println("2. Display All");
            System.out.println("3. Sort by Amount (High to Low)");
            System.out.println("4. Search by Category");
            System.out.println("5. Total Expense");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");

            try {

                choice = Integer.parseInt(sc.nextLine());

            } catch (NumberFormatException e) {

                System.out.println("Invalid menu choice. Enter a number.");
                continue;
            }

            switch (choice) {

                case 1:
                    addExpense();
                    break;

                case 2:
                    displayAll();
                    break;

                case 3:
                    sortByAmount();
                    break;

                case 4:
                    searchByCategory();
                    break;

                case 5:
                    totalExpense();
                    break;

                case 6:
                    saveData();
                    System.out.println("Thank you for using Daily Expense Analyzer!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 6);

        sc.close();
    }
}
