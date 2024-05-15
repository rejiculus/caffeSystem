package common_exceptions;

public class ContainsException  extends RuntimeException{
    public ContainsException(Long id){
        super(String.format(
            "Something wrong: in order %d reserved less products then have to delete!",id));
    }
    public ContainsException(String product){
        super(String.format("It is not contains %s!",product));
    }
    public ContainsException(String product,int count){
        super(String.format("It is contains %s less than %d!",product,count));
    }
    public ContainsException(Long id, String product){
        super(String.format("Order - %d is not contains %s!",id,product));
    }
    public ContainsException(Long id, String product,int count){
        super(String.format("Order - %d contains %s less than %d!",id,product,count));
    }
    
}
