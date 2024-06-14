package controllers;

import java.util.List;
import models.Order;
import models.OrderState;
import params.CaffeParams;
import viewer.ConsoleOut;

public class MonitoringSystem extends Thread{
    private List<Order> orders;
    private MainSystem mainSystem;
    private ConsoleOut out;

    public MonitoringSystem(MainSystem mainSystem, List<Order> orders){
        this.orders=orders;
        this.mainSystem=mainSystem;
        out = new ConsoleOut();
        out.start();
    }

    @Override
    public void run(){
        StringBuffer sb = new StringBuffer();
        while(CaffeParams.isRun){
            sb.setLength(0);
            sb.append("Preparing:\n");
            orders.stream()
                .filter(order-> order.getState().equals(OrderState.IN_PROGRESS))
                .forEach(order-> sb.append(order+"\n"));
            sb.append("Delivering:\n");
            orders.stream()
                .filter(order-> order.getState().equals(OrderState.DELIVERY))
                .forEach(order-> sb.append(order+"\n"));
            sb.append("Ready:\n");
            orders.stream()
                .filter(order-> order.getState().equals(OrderState.READY))
                .forEach(order-> sb.append(order+"\n"));

            out.setText(sb.toString());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    System.out.println(e);
                }
        }
    }
}
