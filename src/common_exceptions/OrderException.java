package common_exceptions;
import models.OrderState;

public class OrderException extends RuntimeException{
    public OrderException(Long id, OrderState mustBeState, OrderState state){
        super(String.format("Order - %d is not %s! But is %s now",id,mustBeState, state));
    }
    
}
