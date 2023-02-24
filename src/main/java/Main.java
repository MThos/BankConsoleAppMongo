import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final User _USER = new User();

    public static void main(String[] args) {
        System.out.print("Name: ");
        Scanner reader = new Scanner(System.in);
        String name = (reader.next());
        Database.findUser(name, _USER);
        transactionOptions();
    }

    public static void transactionOptions() {
        boolean correctOption = false;
        int option = 0;
        Scanner reader = new Scanner(System.in);

        System.out.println("How can we help you?");
        System.out.println("1. Withdraw");
        System.out.println("2. Deposit");
        System.out.println("3. Check Balance");
        System.out.println("4. Exit");
        System.out.println();
        System.out.print("Option: ");

        do {
            try {
                option = reader.nextInt();
                if (option > 0 && option < 5) {
                    correctOption = true;
                } else {
                    System.out.println("Must be an option between 1-4.");
                }
            } catch (Exception ex) {
                System.out.println("Must be an option between 1-4.");
                System.out.print("Option: ");
                reader.next();
            }
        } while (!correctOption);

        System.out.println();

        if (option == 4) {
            System.out.println("Have a great day, " + _USER.getName());
            System.exit(0);
        } else {
            accountOptions(option);
        }
    }

    public static void accountOptions(int type) {
        boolean correctOption = false;
        int option = 0;
        String typeOfTransaction;
        Scanner reader = new Scanner(System.in);

        if (type == 1) {
            typeOfTransaction = "withdraw from?";
        } else if (type == 2) {
            typeOfTransaction = "deposit to?";
        } else {
            typeOfTransaction = "check the balance of?";
        }

        System.out.println("Which account would you like to " + typeOfTransaction);
        System.out.println("1. Chequing");
        System.out.println("2. Savings");
        System.out.println("3. Back");
        System.out.println();
        System.out.print("Option: ");

        do {
            try {
                option = reader.nextInt();
                if (option > 0 && option < 4) {
                    correctOption = true;
                } else {
                    System.out.println("Must be an option between 1-3.");
                }
            } catch (Exception ex) {
                System.out.println("Must be an option between 1-3.");
                System.out.print("Option: ");
                reader.next();
            }
        } while (!correctOption);

        System.out.println();

        if (option == 1) {
            if ((type == 1)) {
                withdrawal("C");
            } else if (type == 2) {
                deposit("C");
            } else {
                getBalance("C");
            }
        } else if (option == 2) {
            if ((type == 1)) {
                withdrawal("S");
            } else if (type == 2) {
                deposit("S");
            } else {
                getBalance("S");
            }
        } else {
            transactionOptions();
        }
    }

    public static void withdrawal(String accountType) {
        System.out.println("How much would you like to withdraw from your " + getAccountType(accountType) + " account?");
        System.out.println("Balance: " + df.format(Database.getBalance(_USER.getName(), accountType)));
        System.out.print("Amount: ");
        Scanner reader = new Scanner(System.in);
        double amount = reader.nextDouble();

        if (amount == 0) {
            System.out.println();
            System.out.println("You cannot withdraw $0.00");
        } else if (amount > Database.getBalance(_USER.getName(), accountType)) {
            System.out.println();
            System.out.println("You do not have enough in your " + getAccountType(accountType) + " account to withdraw $" + df.format(amount));
        } else {
            Database.withdraw(_USER.getName(), accountType, amount);
            System.out.println();
            System.out.println("You have withdrawn $" + df.format(amount) + " from your " + getAccountType(accountType) + " account.");
        }

        System.out.println();
        transactionOptions();
    }

    public static void deposit(String accountType) {
        System.out.println("How much would you like to deposit in your " + getAccountType(accountType) + " account?");
        System.out.println("Balance: " + df.format(Database.getBalance(_USER.getName(), accountType)));
        System.out.print("Amount: ");
        Scanner reader = new Scanner(System.in);
        double amount = reader.nextDouble();

        if (amount < 0 || amount == 0) {
            System.out.println();
            System.out.println("You cannot deposit $0.00 or less.");
        } else {
            Database.deposit(_USER.getName(), accountType, amount);
            System.out.println();
            System.out.println("You have deposited $" + df.format(amount) + " in your " + getAccountType(accountType) + " account.");
        }

        System.out.println();
        transactionOptions();
    }

    public static void getBalance(String accountType) {
        System.out.println("Your " + getAccountType(accountType) + " balance is currently: $" +
                df.format(Database.getBalance(_USER.getName(), accountType)));

        System.out.println();
        transactionOptions();
    }

    public static String getAccountType(String accountType) {
        return (Objects.equals(accountType, "C")) ? "chequing" : "savings";
    }
}
