package ATM;
import java.util.Date;

public class Transaction {

    /**
     * Величина транзакции
     */
    private double amount;

    /**
     * Время и дата транзакции
     */
    private Date timestamp;

    /**
     * Заметка для транзакции
     */
    private String memo;

    /**
     * Аккаунт, на котором была произведена транзакция
     */
    private Account inAccount;

    /**
     * Создание новой транзакции
     * @param amount    сумма транзакции
     * @param inAccount аккаунт, которому принадлежит транзакция
     */
    public Transaction(double amount, Account inAccount) {

        this.amount = amount;
        this.inAccount = inAccount;
        this.timestamp = new Date();
        this.memo = "";

    }

    /**
     * Создание новой транзакции
     * @param amount    сумма транзакции
     * @param memo  заметка для транзакции
     * @param inAccount аккаунт, которому принадлежит транзакция
     */
    public Transaction(double amount, String memo, Account inAccount) {

        // вызвать конструктор с двумя аргументами
        this(amount, inAccount);

        // установить заметку
        this.memo = memo;

    }

    /**
     * Получить размер транзакции
     * @return размер
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * Получить строку - сводку транзакции
     * @return строка
     */
    public String getSummaryLine() {

        if (this.amount >= 0) {
            return String.format("%s : $%.02f : %s", this.timestamp.toString(), this.amount, this.memo);
        } else {
            return String.format("%s : $(%.02f) : %s", this.timestamp.toString(), -this.amount, this.memo);
        }
    }

}
