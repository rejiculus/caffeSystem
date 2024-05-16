package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import models.Order;

public class MainSystem {
    private MonitoringSystem ms;
    private OrderSystem os;
    private PreparingSystem ps;
    private DeliverySystem ds;
    private StockSystem ss;
    
    List<Order> allOrders;
    PriorityQueue<Order> preparing;
    PriorityQueue<Order> delivery;

    //

    private List<Integer> freeTables;

    public MainSystem(){
        allOrders=new ArrayList<>();
        preparing=new PriorityQueue<>();
        delivery=new PriorityQueue<>();
        
        ss=new StockSystem(ProductRepository.getInstance());
        ms=new MonitoringSystem(this, allOrders);
        os=new OrderSystem(this, ss);
        //ps=new PreparingSystem(this, preparing);
        //ds=new DeliverySystem(this, delivery);
    }
    //

    public void orderToPreparing(Order o){//fixme
        System.out.println("Income to preparing: "+o);
    };
    public void orderToDelivery(Order o){//fixme
        System.out.println("Income to Delivery: "+o);
    };
    public void orderToClose(Order o){//fixme
        System.out.println("Income to History: "+o);
    };



    public MonitoringSystem getMs() {
        return ms;
    }

    public OrderSystem getOs() {
        return os;
    }

    public PreparingSystem getPs() {
        return ps;
    }

    public DeliverySystem getDs() {
        return ds;
    }

    public StockSystem getSs() {
        return ss;
    }

    public boolean hasFreeTables() {
        return !freeTables.isEmpty();
    }

    public int getFreeTable() {
        return freeTables.remove(0);
    }
    
    public void start(){//todo
        if(ds!=null) ds.start();
        if(ps!=null) ps.start();
        if(ms!=null) ms.start();
        if(os!=null) os.start();
    }
}
