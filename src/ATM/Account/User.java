package ATM.Account;

public class User {

    String name;
    int acctNo;
    int balance;

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    int pin;

    public User(String name, int acctNo, int balance, int pin) {
        this.name = name;
        this.acctNo = acctNo;
        this.balance = balance;
        this.pin=pin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(int acctNo) {
        this.acctNo = acctNo;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void display() {
        System.out.println("Name :"+name+" Balance :"+balance+" Pin :"+pin);
    }
}
