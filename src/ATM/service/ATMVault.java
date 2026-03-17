package ATM.service;

public class ATMVault {
    int hundreds;
    int twoHundreds;
    int fiveHundreds;

    public ATMVault() {
        this.hundreds = 0;
        this.twoHundreds = 0;
        this.fiveHundreds = 0;
    }

    public void loadCash(int h, int t, int f) {
        hundreds += h;
        twoHundreds += t;
        fiveHundreds += f;
        System.out.println("Cash Loaded into ATM Successfully");
        displayVault();
    }

    // Returns {hundreds, twoHundreds, fiveHundreds} needed, or null if vault can't
    // dispense
    public int[] calculateDispense(int amount) {
        for (int f = Math.min(fiveHundreds, amount / 500); f >= 0; f--) {
            int rem1 = amount - f * 500;
            for (int t = Math.min(twoHundreds, rem1 / 200); t >= 0; t--) {
                int rem2 = rem1 - t * 200;
                if (rem2 % 100 == 0) {
                    int h = rem2 / 100;
                    if (h <= hundreds) {
                        return new int[] { h, t, f };
                    }
                }
            }
        }
        return null;
    }

    public boolean canDispense(int h, int t, int f) {
        return hundreds >= h && twoHundreds >= t && fiveHundreds >= f;
    }

    public void dispense(int h, int t, int f) {
        hundreds -= h;
        twoHundreds -= t;
        fiveHundreds -= f;
    }

    public int totalCash() {
        return (hundreds * 100) + (twoHundreds * 200) + (fiveHundreds * 500);
    }

    public void displayVault() {
        System.out.println("**********************");
        System.out.println("ATM Cash Available");
        System.out.println("100s  : " + hundreds);
        System.out.println("200s  : " + twoHundreds);
        System.out.println("500s  : " + fiveHundreds);
        System.out.println("Total : " + totalCash());
        System.out.println("**********************");
    }
}
