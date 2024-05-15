package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import common_exceptions.ContainsException;
import common_exceptions.LessThanZeroException;
import common_exceptions.NullParamException;
import models.Order;
import models.Product;
import params.CaffeParams;

public class OrderSystem extends Thread {
    private MainSystem mainSystem;
    private StockSystem stockSystem;
    private boolean isWorkTime;
    private UserIOHelper helper;









    private PreparingSystem preparingSystem;
    private BufferedReader br;

    private List<Integer> freeTables;
    private PriorityQueue<Order> ordersDelivery;
    private List<Order> history;

    {
        freeTables = new ArrayList<>();
        ordersDelivery = new PriorityQueue<>();
        history = new ArrayList<>();
        for (int i = 0; i < CaffeParams.TABLE_COUNT; i++) {
            freeTables.add(i);
        }
    }

    public OrderSystem(PreparingSystem preparingSystem, List<Order> orders) {
        this.br = new BufferedReader(new InputStreamReader(System.in));
        this.isWorkTime = true;
        this.preparingSystem = preparingSystem;
        helper = UserIOHelper.getInstance();
    }



    public void createOrder() {
        if (br == null)
            throw new RuntimeException("Input stream was closed!");// ?

        Order o = new Order();
        String name = "";
        Product p;
        while (!name.equals("-1")) {


            name = helper.input(showShoppingBasket(o) + showMenu());


            if (name.equals("exit") || name.equals("Exit") || name.equals("-1"))
                break;

            p = ProductRepository.getProductByName(name);
            if (p == null)
                continue;
            //
            name = helper.input("How many?(defaul=1)");
            int c = 0;
            if (!name.isEmpty())
                c = Integer.parseInt(name);
            if (c != 0)
                o.addItem(p, c);
            else
                o.addItem(p);
            
            stockSystem.reserveProduct(o, p, c);
        }

        while (!o.getItems().isEmpty()) {

            name = helper.input(showDeliveryPlaces()); // fixme check free tables in set

            if (name.equals("exit") || name.equals("Exit") || name.equals("-1"))
                break;

            if (name.equals("1") && hasFreeTables()) {
                o.setDelivery(getFreeTable());
                break;
            } else if (name.equals("2") || !hasFreeTables())
                break;
        }
        o.makeOrder();
        mainSystem.orderToPreparing(o);
        helper.out(showOrderId(o) + showDeliveryPlace(o));


    }


    public List<Order> getHistory() {
        return List.copyOf(this.history);
    }


    private boolean hasFreeTables() {
        return !freeTables.isEmpty();
    }

    private int getFreeTable() {
        return freeTables.remove(0);
    }

    // add
    private void addProduct(Order o, Product product) {
        addProduct(o, product, 1);
    }

    private void addProduct(Order o, Product product, int count) {
        if (product == null)
            throw new NullParamException();
        if (count <= 0)
            throw new LessThanZeroException();

        stockSystem.reserveProduct(o, product, count);

        o.addItem(product, count);
    }

    // delete
    private void deleteProduct(Order order, Product product) {
        deleteProduct(order, product, 1);
    }

    private void deleteProduct(Order order, Product product, int count) {
        if (order == null || product == null)
            throw new NullParamException();
        if (count <= 0)
            throw new LessThanZeroException();
        if (!order.contains(product))
            throw new ContainsException(order.getId(), product.getName());
        if (order.getItems().get(product) < count)
            throw new ContainsException(order.getId(), product.getName(), count);

        order.deleteItem(product, count);
        stockSystem.deleteProduct(order,product,count);
    }

    // show
    private String showMenu() {
        StringBuilder res = new StringBuilder();
        res.append("Menu:\n");
        for (Product p : ProductRepository.getAll().keySet()) {
            res.append(p.getName() + " " + p.getCost() + " " + p.getType() + "\n");
        }
        res.append("exit: -1\n");
        res.append("Enter product name: ");
        return res.toString();
    }

    private String showShoppingBasket(Order o) {
        StringBuilder res = new StringBuilder();
        res.append("Products: \n");
        o.getItems().keySet().stream().map(Product::getName)
                .forEach(x -> res.append(String.format(" %s - %d%n", x,
                        (o.getItems().get(ProductRepository.getProductByName(x))))));
        res.append("Cost: " + o.getCost() + "\n\n");
        return res.toString();
    }

    private String showDeliveryPlaces() {

        if (hasFreeTables())
            return "Were you want to eat? Here or toGo?[1,2]";
        else
            return "We have no free tables? (Enter)";
    }

    private String showDeliveryPlace(Order o) {
        if (o.getDelivery() == null)
            return String.format("Order %d will be given to customer.%n", o.getId());
        else
            return String.format("Order %d will be delivered to %d table.%n", o.getId(),
                    o.getDelivery().getTableNumber());
    }

    private String showOrderId(Order o) {
        return String.format("Your order number is %04d%n", o.getId() % 10000);
    }

    public void close() {
        try {
            if (br.ready())
                br.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        while (isWorkTime) {
            createOrder();
        }

    }
}
