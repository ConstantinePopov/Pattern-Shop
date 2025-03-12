import java.util.*;

public class Shop {
    public static void main(String[] args) {
        List<String> menuMain = Arrays.asList("Выход из системы.",
                "Вход покупателя в систему.",
                "Ввод покупателей в систему.",
                "Ввод товаров в систему.");
        List<String> menuUser = Arrays.asList("Выход из подменю.",
                "Вывод общего списка товаров.",
                "Вывод списка товаров по фильтрам.",
                "Выбор товаров для покупки.",
                "Вывод корзины покупателя.",
                "Очистка корзины",
                "Покупка.",
                "Информация о покупателе.");
        List<String> menuFilter = Arrays.asList("Выход из подменю.",
                "Вывод товара по ключевому слову.",
                "Вывод товаров по производителю.",
                "Вывод товаров с ценой выше указанной.",
                "Выбор товаров с ценой равной указанной.",
                "Вывод товаров с ценой ниже указанной.");

        List<User> users = new ArrayList<>();
        users.add(new User(0, "Василий", "Иванов"));
        users.add(new User(1, "Иван", "Васильев"));
        users.add(new User(2, "Петр", "Семенов"));

        List<Product> products = new ArrayList<>();
        products.add(new Product(0, "Паяльник", 100, "ПрипойЛТД", 15));
        products.add(new Product(1, "Канифоль", 50, "ПрипойЛТД", 10));
        products.add(new Product(2, "Молоток", 80, "УдарникИнк", 20));
        products.add(new Product(3, "Долото", 90, "УдарникИнк", 8));

        Scanner sc = new Scanner(System.in);

        boolean run = true;
        while (run) {
            showMenu(menuMain);
            String pickMenuItem = sc.nextLine();
            System.out.println();
            switch (pickMenuItem) {
                case "0":
                    // Выход из главного меню
                    run = false;
                    break;
                case "1":
                    // Вход покупателя в систему для выбора товаров и покупки
                    System.out.print("Введите свой Id: ");
                    int userId = Integer.parseInt(sc.nextLine());
                    User user = users.get(userId);
                    UserBasket userBasket = new UserBasket(user, new Basket());
                    System.out.println("Здравствуйте, " + user);
                    System.out.println("Вам доступны следующие действия:");
                    boolean runUserMenu = true;
                    while (runUserMenu) {
                        showMenu(menuUser);
                        String pickSubMenuItem = sc.nextLine();
                        System.out.println();
                        switch (pickSubMenuItem) {
                            case "0":
                                // Выход из меню покупателя
                                runUserMenu = false;
                                break;
                            case "1":
                                // Вывод на экран списка товаров
                                // Magic Numbers - пример избегания непонятных чисел
                                for (int i = 0; i < products.size(); i++) {
                                    System.out.println(i + ". " + products.get(i));
                                }
                                break;
                            case "2":
                                // Вывод на экран товаров по фильтрам
                                boolean runFilterUser = true;
                                while (runFilterUser) {
                                    showMenu(menuFilter);
                                    String pickFilter = sc.nextLine();
                                    System.out.println();
                                    switch (pickFilter) {
                                        case "0":
                                            runFilterUser = false;
                                            break;
                                        case "1":
                                            // Вывод списка товаров по ключевому слову
                                            System.out.print("Введите ключевое слово: ");
                                            String keyword = sc.nextLine();
                                            products.stream()
                                                    .filter(x -> x.getLabel().contains(keyword))
                                                    .forEach(System.out::println);
                                            break;
                                        case "2":
                                            // Вывод товаров по производителю
                                            System.out.print("Введите производителя: ");
                                            String creator = sc.nextLine();
                                            products.stream()
                                                    .filter(x -> x.getCreator().equals(creator))
                                                    .forEach(System.out::println);
                                            break;
                                        case "3":
                                            // Вывод товаров с ценой больше указанной
                                            System.out.print("Введите значение: ");
                                            int priceMore = Integer.parseInt(sc.nextLine());
                                            products.stream()
                                                    .filter(x -> x.getPrice() > priceMore)
                                                    .forEach(System.out::println);
                                            break;
                                        case "4":
                                            // Вывод товаров с ценой равной указанной
                                            System.out.print("Введите значение: ");
                                            int priceEquals = Integer.parseInt(sc.nextLine());
                                            products.stream()
                                                    .filter(x -> x.getPrice() == priceEquals)
                                                    .forEach(System.out::println);
                                            break;
                                        case "5":
                                            // Вывод товаров с ценой ниже указанной
                                            System.out.print("Введите значение: ");
                                            int priceLow = Integer.parseInt(sc.nextLine());
                                            products.stream()
                                                    .filter(x -> x.getPrice() < priceLow)
                                                    .forEach(System.out::println);
                                            break;
                                        default:
                                            System.out.println("Такого пункта в меню нет.");
                                            break;
                                    }
                                }
                                break;
                            case "3":
                                // Выбор товаров с наполнением корзины
                                System.out.println("Введите через пробел номера закупаемых продуктов.");
                                System.out.println("Повторы номеров означают количество выбранных товаров.");
                                String[] productsToBye = sc.nextLine().split(" ");
                                for (String s : productsToBye) {
                                    int productNumber = Integer.parseInt(s);
                                    Product productToChange = products.get(productNumber);
                                    if (productToChange.getQuantity() > 0) {
                                        productToChange.setQuantity(productToChange.getQuantity() - 1);
                                        userBasket.basket.changeProductsInBasket(productToChange);
                                    } else {
                                        System.out.println("Товара " + productToChange.getLabel() + " больше нет.");
                                    }
                                }
                                break;
                            case "4":
                                // Вывод товаров, находящихся в корзине пользователя
                                System.out.println(userBasket);
                                break;
                            case "5":
                                // Очистка корзины с возвратом товаров в систему
                                for (Product key : userBasket.basket.productsInBasket.keySet()) {
                                    Product productToReturn = products.get(key.getId());
                                    productToReturn.setQuantity(productToReturn.getQuantity() +
                                            userBasket.basket.productsInBasket.get(key));
                                }
                                userBasket.basket.makeEmpty();
                                break;
                            case "6":
                                // Покупка товаров пользователем
                                System.out.println("Чтобы совершить покупку, введите 1.");
                                if (sc.nextLine().equals("1")) {
                                    user.wasBought.add(userBasket.basket.toString());
                                    userBasket.basket.makeEmpty();
                                }
                                break;
                            case "7":
                                // Вывод информации о пользователе с учетом сделанных покупок
                                // D - логика будет зависеть от интерфейса CommonInfoService
                                CommonInfoService infoService = new InfoService();
                                infoService.info(user);
                                break;
                            default:
                                System.out.println("Такого пункта в меню нет.");
                                break;
                        }
                    }
                    break;
                case "2":
                    // Ввод пользователей в систему (если есть необходимость)
                    int numberOfUsers = users.size();
                    System.out.println("Введите через пробел Имя Фамилия: ");
                    String[] userName = sc.nextLine().split(" ");
                    users.add(new User(numberOfUsers, userName[0], userName[1]));
                    System.out.println();
                    break;
                case "3":
                    // Ввод товаров в систему (если есть необходимость)
                    int numberOfProducts = products.size();
                    System.out.println("Введите товар в формате (через пробел):");
                    System.out.println("Наименование Стоимость Производитель Количество");
                    String[] productDescription = sc.nextLine().split(" ");
                    products.add(new Product(numberOfProducts,
                            productDescription[0],
                            Integer.parseInt(productDescription[1]),
                            productDescription[2],
                            Integer.parseInt(productDescription[3])));
                    System.out.println();
                    break;
                default:
                    System.out.println("Такого пункта в меню нет.");
                    break;
            }
        }
    }

