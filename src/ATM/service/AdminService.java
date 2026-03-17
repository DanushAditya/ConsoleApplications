package ATM.service;

import ATM.Account.User;

import java.util.HashMap;

public class AdminService {
    HashMap<Integer, User> map;
    ATMVault vault;

    public AdminService(HashMap<Integer, User> map, ATMVault vault) {
        this.map = map;
        this.vault = vault;
    }

    public void displayAll() {
        if (map.isEmpty()) {
            System.out.println("No Accounts Found");
            return;
        }
        System.out.println("**********************");
        System.out.println("All Accounts");
        System.out.println("**********************");
        for (int i : map.keySet()) {
            User user = map.get(i);
            System.out.println(
                    "AcctNo: " + user.getAcctNo() + "  Name: " + user.getName() + "  Balance: " + user.getBalance());
        }
        System.out.println("**********************");
    }

    public void addAccount(int acctNo, String name, int balance, int pin) {
        if (map.containsKey(acctNo)) {
            System.out.println("Account Already Exists");
            return;
        }
        map.put(acctNo, new User(name, acctNo, balance, pin));
        System.out.println("Account Created Successfully");
    }

    public void deleteAccount(int acctNo) {
        if (!map.containsKey(acctNo)) {
            System.out.println("Account Not Found");
            return;
        }
        map.remove(acctNo);
        System.out.println("Account Deleted Successfully");
    }

    public void viewAccount(int acctNo) {
        User user = map.get(acctNo);
        if (user == null) {
            System.out.println("Account Not Found");
            return;
        }
        System.out.println("**********************");
        System.out.println("Name    : " + user.getName());
        System.out.println("AcctNo  : " + user.getAcctNo());
        System.out.println("Balance : " + user.getBalance());
        System.out.println("Pin     : " + user.getPin());
        System.out.println("**********************");
    }

    public void resetPin(int acctNo, int newPin) {
        User user = map.get(acctNo);
        if (user == null) {
            System.out.println("Account Not Found");
            return;
        }
        user.setPin(newPin);
        System.out.println("Pin Reset Successfully for " + user.getName());
    }

    public void loadCash(int hundreds, int twoHundreds, int fiveHundreds) {
        vault.loadCash(hundreds, twoHundreds, fiveHundreds);
    }

    public void displayVault() {
        vault.displayVault();
    }

    public HashMap<Integer, User> getMap() {
        return map;
    }
}
