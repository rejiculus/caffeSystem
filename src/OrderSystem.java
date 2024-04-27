import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class OrderSystem {
    private List<Integer> freeTables;
    private PriorityQueue<Order> orders;
    private PriorityQueue<Order> ordersDelivery;
    private List<Order> history;
    private BufferedReader br;

    public OrderSystem(){
        freeTables=new ArrayList<>();
        orders=new PriorityQueue<>();
        ordersDelivery=new PriorityQueue<>();
        br = new BufferedReader(new InputStreamReader(System.in));
        history=new ArrayList<>();

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
                showProductsInOrder(o);
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

            }
            while (!o.getItems().isEmpty()) {
                System.out.println("Delivery or toGo[1,2]:");

                name = br.readLine();

                if (name.equals("exit") || name.equals("Exit") || name.equals("-1"))
                    break;

                if (name.equals("1")) {
                    o.setDelivery(freeTables.remove(0));
                    break;
                } else if(name.equals("2"))
                    break;
            }
        }catch (IOException e){
            System.out.println(e);
        }
        o.makeOrder();
        orders.add(o);

    }

    public void cooking(){
        while(!orders.isEmpty()){
            Order o = orders.poll();
            //wait
            o.orderCookComplite();
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
    private void showMenu(){
        System.out.println("Menu:");
        for(Product p: ProductRepository.getAll()){
            System.out.println(p.getName()+" "+p.getCost()+" "+p.getType());
        }
        System.out.println("exit: -1");
        System.out.print("Enter product name: ");
    }
    private void showProductsInOrder(Order o){
        System.out.println("Products: ");
        o.getItems().keySet().stream()
                .map(Product::getName)
                .forEach(x->System.out.printf(" %s - %d\n",x,(o.getItems().get(ProductRepository.getByName(x))) ));
        System.out.println("Cost: "+o.getCost());
        System.out.println();
        System.out.println();
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