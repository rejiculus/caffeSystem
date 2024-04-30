import java.util.PriorityQueue;

public class DeliverySystem extends Thread{
    private PriorityQueue<Order> orders;
    private static boolean isWorkTime;

    public DeliverySystem(){
    }
    @Override
    public void run() {
        super.run();
    }
}
