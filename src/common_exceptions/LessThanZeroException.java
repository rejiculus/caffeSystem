package common_exceptions;

public class LessThanZeroException extends RuntimeException{
    public LessThanZeroException(){
        super("Value can't be less then zero!");
    }
    public LessThanZeroException(Long id){
        super(String.format("Value can't be less then zero! Order - %d",id));
    }
    
}
