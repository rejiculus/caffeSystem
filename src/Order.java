import java.sql.Time;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Order implements Comparable<Order>{
    private Map<Product,Integer> items;
    private double cost;
    private Time time;
    private boolean canChange;
    private OrderState state;
    private OrderDelivery delivery;

    public Order(){
        this.items = new TreeMap<>();
        this.cost=0.0;
        canChange=true;
        state=OrderState.Compiled;
    }
    private void calcCost(){
        items.keySet().stream().map(item->items.get(item)*item.getCost()).forEach(c->cost+=c);
    }
    public void addItem(Product product){
        if(state!=OrderState.Compiled) return;

        if(items.containsKey(product))
            items.put(product,items.get(product)+1);
        else
            items.put(product,1);

        calcCost();
    }
    public void addItem(Product product, int count){
        if(state!=OrderState.Compiled || count<1) return;// todo throw

        if(items.containsKey(product))
            items.put(product,items.get(product)+count);
        else
            items.put(product,count);
        calcCost();
    }
    public void deleteItem(Product product){
        if(state!=OrderState.Compiled || !items.containsKey(product)) return;// todo throw
        items.remove(product);
        calcCost();
    }
    public void deleteItem(Product product, int count){
        if(state!=OrderState.Compiled ||
                !items.containsKey(product) ||
                count> items.get(product))
            return;// todo throw

        items.put(product, items.get(product)-count);
        calcCost();
    }

    public Map<Product,Integer> getItems(){
        return items;//fixme
    }
    public double getCost(){
        return cost;
    }
    public Time getTime(){
        return time;
    }

    public boolean isCanChange() {
        return state==OrderState.Compiled;
    }

    public void makeOrder(){
        this.time=new Time(System.currentTimeMillis());
        this.canChange=false;
        this.state=OrderState.InProgress;
    }

    @Override
    public int compareTo(Order order) {
        return time.compareTo(order.getTime());
    }

    @Override
    public int hashCode(){
        return time.hashCode();
    }
    @Override
    public boolean equals(Object o) {
        if(this==o) return true;
        if(o==null || !this.getClass().equals(o.getClass())) return false;
        Order or = (Order) o;
        return items.equals(or.getItems()) &&
                this.cost == or.getCost() &&
                this.time == or.getTime() &&
                this.canChange == or.isCanChange();
    }
    @Override
    public String toString(){
        return "Order{"+
                "items: {"+items.keySet()
                                .stream()
                                .map(x->x.getName()+": "+items.get(x))
                                .collect(Collectors.joining(", "))+"}, "+
                "cost: "+this.cost+", "+
                "time: "+this.time+", "+
                "can change: "+this.canChange+"}";
    }

}