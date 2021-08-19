package ATM;

import java.util.ArrayList;
import java.util.Random;

public class Bank {

    private String name;

    private ArrayList<User> users;

    private ArrayList<Account> accounts; //У пользователя может быть несколько аккаунтов(счетов), и банк должен хранить
                                        // данные обо всех счетах и аккаунтах

    /**
     * Создание нового объекта "Bank" с пустыми списками пользователей и счетов
     * @param name  название банка
     */
    public Bank(String name) {

        this.name = name;
        this.users = new ArrayList<User>();
        this.accounts = new ArrayList<Account>();

    }

    /**
     * Сгенерировать новый универсальный уникальный ID для пользователя
     * @return  uuid
     */
    public String getNewUserUUID(){

        // инициализация
        String uuid;
        Random rng = new Random();
        int len = 6;
        boolean nonUnique;

        // продолжать генерацию, пока не получим уникальный ID
        do{

            // сгенерировать номер
            uuid = "";
            for (int c = 0; c < len; c++) {
                uuid += ((Integer)rng.nextInt(10)).toString();
            }

            // проверка, чтобы убедиться, что он уникален
            nonUnique = false;
            for (User u : this.users) {
                if (uuid.compareTo(u.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }
        } while (nonUnique);

        return uuid;
    }

    /**
     * Сгенерировать новый универсальный уникальный ID для аккаунта
     * @return the uuid
     */
    public String getNewAccountUUID(){

        // инициализация
        String uuid;
        Random rng = new Random();
        int len = 10;
        boolean nonUnique;

        // продолжать генерацию, пока не получим уникальный ID
        do{

            // сгенерировать номер
            uuid = "";
            for (int c = 0; c < len; c++) {
                uuid += ((Integer)rng.nextInt(10)).toString();
            }

            // проверка, чтобы убедиться, что он уникален
            nonUnique = false;
            for (Account a : this.accounts) {
                if (uuid.compareTo(a.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }
        } while (nonUnique);

        return uuid;
    }

    /**
     * Добавление аккаунта
     * @param anAcct    аккаунт для добавления
     */
    public void addAccount(Account anAcct) {
        this.accounts.add(anAcct);
    }

    /**
     * Создание нового пользователя банка
     * @param firstName имя пользователя
     * @param lastName  фамилия пользователя
     * @param pin       ПИН-код пользователя
     * @return          новый пользователь (Users object)
     */
    public User addUser(String firstName, String lastName, String pin)  {

        // создание нового пользователя и добавление его в наш список
        User newUser = new User(firstName, lastName, pin, this);
        this.users.add(newUser);

        // создание сберегательного счета для пользователя и добавление в списки пользователей и банковских аккаунтов
        Account newAccount = new Account("Savings", newUser, this);
        newUser.addAccount(newAccount);
        this.addAccount(newAccount);

        return newUser;
    }

    /**
     * Получить объект User, связанный с конкретным идентификатором пользователя и PIN-кодом, если они действительны.
     * @param userID UUID пользователя для входа
     * @param pin    ПИН-код пользователя
     * @return       объект User, если логин успешен, или null, если нет
     */
    public User userLogin(String userID, String pin) {

        // поиск через список пользователей
        for (User u : this.users) {

            // проверка корректности ПИН-кода пользователя
            if (u.getUUID().compareTo(userID) == 0 && u.validatePin(pin)) {
                return u;
            }
        }

        // если пользователь не найден или если введен некорректный ПИН-код
        return null;
    }

    /**
     * Получить название банка
     * @return название банка
     */
    public String getName() {
        return this.name;
    }

}
