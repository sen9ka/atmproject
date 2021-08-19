package ATM;
import java.sql.SQLOutput;
import java.util.Scanner;

public class ATM {

    public static void main(String[] args) {

        // инициализация сканнера
        Scanner sc = new Scanner(System.in);

        // инициализация банка
        Bank theBank = new Bank("Bank of Drausin");

        // добавление пользователя, который также создает сберегательный счет
        User aUser = theBank.addUser("John", "Doe", "1234");

        // добавление расчетного счета для пользователя
        Account newAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while (true) {

            // оставаться в состоянии входа в систему до момента успешного входа в систему
            curUser = ATM.mainMenuPrompt(theBank, sc);

            // отсаваться в меню до момента выхода пользователя
            ATM.printUserMenu(curUser, sc);

        }

    }

    /**
     * Создание меню входа в банкомат
     * @param theBank банк, чей аккаунт используется
     * @param sc сканнер, необходимый для считывания того, что ввел пользователь
     * @return аутентифицированного пользователя
     */
    public static User mainMenuPrompt(Bank theBank, Scanner sc) {

        // инициализация
        String userID;
        String pin;
        User authUser;

        // проверка пользователя на комбинацию ID/ПИН-кода до тех пор, пока не достигнута верная
        do{

            System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
            System.out.print("Enter user ID: ");
            userID = sc.nextLine();
            System.out.print("Enter pin: ");
            pin = sc.nextLine();

            // попробовать получить пользователя, соответствующего комбинации ID/ПИН-кода
            authUser = theBank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.println("Incorrect user ID/pin combination. Please try again.");
            }

        } while(authUser == null); // продолжать цикл до успешного входа

        return authUser;

    }

    public static void printUserMenu(User theUser, Scanner sc) {

        // информация об аккаунтах пользователя
        theUser.printAccountsSummary();

        // инициализация
        int choice;

        // меню пользователя
        do {
            System.out.printf("Welcome %s, what would you like to do?\n", theUser.getFirstName());
            System.out.println("  1) Show account transaction history");
            System.out.println("  2) Withdraw");
            System.out.println("  3) Deposit");
            System.out.println("  4) Transfer");
            System.out.println("  5) Quit");
            System.out.println();
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice. Please choose 1-5");
            }
        } while(choice < 1 || choice > 5);

        // обработка выбора
        switch (choice) {

            case 1:
                ATM.showTransHistory(theUser, sc);
                break;
            case 2:
                ATM.withdrawFunds(theUser, sc);
                break;
            case 3:
                ATM.depositFunds(theUser, sc);
                break;
            case 4:
                ATM.transferFunds(theUser, sc);
                break;
            case 5:
                // "съесть" оставшуюся часть предыдущего ввода
                sc.nextLine();
                break;
        }

        // заново отобразить это меню, кроме случая, когда пользователь хочет выйти
        if (choice != 5){
            ATM.printUserMenu(theUser, sc);
        }
    }

    /**
     * Показать историю транзакций аккаунта
     * @param theUser   залогинившийся пользователь
     * @param sc        сканнер, использующийся для пользовательского ввода
     */
    public static void showTransHistory(User theUser, Scanner sc){

        int theAcct;

        // выбрать аккаунт, чью историю транзакций просмотреть
        do {
            System.out.printf("Enter the number (1-%d) of the account\n whose transaction you want to see: ",
                                theUser.numAccounts());
            theAcct = sc.nextInt()-1;
            if (theAcct < 0 || theAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again");
            }
        } while (theAcct < 0 || theAcct >= theUser.numAccounts());

        // вывести историю транзакций

        theUser.printAcctTransHistory(theAcct);

    }

    /**
     * Делает перевод средств с одного аккаунта на другой
     * @param theUser   вошедший в систему пользователь
     * @param sc        сканер для считывания пользовательского ввода
     */
    public static void transferFunds(User theUser, Scanner sc) {

        // инициализация
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        // получить аккаунт, с которого выполняется перевод
        do {
            System.out.printf("Enter the number (1-%d) of the account to transfer from: ", theUser.numAccounts());
            fromAcct = sc.nextInt()-1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again");
            }
        } while (fromAcct <0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        //получить аккаунт, которому ваыполняется перевод
        do {
            System.out.printf("Enter the number (1-%d) of the account to transfer to: ", theUser.numAccounts());
            toAcct = sc.nextInt()-1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again");
            }
        } while (toAcct <0 || toAcct >= theUser.numAccounts());

        // получить сумму для перевода
        do {
            System.out.printf("Enter the amount to transfer (max $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must not be greater than balance of $%.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        // наконец, выполнить перевод
        theUser.addAcctTransaction(fromAcct, -1*amount, String.format(
                "Transfer to account %s", theUser.getAcctUUID(toAcct)));
        theUser.addAcctTransaction(toAcct, amount, String.format(
                "Transfer to account %s", theUser.getAcctUUID(toAcct)));

    }

    /**
     * Процесс списания средств с аккаунта(счета)
     * @param theUser   вошедший в аккаунт пользователь
     * @param sc        сканнер, использующийся для ввода пользователя
     */
    public static void withdrawFunds(User theUser, Scanner sc) {

        // инициализация
        int fromAcct;
        double amount;
        double acctBal;
        String memo;

        // получить аккаунт, с которого выполняется перевод
        do {
            System.out.printf("Enter the number (1-%d) of the account to withdraw from: ", theUser.numAccounts());
            fromAcct = sc.nextInt()-1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again");
            }
        } while (fromAcct <0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        // получить сумму для перевода
        do {
            System.out.printf("Enter the amount to transfer (max $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must not be greater than balance of $%.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        // "съесть" оставшуюся часть предыдущего ввода
        sc.nextLine();

        //создать заметку
        System.out.println("Enter a memo: ");
        memo = sc.nextLine();

        //выполнить списание
        theUser.addAcctTransaction(fromAcct, -1*amount, memo);
    }

    /**
     * Процесс депозита средств на аккаунт(счет)
     * @param theUser   вошедший в систему пользователь
     * @param sc        сканнер для считывания ввода пользователя
     */
    public static void depositFunds(User theUser, Scanner sc) {

        // инициализация
        int toAcct;
        double amount;
        double acctBal;
        String memo;

        // получить аккаунт, на который выполняется перевод
        do {
            System.out.printf("Enter the number (1-%d) of the account to deposit in: ", theUser.numAccounts());
            toAcct = sc.nextInt()-1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again");
            }
        } while (toAcct <0 || toAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(toAcct);

        // получить сумму для пополнения
        do {
            System.out.printf("Enter the amount to transfer (max $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            }
        } while (amount < 0);

        // "съесть" оставшуюся часть предыдущего ввода
        sc.nextLine();

        //создать заметку
        System.out.println("Enter a memo: ");
        memo = sc.nextLine();

        //выполнить списание
        theUser.addAcctTransaction(toAcct, amount, memo);

    }
}
