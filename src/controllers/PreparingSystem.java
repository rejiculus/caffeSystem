package controllers;

import java.util.PriorityQueue;
import java.util.Queue;
import models.Order;
import params.CaffeParams;

public class PreparingSystem extends Thread {
    private Queue<Order> orders;
    private static boolean isWorkTime;
    private DeliverySystem delivery;
    private MainSystem mainSystem;

    public PreparingSystem(MainSystem mainSystem, Queue<Order> preparingOrders) {
        this.mainSystem = mainSystem;
        this.orders = preparingOrders;
        isWorkTime = true;
    }

    public void run() {
        try {
            while (CaffeParams.isRun) {
                if (!orders.isEmpty()) {
                    Order o = orders.poll();
                    sleep(o.getItems().size() * 5000);
                    o.orderCookComplete();
                    mainSystem.orderToDelivery(o);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void put(Order o) {
        if (o == null)
            throw new RuntimeException("Null");
        orders.add(o);
    }

    public void turnOn() {
        isWorkTime = true;
    }

    public void turnOff() {
        isWorkTime = false;
    }
}
