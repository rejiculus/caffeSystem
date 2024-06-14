package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Order;

public class historyRepository {


    public List<Order> getHistory(){
        return new ArrayList<Order>();
    }

    public static void send(Order o){
        System.out.println(o);
    }
}
