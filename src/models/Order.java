package models;

import java.sql.Time;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import common_exceptions.ContainsException;
import common_exceptions.LessThanZeroException;
import common_exceptions.NullParamException;
import common_exceptions.OrderException;

public class Order implements Comparable<Order> {
    /*
     * todo возможно стоит сделать этот класс наследником коллекции
     */
    private static Long orderCount = 0L;
    private Long id;
    private Map<Product, Integer> items;
    private double cost;
    private Time time;
    private Time preparedTime;
    private Time deliveredTime;
    private OrderState state;
    private OrderDelivery delivery;
    private boolean canToGo;

    public Order() {
        this.items = new TreeMap<>();
        this.cost = 0.0;
        this.state = OrderState.COMPILED;
        this.canToGo = true;
    }


    // add elements //
    public void addItem(Product product) {
        addItem(product, 1);
    }

    public void addItem(Product product, int count) {
        if (product == null)
            throw new NullParamException(this.id);
        if (state != OrderState.COMPILED)
            throw new OrderException(this.id, OrderState.COMPILED, this.state);
        if (count < 1)
            throw new LessThanZeroException(this.id);

        if (items.containsKey(product))
            items.put(product, items.get(product) + count);
        else
            items.put(product, count);

        if (!product.getIsToGo())
            this.canToGo = false;
        calcCost(); // update cost
    }

    // del elements //
    public void deleteItem(Product product) {
        if (product == null)
            throw new NullParamException(this.id);
        if (state != OrderState.COMPILED)
            throw new OrderException(this.id, OrderState.COMPILED, this.state);
        if (!items.containsKey(product))
            throw new ContainsException(this.id, product.getName());

        items.remove(product);
        canToGo = items.keySet().stream().map(Product::getIsToGo).anyMatch(x -> !x);
        calcCost();
    }

    public void deleteItem(Product product, int count) {
        if (product == null)
            throw new NullParamException(this.id);
        if (state != OrderState.COMPILED)
            throw new OrderException(this.id, OrderState.COMPILED, this.state);
        if (count < 1)
            throw new LessThanZeroException(this.id);
        if (!items.containsKey(product))
            throw new ContainsException(this.id, product.getName());
        if (items.get(product) < count)
            throw new ContainsException(this.id, product.getName(), count);

        if (count == items.get(product)) {
            deleteItem(product);
        } else {
            items.put(product, items.get(product) - count);
            calcCost(); // update cost
        }

    }

    // contains //
    public boolean contains(Product product) {
        return items.containsKey(product);
    }

    // order state methods
    public void makeOrder() {
        this.time = new Time(System.currentTimeMillis());
        this.state = OrderState.IN_PROGRESS;
        this.id = orderCount;
        orderCount++;
    }

    public void orderCookComplete() {
        preparedTime = new Time(System.currentTimeMillis());
        if (delivery != null)
            state = OrderState.DELIVERE;
        else
            state = OrderState.READY;
    }

    public void orderClose() {
        deliveredTime = new Time(System.currentTimeMillis());
        state = OrderState.COMPLETE;
    }

    //
    private void calcCost() {
        cost = 0.0;
        items.keySet().stream().map(item -> items.get(item) * item.getCost())
                .forEach(c -> cost += c);
    }

    // getters
    public OrderState getState() {
        return this.state;
    }

    public Map<Product, Integer> getItems() {
        return items;// fixme
    }

    public double getCost() {
        return cost;
    }

    public Time getTime() {
        return time;
    }

    public boolean isCanChange() {
        return state == OrderState.COMPILED;
    }

    public Long getId() {
        return this.id;
    }

    public OrderDelivery getDelivery() {
        return delivery;
    }

    public Time getPreparedTime() {
        return preparedTime;
    }

    public Time getDeliveredTime() {
        return deliveredTime;
    }

    public boolean isCanToGo() {
        return canToGo;
    }

    // setters
    public void setDelivery(OrderDelivery delivery) {
        if (state.equals(OrderState.COMPILED))
            this.delivery = delivery;
    }

    public void setDelivery(int table) {
        if (state.equals(OrderState.COMPILED))
            this.delivery = new OrderDelivery(table);
    }

    //
    @Override
    public int compareTo(Order order) {
        return time.compareTo(order.getTime());
    }

    @Override
    public int hashCode() {
        return time.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || !this.getClass().equals(o.getClass()))
            return false;
        Order or = (Order) o;
        return items.equals(or.getItems()) && this.cost == or.getCost() && this.time == or.getTime()
                && this.state.equals(or.state);
    }

    @Override
    public String toString() {
        return "Order{" + "items: {"
                + items.keySet().stream().map(x -> x.getName() + ": " + items.get(x))
                        .collect(Collectors.joining(", "))
                + "}, " + "cost: " + this.cost + ", " + "time: " + this.time + ", " + "state: "
                + this.state + "}";
    }

}
