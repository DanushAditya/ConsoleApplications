package ATM.service;

import ATM.Account.User;

import java.util.HashMap;

public class UserService {
    HashMap<Integer, User> map = new HashMap<>();

    public UserService() {
        map.put(1001, new User("Danush", 1001, 1000, 1234));
        map.put(1002, new User("Ravi", 1002, 2000, 1234));
        map.put(1003, new User("Arun", 1003, 1500, 1234));
    }

    public void displayAll() {
        System.out.println("Accounts Available");
        for (int i : map.keySet()) {
            User user = map.get(i);
            System.out.print(user.getAcctNo() + "  ");
        }
        System.out.println();
    }

    public User validAccount(int acctNo) {
        return map.get(acctNo);
    }

    public void deposit(int amt, User user) {
        System.out.println("Depositing amount: " + amt);
        int newBalance = user.getBalance() + amt;
        user.setBalance(newBalance);
        System.out.println("Deposit Successfull");
        System.out.println("Balance : " + user.getBalance());
    }

    public void withdrawl(int amount, User user, ATMVault vault) {
        if (amount <= 0) {
            System.out.println("Amount Less than Zero");
            return;
        }
        if (amount % 100 != 0) {
            System.out.println("Amount must be in multiples of 100, 200 or 500");
            return;
        }
        if (amount > user.getBalance()) {
            System.out.println("Insufficient Balance in your Account");
            return;
        }
        int[] notes = vault.calculateDispense(amount);
        if (notes == null) {
            System.out.println("ATM does not have sufficient cash to dispense this amount");
            vault.displayVault();
            return;
        }
        user.setBalance(user.getBalance() - amount);
        vault.dispense(notes[0], notes[1], notes[2]);
        System.out.println("Withdrawl Successfull");
        System.out.println("Dispensing : 100s=" + notes[0] + "  200s=" + notes[1] + "  500s=" + notes[2]);
        System.out.println("Balance : " + user.getBalance());
    }

    public void checkBalance(User user) {
        System.out.println(user.getBalance());
    }

    public void pinChange(int pin, User user) {
        user.setPin(pin);
        System.out.println("Pin Changed Suscessfully");
    }

    public HashMap<Integer, User> getMap() {
        return map;
    }
}
