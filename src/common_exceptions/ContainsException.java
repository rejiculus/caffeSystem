package common_exceptions;

public class ContainsException  extends RuntimeException{
    public ContainsException(Long id, String product){
        super(String.format("Order - %d is not contains %s!",id,product));
    }
    public ContainsException(Long id, String product,int count){
        super(String.format("Order - %d contains %s less than %d!",id,product,count));
    }
    
}
