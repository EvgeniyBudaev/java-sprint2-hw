import java.util.*;
import java.util.function.ToLongFunction;

public class Report {
    private final String filesPath = "resources/";
    private final HashMap<Integer, ArrayList<MonthlyReport>> monthlyReports = new HashMap<>();
    private final HashMap<Integer, ArrayList<YearlyReport>> yearlyReports = new HashMap<>();
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
            String data = ReadFile.readFileContentsOrNull(monthPath, "месячным");
            if (data == null) {
                break;
            }
            String[] lines = data.split(System.lineSeparator());
            ArrayList<MonthlyReport> filesMonthly = new ArrayList<>();

            for (int j = 1; j < lines.length; j++) {
                String[] lineContents = lines[j].split(",");
                int _itemName = 0;
                String itemName = lineContents[_itemName];
                int _isExpenseMonthly = 1;
                boolean isExpense = Boolean.parseBoolean(lineContents[_isExpenseMonthly]);
                int _quantity = 2;
                int quantity = Integer.parseInt(lineContents[_quantity]);
                int _sumOfOne = 3;
                int sumOfOne = Integer.parseInt(lineContents[_sumOfOne]);
                MonthlyReport monthlyReport = new MonthlyReport(itemName, isExpense, quantity, sumOfOne);
                filesMonthly.add(monthlyReport);
            }
            monthlyReports.put(i, filesMonthly);
        }
        System.out.println("Все месячные отчёты считаны");
    }

    public void readYearlyFiles() {
        String data = ReadFile.readFileContentsOrNull(filesPath + "y.2021.csv", "годовым");
        if (data == null) {
            return;
        }
        String[] lines = data.split(System.lineSeparator());
        ArrayList<YearlyReport> filesYearly = new ArrayList<>();
        int month = 0;
        for (int j = 1; j < lines.length; j++) {
            String[] lineContents = lines[j].split(",");
            int _month = 0;
            month = Integer.parseInt(lineContents[_month]);
            int _amount = 1;
            int amount = Integer.parseInt(lineContents[_amount]);
            int _isExpenseYearly = 2;
            boolean isExpense = Boolean.parseBoolean(lineContents[_isExpenseYearly]);
            YearlyReport yearlyReport = new YearlyReport(month, amount, isExpense);
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
            return;
        }

        boolean isCorrectly = true;

        for (int i = 1; i <= monthlyReports.size(); i++) {
            ArrayList<MonthlyReport> monthlyReportPerMonth = monthlyReports.get(i);
            ArrayList<YearlyReport> yearlyReportPerMonth = yearlyReports.get(i);

            long expenseMonth = findSum(monthlyReportPerMonth, report -> report.isExpense() ? report.getSum() : 0);
            long incomeMonth = findSum(monthlyReportPerMonth, report -> !report.isExpense() ? report.getSum() : 0);

            long expenseYearly = findSum(yearlyReportPerMonth, report -> report.isExpense() ? report.getAmount() : 0);
            long incomeYearly = findSum(yearlyReportPerMonth, report -> !report.isExpense() ? report.getAmount() : 0);

            if (expenseMonth != expenseYearly) {
                System.out.printf("В %s несоответствие расходов%n", listMonths.get(i - 1));
                isCorrectly = false;
            } else if (incomeMonth != incomeYearly) {
                System.out.printf("В %s несоответствие доходов%n", listMonths.get(i - 1));
                isCorrectly = false;
            }
        }

        if (isCorrectly) {
            System.out.println("Операция успешно завершена");
        }
    }

    public void getInfoMonthlyReport() {
        if (monthlyReports.isEmpty()) {
            System.out.println("Cначала считайте годовой отчёт.");
            return;
        }

        for (int i = 1; i <= monthlyReports.size(); i++) {
            System.out.println(listMonths.get(i - 1) + ":");
            ArrayList<MonthlyReport> monthlyInfo = monthlyReports.get(i);
            try {
                getProfitableCommodityPerMonth(monthlyInfo);
                getMaxExpensePerMonth(monthlyInfo);
            } catch (NoEntityException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void getProfitableCommodityPerMonth(ArrayList<MonthlyReport> monthlyInfo) throws NoEntityException {
        if (monthlyInfo == null || monthlyInfo.isEmpty()) {
            throw new IllegalArgumentException("Список пуст");
        }

        MonthlyReport profitableCommodity = monthlyInfo.stream()
                .filter(el -> !el.isExpense())
                .max(Comparator.comparingInt(MonthlyReport::getSum))
                .orElseThrow(() -> new NoEntityException("Товар не найден"));

        String maxProfitItemName = profitableCommodity.getItemName();
        int maxProfit = profitableCommodity.getQuantity() * profitableCommodity.getSumOfOne();
        System.out.print("Самый прибыльный товар: " + maxProfitItemName);
        System.out.println(", продан на сумму: " + maxProfit);
    }

    public void getMaxExpensePerMonth(ArrayList<MonthlyReport> monthlyInfo) throws NoEntityException {
        MonthlyReport expenseCommodity = Optional.ofNullable(monthlyInfo)
                .stream()
                .flatMap(Collection::stream)
                .filter(MonthlyReport::isExpense)
                .max(Comparator.comparingInt(MonthlyReport::getSum))
                .orElseThrow(() -> new NoEntityException("Товар не найден"));
        String maxExpenseItemName = expenseCommodity.getItemName();
        int maxExpense = expenseCommodity.getQuantity() * expenseCommodity.getSumOfOne();
        System.out.print("Самая большая трата: " + maxExpenseItemName);
        System.out.println(", потрачено: " + maxExpense);
    }

    public void getInfoYearlyReport() {
        if (yearlyReports.isEmpty()) {
            System.out.println("Cначала считайте годовой отчёт.");
            return;
        }

        System.out.println("Рассматриваемый год: 2021");
        System.out.println(yearlyReports);
        long expenseYearly = 0;
        long incomeYearly = 0;
        int countMoths = yearlyReports.size();

        for (int i = 1; i <= yearlyReports.size(); i++) {
            long expenseToMonth = findSum(yearlyReports, i, true);
            expenseYearly += expenseToMonth;
            long incomeToMonth = findSum(yearlyReports, i, false);
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

    public <T> long findSum(ArrayList<? extends T> reports, ToLongFunction<T> function) {
        return reports.stream()
                .mapToLong(function)
                .sum();
    }
}
