import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class OrderSystem {
    private List<Integer> freeTables;
    private PriorityQueue<Order> orders;
    private HashMap<Product, Integer> reservedProducts;
    private Map<Product,Integer> stock;
    private PriorityQueue<Order> ordersDelivery;
    private List<Order> history;
    private BufferedReader br;

    public OrderSystem(){
        freeTables=new ArrayList<>();
        orders=new PriorityQueue<>();
        ordersDelivery=new PriorityQueue<>();
        br = new BufferedReader(new InputStreamReader(System.in));
        history=new ArrayList<>();
        reservedProducts=new HashMap<>();
        stock=ProductRepository.getAll();

        for(int i=0;i<CaffeParams.TABLE_COUNT;i++){
            freeTables.add(i);
        }
    }

    public void run(){
        createOrder();
        createOrder();
        cooking();
        delivery();
    }


    public void createOrder(){
        Order o = new Order();
        String name="";
        Product p=null;
        try {
            while (!name.equals("-1")) {
                showShoppingBasket(o);
                showMenu();

                name = br.readLine();


                if (name.equals("exit") || name.equals("Exit") || name.equals("-1"))
                    break;

                p = ProductRepository.getByName(name);
                if (p == null)
                    continue;
                System.out.println("How many?(defaul=1)");
                name =br.readLine();
                int c=0;
                if(!name.isEmpty())
                    c=Integer.parseInt(name);
                if(c!=0)
                    o.addItem(p,c);
                else
                    o.addItem(p);
                reservedProducts.put(p,c);
            }
            while (!o.getItems().isEmpty()) {
                showDeliveryPlaces();

                name = br.readLine();

                if (name.equals("exit") || name.equals("Exit") || name.equals("-1"))
                    break;

                if (name.equals("1")) {
                    o.setDelivery(freeTables.remove(0));
                    break;
                } else if(name.equals("2"))
                    break;
            }
            o.makeOrder();
            orders.add(o);
            showOrderId(o);
            showDeliveryPlace(o);
        }catch (IOException e){
            System.out.println(e);
        }


    }

    public void cooking(){
        while(!orders.isEmpty()){
            Order o = orders.poll();
            //wait
            o.orderCookComplete();
            System.out.println(o.getTime());
            ordersDelivery.add(o);
        }
    }
    public void delivery(){
        while(!ordersDelivery.isEmpty()) {
            Order o = ordersDelivery.poll();
            o.orderClose();
            if(o.getDelivery()!=null)
                System.out.println("Order " + o + " delivered to" + o.getDelivery().getTableNumber());
            else
                System.out.println("Order " + o + " gave to customers");
        }
    }


    public List<Order> getHistory(){
        return this.history;//fixme изменение истории инвне
    }



    //add
    private void addProduct(Product product){
        addProduct(product,1);
    }
    private void addProduct(Product product,int count){
        if(product==null) throw new RuntimeException("Params can't be null!");
        if(count<=0) throw new RuntimeException("Count must be more then zero.");
        if(!stock.containsKey(product)) throw new RuntimeException("Incorrect product - "+product.getName());
        if(stock.get(product)<count) throw new RuntimeException("Have no enough products - "+product.getName()+" - "+count);

        stock.put(product,stock.get(product)-count);

        if(reservedProducts.containsKey(product))
            count += reservedProducts.get(product);

        reservedProducts.put(product,count);
    }

    private void deleteProduct(Order order, Product product){
        deleteProduct(order,product,1);
    }
    private void deleteProduct(Order order, Product product, int count){
        if(order==null || product==null) throw new RuntimeException("Params can't be null!");
        if(count<=0) throw new RuntimeException("Count must be more then zero.");
        if(!order.contains(product)) throw new RuntimeException(String.format("Order %d have no %s.",order.getId(),product.getName()));
        if(order.getItems().get(product)<count) throw new RuntimeException(String.format("Order %d have no %s enough (%d).",order.getId(),product.getName(),count));
        if(!reservedProducts.containsKey(product) || reservedProducts.get(product)<count) throw new RuntimeException(String.format("Something wrong: in order %d reserved less products then have to delete!",order.getId()));

        order.deleteItem(product,count);

        reservedProducts.put(product, reservedProducts.get(product)-count);

        if(stock.containsKey(product))
            count+=stock.get(product);
        stock.put(product,count);
    }
    // show
    private void showMenu() {
        System.out.println("Menu:");
        for(Product p: ProductRepository.getAll().keySet()){
            System.out.println(p.getName()+" "+p.getCost()+" "+p.getType());
        }
        System.out.println("exit: -1");
        System.out.print("Enter product name: ");
    }
    private void showShoppingBasket(Order o){
        System.out.println("Products: ");
        o.getItems().keySet().stream()
                .map(Product::getName)
                .forEach(x->System.out.printf(" %s - %d\n",x,(o.getItems().get(ProductRepository.getByName(x))) ));
        System.out.println("Cost: "+o.getCost());
        System.out.println();
        System.out.println();
    }
    private void showDeliveryPlaces()
    {
        System.out.println("Were you want to eat? Here or toGo?[1,2]");
    }
    private void showDeliveryPlace(Order o){
        if(o.getDelivery()==null)
            System.out.printf("Order %d will be given to customer.\n",o.getId());
        else
            System.out.printf("Order %d will be delivered to %d table.\n",o.getId(),o.getDelivery().getTableNumber());
    }
    private void showOrderId(Order o){
        System.out.printf("Your order number is %04d\n",o.getId()%10000);
    }

    public void close()  {
        try{
            if(br.ready())
                br.close();
        } catch(IOException e){
            System.out.println(e);
        }

    }
}