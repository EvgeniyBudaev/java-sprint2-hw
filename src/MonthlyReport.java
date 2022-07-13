import java.util.Objects;

public class MonthlyReport {
    private final String itemName; // название товара
    private final boolean isExpense;  // true - трата, false - доход
    private final int quantity; // количество товара
    private final int sumOfOne; // стоимость единицы товара

    public MonthlyReport(String itemName, boolean isExpense, int quantity, int sumOfOne) {
        this.itemName = itemName;
        this.isExpense = isExpense;
        this.quantity = quantity;
        this.sumOfOne = sumOfOne;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyReport that = (MonthlyReport) o;
        return isExpense == that.isExpense && quantity == that.quantity && sumOfOne == that.sumOfOne && Objects.equals(itemName, that.itemName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemName, isExpense, quantity, sumOfOne);
    }

    @Override
    public String toString() {
        return "MonthlyReport{" +
                "itemName='" + itemName + '\'' +
                ", isExpense=" + isExpense +
                ", quantity=" + quantity +
                ", sumOfOne=" + sumOfOne +
                '}' + "\n";
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isExpense() {
        return isExpense;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getSumOfOne() {
        return sumOfOne;
    }

    public int getSum() {
        return quantity * sumOfOne;
    }
}
