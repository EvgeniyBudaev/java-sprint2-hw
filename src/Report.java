import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Report {
    private final String filesPath = "resources/";
    private MonthlyReport monthlyReport;
    private YearlyReport yearlyReport;
    private ArrayList<MonthlyReport> filesMonthly;
    private ArrayList<YearlyReport> filesYearly;
    private HashMap<Integer, ArrayList<MonthlyReport>> monthlyReports = new HashMap<>();
    private HashMap<Integer, ArrayList<YearlyReport>> yearlyReports = new HashMap<>();

    public void readMonthlyFiles() {
        for (int i = 1; i <= 3; i++) {
            String monthPath = filesPath + "m.20210" + i + ".csv";
            String data = ReadFile.readFileContentsOrNull(monthPath);
            if (data == null) {
                break;
            }
            String[] lines = data.split(System.lineSeparator());
            filesMonthly = new ArrayList<>();

            for (int j = 1; j < lines.length; j++) {
                String[] lineContents = lines[j].split(",");
                String itemName = lineContents[0];
                boolean isExpense = Boolean.parseBoolean(lineContents[1]);
                int quantity = Integer.parseInt(lineContents[2]);
                int sumOfOne = Integer.parseInt(lineContents[3]);
                monthlyReport = new MonthlyReport(itemName, isExpense, quantity, sumOfOne);
                filesMonthly.add(monthlyReport);
            }
            monthlyReports.put(i, filesMonthly);
        }
        System.out.println("Все месячные отчёты считаны");
    }

    public void readYearlyFiles() {
        String data = ReadFile.readFileContentsOrNull(filesPath + "y.2021.csv");
        String[] lines = data.split(System.lineSeparator());
        filesYearly = new ArrayList<>();
        int month = 0;
        for (int j = 1; j < lines.length; j++) {
            String[] lineContents = lines[j].split(",");
            month = Integer.parseInt(lineContents[0]);
            int amount = Integer.parseInt(lineContents[1]);
            boolean isExpense = Boolean.parseBoolean(lineContents[2]);
            yearlyReport = new YearlyReport(month, amount, isExpense);
            filesYearly.add(yearlyReport);
        }
        yearlyReports.put(month, filesYearly);
        System.out.println("Годовой отчёт считан");
    }

    public void checkReport() {
        if (monthlyReports.isEmpty() || yearlyReports.isEmpty()) {
            System.out.println("Месячные или годовой отчеты не считаны");
        } else {
            long totalExpenseMonthly = 0;
            long totalIncomeMonthly = 0;
            AtomicLong totalExpenseYearly = new AtomicLong();
            AtomicLong totalIncomeYearly = new AtomicLong();
            boolean isCorrectly = false;

            for (ArrayList<MonthlyReport> monthlyReport: monthlyReports.values()) {
                long expenseMonth = monthlyReport.stream().mapToLong(report -> {
                    if (report.isExpense()) {
                        return (long) report.getQuantity() *report.getSumOfOne();
                    } else {
                        return 0;
                    }
                }).sum();
                long incomeMonth = monthlyReport.stream().mapToLong(report -> {
                    if (!report.isExpense()) {
                        return (long) report.getQuantity() *report.getSumOfOne();
                    } else {
                        return 0;
                    }
                }).sum();
                System.out.println("expenseMonth: " + expenseMonth);
                totalExpenseMonthly += expenseMonth;
                totalIncomeMonthly += incomeMonth;
            }

            for (ArrayList<YearlyReport> yearlyReport: yearlyReports.values()) {
                yearlyReport.stream().forEach(report -> {
                    if (report.isExpense()) {
                        totalExpenseYearly.addAndGet(report.getAmount());
                    } else {
                        totalIncomeYearly.addAndGet(report.getAmount());
                    }
                });
            }
            System.out.println("totalExpenseMonthly: " + totalExpenseMonthly);
            System.out.println("totalExpenseYearly.get(): " + totalExpenseYearly.get());

            if (totalExpenseMonthly != totalExpenseYearly.get()) {
                System.out.println("В " + " несоответствие расходов");
                isCorrectly = false;
            }
            else if (totalIncomeMonthly != totalIncomeYearly.get()) {
                System.out.println("В " + " несоответствие доходов");
                isCorrectly = false;
            } else {
                isCorrectly = true;
            }

            if (isCorrectly) {
                System.out.println("Операция успешно завершена");
            }
        }
    }

}
