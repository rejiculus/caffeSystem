package controllers;

import java.util.Map;
import java.util.TreeMap;
import models.Product;
import models.ProductType;

public class ProductRepository {
    private static TreeMap<Product,Integer> products;
    private static ProductRepository instance;

    private ProductRepository(){
        products=new TreeMap<>();
        products.put(new Product("soup",ProductType.HOT_FOOD,100.0,false),10);
        products.put(new Product("tea",ProductType.HOT_DRINK,50.0,true),10);
        products.put(new Product("coffee",ProductType.HOT_DRINK,150.0,true),10);
        products.put(new Product("cake",ProductType.FOOD,300.0,true),10);
        products.put(new Product("sidr",ProductType.ALCOHOL,200.0,true),10);
    }
    public static ProductRepository getInstance(){
        if(instance==null)
            instance=new ProductRepository();
        return instance;
    }

    public static Map<Product,Integer> getAll(){
        return new TreeMap<>(products);
    }

    public static Product getProductByName(String name){
        for(Product p:products.keySet()){
            if(name.equals(p.getName()))
                return p;
        }
        return null;
    }
    public static int getCount(Product product){
        return products.get(product);
    }

}
