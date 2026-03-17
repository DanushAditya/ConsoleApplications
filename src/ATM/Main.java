package ATM;

import ATM.Account.User;
import ATM.service.ATMVault;
import ATM.service.AdminService;
import ATM.service.UserService;

import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static ATMVault vault = new ATMVault();
    // Initial Login Credentials:
    //  Admin password : 1234
    //  user pin : 1234
    public static void main(String[] args) {
        System.out.println("******* A T M *******");
        System.out.println();
        while (true) {
            System.out.println("** User(1) / Admin(0) **");
            int choice = sc.nextInt();
            System.out.println();
            if (choice == 1) {
                user();
            } else {
                admin();
            }
            System.out.println("Exit ATM ? (1/0)");
            int exit = sc.nextInt();
            if (exit == 1) {
                System.out.println("Thank you for using ATM. Goodbye!");
                break;
            }
            System.out.println();
        }
    }

    public static void user() {
        System.out.println("** User Portal **");
        UserService service = new UserService();
        boolean flag=true;
        while (flag) {
            service.displayAll();
            System.out.println("Enter Account No");
            int acctNo = sc.nextInt();
            User user = service.validAccount(acctNo);
            if (user == null) {
                System.out.println("Invalid Account");
                continue;
            }
            System.out.println("Enter pin");
            int pin = sc.nextInt();
            if (user.getPin() != pin) {
                System.out.println("Incorrect Pin");
                continue;
            }
            System.out.println(user.getName() + " Logged IN");
            cancel: while (true) {
                user.display();
                System.out.println("**********************");
                System.out.println("Enter the Service");
                System.out.println("1. Deposit");
                System.out.println("2. Withdrawl");
                System.out.println("3. Check Balance");
                System.out.println("4. Pin Change");
                System.out.println("5. ATM Cash Info");
                System.out.println("6. Exit");
                int choice = sc.nextInt();
                if (choice == 6) {
                    flag = false;
                    break;
                }
                switch (choice) {
                    case 1:
                        System.out.println("Enter the number of 100's:");
                        int hundred = sc.nextInt();
                        System.out.println("Enter the number of 200's:");
                        int twoHundred = sc.nextInt();
                        System.out.println("Enter the number of 500's:");
                        int fiveHundred = sc.nextInt();
                        int depositAmount = (hundred * 100) + (twoHundred * 200) + (fiveHundred * 500);
                        if (depositAmount > 0) {
                            service.deposit(depositAmount, user);
                        }
                        break;
                    case 2:
                        System.out.println("Enter the amount to Withdraw:");
                        int withdrawlAmount = sc.nextInt();
                        if (withdrawlAmount <= 0) {
                            System.out.println("Amount Less than Zero");
                            break cancel;
                        }
                        service.withdrawl(withdrawlAmount, user, vault);
                        break;
                    case 3:
                        service.checkBalance(user);
                        break;
                    case 4:
                        System.out.println("Enter the new Pin");
                        int newPin = sc.nextInt();
                        service.pinChange(newPin, user);
                        break;
                    case 5:
                        vault.displayVault();
                        break;
                    default:
                        System.out.println("Incorrect choice");
                }
            }
//            System.out.println("Exit ATM ? (1/0)");
//            int exit = sc.nextInt();
//            if (exit == 1)
//                break;
        }
    }

    public static void admin() {
        System.out.println("** Admin Portal **");
        System.out.println("Enter Admin Password");
        int password = sc.nextInt();
        if (password != 1234) {
            System.out.println("Incorrect Password");
            return;
        }
        System.out.println("Admin Logged IN");
        UserService userService = new UserService();
        AdminService adminService = new AdminService(userService.getMap(), vault);
        while (true) {
            System.out.println("**********************");
            System.out.println("Enter the Service");
            System.out.println("1. View All Accounts");
            System.out.println("2. Add Account");
            System.out.println("3. Delete Account");
            System.out.println("4. View Account Details");
            System.out.println("5. Reset User Pin");
            System.out.println("6. Load Cash into ATM");
            System.out.println("7. View ATM Cash");
            System.out.println("8. Exit");
            int choice = sc.nextInt();
            if (choice == 8)
                break;
            switch (choice) {
                case 1:
                    adminService.displayAll();
                    break;
                case 2:
                    System.out.println("Enter Account No");
                    int acctNo = sc.nextInt();
                    System.out.println("Enter Name");
                    String name = sc.next();
                    System.out.println("Enter Initial Balance");
                    int balance = sc.nextInt();
                    System.out.println("Enter Pin");
                    int pin = sc.nextInt();
                    adminService.addAccount(acctNo, name, balance, pin);
                    break;
                case 3:
                    System.out.println("Enter Account No to Delete");
                    int delAcctNo = sc.nextInt();
                    adminService.deleteAccount(delAcctNo);
                    break;
                case 4:
                    System.out.println("Enter Account No to View");
                    int viewAcctNo = sc.nextInt();
                    adminService.viewAccount(viewAcctNo);
                    break;
                case 5:
                    System.out.println("Enter Account No");
                    int resetAcctNo = sc.nextInt();
                    System.out.println("Enter New Pin");
                    int newPin = sc.nextInt();
                    adminService.resetPin(resetAcctNo, newPin);
                    break;
                case 6:
                    System.out.println("Enter number of 100's to Load:");
                    int loadHundred = sc.nextInt();
                    System.out.println("Enter number of 200's to Load:");
                    int loadTwoHundred = sc.nextInt();
                    System.out.println("Enter number of 500's to Load:");
                    int loadFiveHundred = sc.nextInt();
                    adminService.loadCash(loadHundred, loadTwoHundred, loadFiveHundred);
                    break;
                case 7:
                    adminService.displayVault();
                    break;
                default:
                    System.out.println("Incorrect choice");
            }
        }
        System.out.println("Admin Logged OUT");
    }
}