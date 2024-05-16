package controllers;

import java.util.List;
import models.Order;
import models.OrderState;
import params.CaffeParams;

public class MonitoringSystem extends Thread{
    private List<Order> orders;
    private MainSystem mainSystem;

    public MonitoringSystem(MainSystem mainSystem, List<Order> orders){
        this.orders=orders;
        this.mainSystem=mainSystem;
    }

    @Override
    public void run(){
        while(CaffeParams.isRun){
            orders.stream()
                .filter(order-> order.getState().equals(OrderState.COMPLETE))
                .forEach(System.out::println);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    System.out.println(e);
                }
        }
    }
}
