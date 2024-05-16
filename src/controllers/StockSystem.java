package controllers;

import models.Product;
import java.util.HashMap;
import java.util.Map;
import common_exceptions.ContainsException;
import models.Order;

public class StockSystem {
    private Map<Product,Integer> stock;
    private Map<Product,Integer> reserved; // when product in become reserved we decrement stock product
    private ProductRepository productRepository;

    public StockSystem(ProductRepository productRepository){
        this.productRepository = productRepository;
        stock=productRepository.getAll();
        reserved=new HashMap<>();
    }



    public void reserveProduct(Order order, Product product){
        reserveProduct(order, product, 1);
    }
    public void reserveProduct(Order order, Product product, Integer count){

        if (!stock.containsKey(product))
            throw new ContainsException(product.getName());
        if (stock.get(product) < count)
            throw new ContainsException(product.getName(),count);

         stock.put(product, stock.get(product) - count);

        if (reserved.containsKey(product))
            count += reserved.get(product);

        reserved.put(product, count);
    }
    //public void deleteProduct(Order order, Product product);
    public void deleteProduct(Order order, Product product, Integer count){

        if (!reserved.containsKey(product) || reserved.get(product) < count)
            throw new ContainsException(order.getId());


        reserved.put(product, reserved.get(product) - count);

        if (stock.containsKey(product))
            count += stock.get(product);
        stock.put(product, count);
    }
    
}
