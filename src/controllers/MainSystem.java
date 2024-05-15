package controllers;

import java.util.List;
import java.util.PriorityQueue;
import models.Order;

public class MainSystem {
    private MonitoringSystem ms;
    private OrderSystem os;
    private PreparingSystem ps;
    private DeliverySystem ds;
    
    List<Order> allOrders;
    PriorityQueue<Order> preparing;
    PriorityQueue<Order> delivery;

    //

    private List<Integer> freeTables;
    //

    public orderToPreparing(Order o);
    public orderToDelivery(Order o);
    public orderToClose(Order o);



    private boolean hasFreeTables() {
        return !freeTables.isEmpty();
    }

    private int getFreeTable() {
        return freeTables.remove(0);
    }
    
}
