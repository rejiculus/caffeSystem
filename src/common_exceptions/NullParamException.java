package common_exceptions;

public class NullParamException extends RuntimeException{
    public NullParamException(){
        super("Param can't be null!");
    }
    public NullParamException(Long id){
        super("You can't put null in parameters! Order-" + id);
    }
    
}
