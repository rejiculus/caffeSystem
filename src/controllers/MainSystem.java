package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import models.Order;
import params.CaffeParams;

public class MainSystem {
    private MonitoringSystem ms;
    private OrderSystem os;
    private PreparingSystem ps;
    private DeliverySystem ds;
    private StockSystem ss;

    List<Order> allOrders;
    PriorityQueue<Order> preparing;
    PriorityQueue<Order> delivery;
    List<Order> historyList;
    //

    private List<Integer> freeTables;

    public MainSystem() {
        allOrders = Collections.synchronizedList(new ArrayList<>());
        preparing = new PriorityQueue<>();
        delivery = new PriorityQueue<>();
        historyList = Collections.synchronizedList(new ArrayList<>());

        ss = new StockSystem(ProductRepository.getInstance());
        ms = new MonitoringSystem(this, allOrders);
        os = new OrderSystem(this, ss);
        ps = new PreparingSystem(this, preparing);

        freeTables = new ArrayList<>();
        for (int i = 1; i <= CaffeParams.TABLE_COUNT; i++)
            freeTables.add(i);

        // ps=new PreparingSystem(this, preparing);
        // ds=new DeliverySystem(this, delivery);
    }
    //

    public synchronized void orderToPreparing(Order o) {// fixme
        this.allOrders.add(o);
        this.preparing.add(o);
        System.out.println("Income to preparing: " + o);
    };

    public synchronized void orderToDelivery(Order o) {// fixme
        this.delivery.add(o);
        System.out.println("Income to Delivery: " + o);
    };

    public synchronized void orderToClose(Order o) {// fixme
        this.allOrders.remove(o);
        this.historyList.add(0, o);
        System.out.println("Income to History: " + o);
    };

    private void trimHistoryList() {// todo list dosnt trim
        if (historyList.size() > 100) {
            for (int i = historyList.size() - 1; i > 50; i--) {
                Order o = historyList.remove(i);
                historyRepository.send(o);
            }
        }
    }

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

    public void start() {// todo
        if (ds != null)
            ds.start();
        if (ps != null)
            ps.start();
        if (ms != null)
            ms.start();
        if (os != null)
            os.start();
        while (CaffeParams.isRun) {
            try {
                Thread.sleep(60_0000);
                trimHistoryList();
            } catch (InterruptedException e) {
            }
        }
    }
}