    // DRY - showMenu позволяет не повторять общий код вывода меню

    private static void showMenu(List<String> menu) {
        for (int i = 0; i < menu.size(); i++) {
            System.out.println(i + ". " + menu.get(i));
        }

        System.out.println();
        System.out.print("Введите номер действия: ");
    }

    static class User {
        private int id;
        private String name;
        private String surname;
        public List<String> wasBought = new ArrayList<>();

        User(int id, String name, String surname) {
            this.id = id;
            this.name = name;
            this.surname = surname;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getSurname() {
            return surname;
        }

        @Override
        public String toString() {
            return name + " " + surname;
        }
    }

    // O - принцип открытости/закрытости
    public interface CommonInfoService {
        public void info(User user);
    }

    // S - вынесена ответственность за вывод информации о пользователе
    public static class InfoService implements CommonInfoService {
        @Override
        public void info(User user) {
            StringBuilder infoText = new StringBuilder();
            infoText.append("Пользователь номер ")
                    .append(user.id)
                    .append("\n");
            infoText.append("Имя: ")
                    .append(user.name)
                    .append("\n");
            infoText.append("Фамилия: ")
                    .append(user.surname)
                    .append("\n");
            if (user.wasBought.isEmpty()) {
                infoText.append("Покупок не было. \n");
            } else {
                for (int i = 0; i < user.wasBought.size(); i++) {
                    infoText.append("Покупка ")
                            .append(i + 1)
                            .append("\n")
                            .append(user.wasBought.get(i));
                }
            }
            System.out.println(infoText.toString());
        }
    }

    static class Product {
        private int id;
        private String label;
        private int price;
        private String creator;
        private int quantity;

        Product(int id, String label, int price, String creator, int quantity) {
            this.id = id;
            this.label = label;
            this.price = price;
            this.creator = creator;
            this.quantity = quantity;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getPrice() {
            return price;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getCreator() {
            return creator;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getQuantity() {
            return quantity;
        }

        @Override
        public String toString() {
            return "Товар " + label +
                    " по цене: " + price +
                    " руб. от производителя: " + creator +
                    " в количестве " + quantity + " штук.";
        }
    }

    static class Basket {
        public Map<Product, Integer> productsInBasket = new HashMap<>();

        public void changeProductsInBasket(Product newProduct) {
            if (productsInBasket.containsKey(newProduct)) {
                productsInBasket.put(newProduct, productsInBasket.get(newProduct) + 1);
            } else {
                productsInBasket.put(newProduct, 1);
            }
        }

        public void makeEmpty() {
            productsInBasket.clear();
        }

        @Override
        public String toString() {
            if (!productsInBasket.isEmpty()) {
                StringBuilder toPrint = new StringBuilder();
                for (Product key : productsInBasket.keySet()) {
                    toPrint.append("Товар ")
                            .append(key.getLabel())
                            .append(" в количестве ")
                            .append(productsInBasket.get(key))
                            .append(" шт. \n");
                }
                return toPrint.toString();
            } else {
                return "Ничего нет.";
            }
        }

    }

    // L - мы не стали наследоваться от Basket, поскольку удобнее показалось включить корзину, как тип переменной

    static class UserBasket {
        private User user;
        private Basket basket;

        UserBasket(User user, Basket basket) {
            this.user = user;
            this.basket = basket;
        }

        @Override
        public String toString() {
            return "У вас в корзине следующие товары: \n" +
                    basket.toString();
        }
    }
}
