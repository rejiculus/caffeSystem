package controllers;

import java.util.PriorityQueue;
import models.Order;

public class PreparingSystem extends Thread {
    private PriorityQueue<Order> orders;
    private static boolean isWorkTime;
    private DeliverySystem delivery;

    public PreparingSystem(DeliverySystem delivery) {
        this.delivery = delivery;
        this.orders=new PriorityQueue<>();
        isWorkTime=true;
    }

    public void run(){
        try{
            while(isWorkTime){
                if(!orders.isEmpty()){
                    Order o = orders.poll();
                    wait(1000);

                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    public void put(Order o){
        if(o==null) throw new RuntimeException("Null");
        orders.add(o);
    }

    public void turnOn(){
        isWorkTime=true;
    }
    public void turnOff(){
        isWorkTime=false;
    }
}
