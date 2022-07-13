import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Report report = new Report();

        while (true) {
            printMenu();
            int userInput = scanner.nextInt();

            if (userInput == 1) {
                report.readMonthlyFiles();
            } else if (userInput == 2) {
                report.readYearlyFiles();
            } else if (userInput == 3) {
                report.checkReport();
            } else if (userInput == 4) {
                report.getInfoMonthlyReport();
            } else if (userInput == 5) {
                report.getInfoYearlyReport();
            } else if (userInput == 0) {
                System.out.println("Вы вышли из приложения!");
                break;
            } else {
                System.out.println("Извините, такой команды пока нет.");
            }
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

