import java.util.Objects;

public class YearlyReport {
    private final int month; // месяц
    private final int amount; // сумма
    private final boolean isExpense; // true - трата, false - доход

    public YearlyReport(int month, int amount, boolean isExpense) {
        this.month = month;
        this.amount = amount;
        this.isExpense = isExpense;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YearlyReport that = (YearlyReport) o;
        return month == that.month && amount == that.amount && isExpense == that.isExpense;
    }

    @Override
    public int hashCode() {
        return Objects.hash(month, amount, isExpense);
    }

    @Override
    public String toString() {
        return "YearlyReport{" +
                "month=" + month +
                ", amount=" + amount +
                ", isExpense=" + isExpense +
                '}' + "\n";
    }

    public int getAmount() {
        return amount;
    }

    public boolean isExpense() {
        return isExpense;
    }
}
