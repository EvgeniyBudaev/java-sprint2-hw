import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Report {
    private final String filesPath = "resources/";
    private MonthlyReport monthlyReport;
    private YearlyReport yearlyReport;
    private ArrayList<MonthlyReport> filesMonthly;
    private ArrayList<YearlyReport> filesYearly;
    private HashMap<Integer, ArrayList<MonthlyReport>> monthlyReports = new HashMap<>();
    private HashMap<Integer, ArrayList<YearlyReport>> yearlyReports = new HashMap<>();
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
            if (j % 2 != 0) {
                filesYearly.add(yearlyReport);
            } else {
                filesYearly.add(yearlyReport);
                yearlyReports.put(month, filesYearly);
                filesYearly = new ArrayList<>();
            }
        }
        System.out.println("Годовой отчёт считан");
    }

    public void checkReport() {
        if (monthlyReports.isEmpty() || yearlyReports.isEmpty()) {
            System.out.println("Месячные или годовой отчеты не считаны");
        } else {
            boolean isCorrectly = true;

            for (int i = 1; i <= monthlyReports.size(); i++) {
                ArrayList<MonthlyReport> monthlyReport1 = monthlyReports.get(i);
                ArrayList<YearlyReport> yearlyReport1 = yearlyReports.get(i);

                long expenseMonth = monthlyReport1.stream().mapToLong(report -> {
                    if (report.isExpense()) {
                        return (long) report.getQuantity() * report.getSumOfOne();
                    } else {
                        return 0;
                    }
                }).sum();
                long incomeMonth = monthlyReport1.stream().mapToLong(report -> {
                    if (!report.isExpense()) {
                        return (long) report.getQuantity() * report.getSumOfOne();
                    } else {
                        return 0;
                    }
                }).sum();

                long expenseYearly = yearlyReport1.stream().mapToLong(report -> {
                    if (report.isExpense()) {
                        return report.getAmount();
                    } else {
                        return 0;
                    }
                }).sum();
                long incomeYearly = yearlyReport1.stream().mapToLong(report -> {
                    if (!report.isExpense()) {
                        return report.getAmount();
                    } else {
                        return 0;
                    }
                }).sum();

                if (expenseMonth != expenseYearly) {
                    System.out.println("В " + listMonths.get(i - 1) + " несоответствие расходов");
                    isCorrectly = false;
                } else if (incomeMonth != incomeYearly) {
                    System.out.println("В " + listMonths.get(i - 1) + " несоответствие доходов");
                    isCorrectly = false;
                }
            }

            if (isCorrectly) {
                System.out.println("Операция успешно завершена");
            }
        }
    }

    public void getInfoMonthlyReport() {
        for (int i = 1; i <= monthlyReports.size(); i++) {
            System.out.println(listMonths.get(i - 1) + ":");
            ArrayList<MonthlyReport> monthlyInfo = monthlyReports.get(i);
            getProfitableCommodityPerMonth(monthlyInfo);
            getMaxExpensePerMonth(monthlyInfo);
        }
    }

    public void getProfitableCommodityPerMonth(ArrayList<MonthlyReport> monthlyInfo) {
        Optional<MonthlyReport> profitableCommodity = monthlyInfo.stream()
                .filter(el -> !el.isExpense())
                .max((el1, el2) -> (el1.getQuantity()*el1.getSumOfOne())-(el2.getQuantity()*el2.getSumOfOne()));
        if (profitableCommodity.isPresent()) { // isPresent - если есть значение
            String maxProfitItemName = profitableCommodity.get().getItemName();
            int maxProfit = profitableCommodity.get().getQuantity() * profitableCommodity.get().getSumOfOne();
            System.out.print("Самый прибыльный товар: " + maxProfitItemName);
            System.out.println(", продан на сумму: " + maxProfit);
        } else {
            System.out.println("Товар не найден");
        }
    }

    public void getMaxExpensePerMonth(ArrayList<MonthlyReport> monthlyInfo) {
        Optional<MonthlyReport> expenseCommodity = monthlyInfo.stream()
                .filter(MonthlyReport::isExpense)
                .max((el1, el2) -> (el1.getQuantity()*el1.getSumOfOne())-(el2.getQuantity()*el2.getSumOfOne()));
        if (expenseCommodity.isPresent()) { // isPresent - если есть значение
            String maxExpenseItemName = expenseCommodity.get().getItemName();
            int maxExpense = expenseCommodity.get().getQuantity() * expenseCommodity.get().getSumOfOne();
            System.out.print("Самая большая трата: " + maxExpenseItemName);
            System.out.println(", потрачено: " + maxExpense);
        } else {
            System.out.println("Товар не найден");
        }
    }

    public void getInfoYearlyReport() {
        System.out.println("Рассматриваемый год: 2021");
        System.out.println(yearlyReports);
        long expenseYearly = 0;
        long incomeYearly = 0;
        int countMoths = yearlyReports.size();

        for (int i = 1; i <= yearlyReports.size(); i++) {
            long expenseToMonth = yearlyReports.get(i).stream().mapToLong(report -> {
                if (report.isExpense()) {
                    return report.getAmount();
                } else {
                    return 0;
                }
            }).sum();
//            long expenseToMonth = findSum(yearlyReports, i, true);
            expenseYearly += expenseToMonth;
            long incomeToMonth = yearlyReports.get(i).stream().mapToLong(report -> {
                if (!report.isExpense()) {
                    return report.getAmount();
                } else {
                    return 0;
                }
            }).sum();
//            long incomeToMonth = findSum(yearlyReports, i, false);
            incomeYearly += incomeToMonth;
            long profit = incomeToMonth - expenseToMonth;
            System.out.println("Прибыль за " + listMonths.get(i - 1) + " составила: " + profit);
        }

        long averageExpenseYearly = expenseYearly / countMoths;
        long averageIncomeYearly = incomeYearly / countMoths;
        System.out.println("Средний расход за все месяцы: " + averageExpenseYearly);
        System.out.println("Средний доход за все месяцы: " + averageIncomeYearly);
    }

    public long findSum(HashMap<Integer, ArrayList<YearlyReport>> reports, int index, boolean isExpense) {
        return reports.get(index).stream().mapToLong(report -> {
            if (report.isExpense() == isExpense) {
                return report.getAmount();
            } else {
                return 0;
            }
        }).sum();
    }

    public long findSum(ArrayList<MonthlyReport> reports) {
        return reports.stream().mapToLong(report -> {
            if (report.isExpense()) {
                return (long) report.getQuantity() * report.getSumOfOne();
            } else {
                return 0;
            }
        }).sum();
    }
}
