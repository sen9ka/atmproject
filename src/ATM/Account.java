package ATM;
import java.util.ArrayList;

public class Account {

    /**
     * Имя аккаунта
     */
    private String name;

    /**
     * ID аккаунта
     */
    private String uuid;

    /**
     * Объект User, которому принадлежит аккаунт
     */
    private User holder;

    /**
     * Список транзакций для данного аккаунта
     */
    private ArrayList<Transaction> transactions;

    /**
     * Создать новый аккаунт
     * @param name  имя аккаунта
     * @param holder    объект User, которому принадлежит аккаунт
     * @param theBank   банк, услугами которого пользуется аккаунт
     */
    public Account(String name, User holder, Bank theBank){ // когда аккаунт создается, он добавляет себя в списки
                                                            // аккаунтов пользователя и в списки аккаунтов банка

        // задать имя учетной записи и владельца
        this.name = name;
        this.holder = holder;

        // получить новый UUID аккаунта
        this.uuid = theBank.getNewAccountUUID();

        // инициализировать транзакции
        this.transactions = new ArrayList<Transaction>();

    }

    /**
     * Получить ID аккаунта
     * @return uuid
     */
    public String getUUID(){
        return this.uuid;
    }

    /**
     * Строка с балансо пользователя
     * @return строка баланса
     */
    public String getSummaryLine() {

        // получить баланс пользователя
        double balance = this.getBalance();

        // форматировать строку в зависимости от того, отрицательный баланс или нет
        if (balance >= 0) {
            return String.format("%s : $%.02f : %s", this.uuid, balance, this.name);
        } else {
            return String.format("%s : $(%.02f) : %s", this.uuid, balance, this.name);
        }
    }

    /**
     * Получить баланс аккаунта
     * @return значение баланса
     */
    public double getBalance() {

        double balance = 0;
        for (Transaction t : this.transactions) {
            balance += t.getAmount();
        }
        return balance;
    }

    /**
     * Напечатать историю транзакций аккаунта
     */
    public void printTransHistory() {

        System.out.printf("\nTransaction history for account %s\n", this.uuid);
        for (int t = this.transactions.size()-1; t >= 0; t--) {
            System.out.println(this.transactions.get(t).getSummaryLine());
        }
        System.out.println();
    }

    /**
     * Добавить новую транзакцию на этом аккаунте
     * @param amount    величина транзакции
     * @param memo      заметка транзакции
     */
    public void addTransaction(double amount, String memo) {

        //создать новый объект транзакции и добавить его в наш лист
        Transaction newTrans = new Transaction(amount, memo, this);
        this.transactions.add(newTrans);
    }

}
