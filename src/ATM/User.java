package ATM;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.security.MessageDigest;

public class User {

    /**
     * Имя пользователя
     */
    private String firstName;

    /**
     * Фамилия пользователя
     */
    private String lastName;

    /**
     * ID пользователя
     */
    private String uuid; // universally unique identifier(Универсальный уникальный идентификатор)

    /**
     * Хэш MD5 пин-кода пользователя
     */
    private byte pinHash[];

    /**
     * Список аккаунтов данного пользователя
     */
    private ArrayList<Account> accounts; // arrayList, так как это класс переменной длины, в отличие от Array

    /**
     * Создать нового пользователя
     * @param firstName имя пользователя
     * @param lastName фамилия пользователя
     * @param pin ПИН-код аккаунт
     * @param theBank объект Bank, клиентом которого является пользователь
     */
    public User(String firstName, String lastName, String pin, Bank theBank) {

        // задать имя пользователя
        this.firstName = firstName;
        this.lastName = lastName;

        // хранить хеш MD5 пин-кода, а не исходное значение, в целях безопасности
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes()); // из памяти берутся байты объекта PIN и прогоняются
                                                        // через алгоритм MD5
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        // получить новый, уникальный универсальный ID для пользователя
        this.uuid = theBank.getNewUserUUID();

        // создать пустой список аккаунтов
        this.accounts = new ArrayList<Account>();

        // написать сообщение лога
        System.out.printf("New user %s, %s with ID %s created.\n", lastName, firstName, this.uuid);

    }

    /**
     * Добавить аккаунт пользователя
     * @param anAcct    аккаунт для добавления
     */
    public void addAccount(Account anAcct) {
        this.accounts.add(anAcct);
    }

    /**
     * Вернуть значение UUID пользователя
     * @return uuid
     */
    public String getUUID() {
        return this.uuid;
    }

    /**
     * Проверка, совпадает ли введенный ПИН-код с реальным ПИН-кодом пользователя
     * @param aPin  ПИН-код для проверки
     * @return      верен ли ПИН-код или нет
     */
    public boolean validatePin(String aPin) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(aPin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        return false;
    }

    /**
     * Вернуть имя(firstName) пользователя
     * @return firstName
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Написать сводку по аккаунтам пользователя
     */
    public void printAccountsSummary() {
        System.out.printf("\n\n%s's accounts summary\n", this.firstName);
        for(int a = 0; a < this.accounts.size(); a++) {
            System.out.printf("  %d) %s\n", a+1, this.accounts.get(a).getSummaryLine());
        }
        System.out.println();
    }

    /**
     * Получить количество аккаунтов у пользователя
     * @return  количество аккаунтов
     */
    public int numAccounts() {
        return this.accounts.size();
    }

    /**
     * Вывести историю транзакций для конкретного аккаунта
     * @param acctIdx   индекс аккаунта для использования
     */
    public void printAcctTransHistory(int acctIdx) {
        this.accounts.get(acctIdx).printTransHistory();
    }

    /**
     * Получить баланс конкретного аккаунта
     * @param acctIdx   индекс нужного аккаунта
     * @return          баланс на аккаунте
     */
    public double getAcctBalance( int acctIdx) {
        return this.accounts.get(acctIdx).getBalance();
    }

    /**
     * Получение UUID конкретного аккаунта
     * @param acctIdx   индекс используемого аккаунта
     * @return          UUID аккаунта
     */
    public String getAcctUUID(int acctIdx) {
        return this.accounts.get(acctIdx).getUUID();
    }

    /**
     * Добавить транзакцию к конкретному аккаунту
     * @param acctIdx   индекс аккаунта
     * @param amount    величина транзакции
     * @param memo      заметка к транзакции
     */
    public void addAcctTransaction(int acctIdx, double amount, String memo) {
        this.accounts.get(acctIdx).addTransaction(amount, memo);
    }
}
