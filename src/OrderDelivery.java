import java.util.Objects;

public class OrderDelivery {
    private int tableNumber;

    OrderDelivery(int table) {
        if (table < 0 || table > CaffeParams.TABLE_COUNT)
            throw new RuntimeException("Incorrect table");
        tableNumber = table;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        if (tableNumber < 0 || tableNumber > CaffeParams.TABLE_COUNT)
            throw new RuntimeException("Incorrect table");
        this.tableNumber = tableNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDelivery that = (OrderDelivery) o;
        return tableNumber == that.tableNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableNumber);
    }
}
