package controllers;

import java.util.ArrayList;
import java.util.PriorityQueue;
import models.Order;

public class MonitoringSystem extends Thread{
    private ArrayList<Order> orders;
    private OrderSystem orderSystem;
    private PreparingSystem preparingSystem;
    private DeliverySystem deliverySystem;

    public MonitoringSystem(){
        deliverySystem=new DeliverySystem();
        preparingSystem=new PreparingSystem(deliverySystem);
        orderSystem=new OrderSystem(preparingSystem,orders);
    }
}
