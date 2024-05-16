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









    private BufferedReader br;


    public OrderSystem(MainSystem ms, StockSystem ss) {
        this.mainSystem = ms;
        this.stockSystem = ss;
        this.isWorkTime = true;
        helper = UserIOHelper.getInstance();
    }



    public void createOrder() {
        if (helper == null)
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
            if (c < 1) continue; //FIXME Какой в этом смысл
            o.addItem(p, c);
            
            stockSystem.reserveProduct(o, p, c);
        }

        while (!o.getItems().isEmpty()) {

            name = helper.input(showDeliveryPlaces()); // fixme check free tables in set

            if (name.equals("exit") || name.equals("Exit") || name.equals("-1"))
                break;//fixme выход в никуда (баг)

            if (name.equals("1") && mainSystem.hasFreeTables()) {
                o.setDelivery(mainSystem.getFreeTable());
                break;
            } else if (name.equals("2") || !mainSystem.hasFreeTables())
                break;
        }
        o.makeOrder();
        mainSystem.orderToPreparing(o);
        helper.out(showOrderId(o) + showDeliveryPlace(o));

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
        deleteProduct(order, product, 1);//todo change to delete product from list 
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

        if (mainSystem.hasFreeTables())
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
        if (helper!=null)
            helper.close();
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
