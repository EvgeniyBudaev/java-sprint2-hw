import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Report report = new Report();
        ArrayList<String> listMonths = new ArrayList<>() {{
            add("январь");
            add("февраль");
            add("март");
            add("апрель");
            add("май");
            add("июнь");
            add("июль");
            add("август");
            add("сентябрь");
            add("октябрь");
            add("ноябрь");
            add("декабрь");
        }};
        HashMap<Integer, ArrayList<MonthlyReport>> monthlyReports = new HashMap<>();

        printMenu();
        int userInput = scanner.nextInt();

        while (userInput != 0) {
            if (userInput == 1) {
                report.readMonthlyFiles();
            } else if (userInput == 2) {
                report.readYearlyFiles();
            } else if (userInput == 3) {
                report.checkReport();
            } else {
                System.out.println("Извините, такой команды пока нет.");
            }

            printMenu();
            userInput = scanner.nextInt();
        }
        System.out.println("Программа завершена");
        scanner.close();
    }

    public static void printMenu() {
        System.out.println("Что вы хотите сделать? ");
        System.out.println("1 - Считать все месячные отчёты");
        System.out.println("2 - Считать годовой отчёт");
        System.out.println("3 - Сверить отчёты");
        System.out.println("4 - Вывести информацию о всех месячных отчётах");
        System.out.println("5 - Вывести информацию о годовом отчёте");
        System.out.println("0 - Выйти из приложения");
    }
}

